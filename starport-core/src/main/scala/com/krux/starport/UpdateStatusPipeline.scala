package com.krux.starport

import com.codahale.metrics.MetricRegistry
import com.krux.starport.db.record.ScheduledPipeline
import com.krux.starport.db.table.{Pipelines, ScheduledPipelines}
import com.krux.starport.metric.{ConstantValueGauge, MetricSettings}
import com.krux.starport.util.{AwsDataPipeline, ErrorHandler, PipelineState}
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDateTime

object UpdateStatusPipeline extends StarportActivity {

  val metrics = new MetricRegistry()

  lazy val reportingEngine: MetricSettings = conf.metricsEngine

  private def updateStatusToDeleted(awsId: String) = db
    .run(
      ScheduledPipelines()
        .filter(_.awsId === awsId)
        .map(_.pipelineStatus)
        .update("DELETED")
    )
    .waitForResult

  private def updateStatusToFinished(awsId: String, endTime: String) = db
    .run(
      ScheduledPipelines()
        .filter(_.awsId === awsId)
        .map(r => (r.pipelineStatus, r.actualEnd))
        .update((PipelineState.FINISHED.toString, Option(LocalDateTime.parse(endTime))))
    )
    .waitForResult

  private def updateStatusToFailed(awsId: String, endTime: String) = db
    .run(
      ScheduledPipelines()
        .filter(_.awsId === awsId)
        .map(r => (r.pipelineStatus, r.actualEnd))
        .update((PipelineState.FAILED.toString, Option(LocalDateTime.parse(endTime))))
    )
    .waitForResult

  def run(): Unit = {

    val inConsoleRunningPipelines: Seq[ScheduledPipeline] = db
      .run(
        ScheduledPipelines()
          .filter(sp =>
            sp.inConsole && sp.pipelineStatus === PipelineState.RUNNING.toString && sp.scheduledStart > java.time.LocalDateTime
              .now()
              .minusDays(3)
          )
          .result
      )
      .waitForResult

    inConsoleRunningPipelines.groupBy(_.pipelineId).par.foreach {case (pipelineId, scheduledPipelines) =>

        val pipelineRecord = db.run(Pipelines().filter(_.id === pipelineId).take(1).result)
          .waitForResult
          .head

        try {

          val pipelineIdPrefix = s"|PipelineId: $pipelineId|"
          logger.info(
            s"$pipelineIdPrefix has ${scheduledPipelines.size} In-Console Running Pipelines in DB."
          )

          val awsManagedPipelineStatus =
            AwsDataPipeline.describePipeline(scheduledPipelines.map(_.awsId): _*)

          val awsManagedKeySet = awsManagedPipelineStatus.keySet
          logger.info(s"$pipelineIdPrefix AWS contains ${awsManagedKeySet.size}")

          val inDatabaseButNotInAws = scheduledPipelines.map(_.awsId).toSet -- awsManagedKeySet
          logger.info(
            if (inDatabaseButNotInAws.nonEmpty)
              s"$pipelineIdPrefix updating the status to DELETED for ${inDatabaseButNotInAws.size} entries"
            else ""
          )

          inDatabaseButNotInAws.foreach(updateStatusToDeleted)

          //Finished does not mean successful, it will contain Failed & Successful both. Failed is Pipeline finished or no longer running but With Errors in any step.
          val finishedPipelines = scheduledPipelines.filter { p =>
            awsManagedPipelineStatus
              .get(p.awsId)
              .flatMap(_.pipelineState)
              .contains(PipelineState.FINISHED)
          }

          logger.info(s"$pipelineIdPrefix has ${finishedPipelines.size} finished pipelines.")

          val (failedPipelines, healthyPipelines) = finishedPipelines.partition { p =>
            awsManagedPipelineStatus.get(p.awsId).flatMap(_.healthStatus).contains("ERROR")
          }

          val healthyAwsIds = healthyPipelines.map(_.awsId)
          val healthyPipelinesDesc = awsManagedPipelineStatus
            .filter(dataFromAWS => healthyAwsIds.contains(dataFromAWS._2.awsId))

          val failedAwsIds = failedPipelines.map(_.awsId)
          val failedPipelinesDesc = awsManagedPipelineStatus
            .filter(dataFromAWS => failedAwsIds.contains(dataFromAWS._2.awsId))

          healthyPipelinesDesc
            .map(data => updateStatusToFinished(data._2.awsId, data._2.finishedTime.orNull))
          failedPipelinesDesc
            .map(data => updateStatusToFailed(data._2.awsId, data._2.finishedTime.orNull))

        } catch {
          case e: Exception => println(e.printStackTrace())
          ErrorHandler.statusUpdateActivityFailed(pipelineRecord, e.getStackTrace)
        }

    }
  }

  def main(args: Array[String]): Unit = {

    val reporter = reportingEngine.getReporter(metrics)

    val start = System.nanoTime
    try {
      run()

      val timeSpan = (System.nanoTime - start) / 1e9
      logger.info(s"All pipelines Status Updated in $timeSpan seconds")

      val numActivePipelines = CleanupExistingPipelines.activePipelineRecords()

      metrics.register(
        "gauges.active_pipeline_count", new ConstantValueGauge(numActivePipelines)
      )
    } finally {
      reporter.report()
      reporter.close()
    }
  }

}
