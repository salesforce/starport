/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport

import com.krux.starport.util.AwsDataPipeline

object SummarizeUnmanagedPipelines extends StarportActivity {

  def run(): Unit = {
    val pipelinesInAws = AwsDataPipeline.listPipelineIds() -- inConsoleManagedPipelineIds()
    val pipelineStatuses = AwsDataPipeline.describePipeline(pipelinesInAws.toSeq: _*)

    logger.info("Unmanaged Pipeline Count By State:")
    pipelineStatuses.values
      .groupBy(_.pipelineState)
      .view
      .mapValues(_.size)
      .toSeq
      .sortBy(_._2)(Ordering[Int].reverse)
      .foreach { case (state, count) => logger.info(s"${state.getOrElse("Unknown")} -> $count") }

    logger.info("Unmanaged Pipeline Count By Date:")
    pipelineStatuses.values
      .map(status => status.creationTime.flatMap(_.split("T").headOption).getOrElse("Unknown"))
      .groupBy(identity)
      .view
      .mapValues(_.size)
      .toSeq
      .sortBy(_._1)(Ordering[String].reverse)
      .foreach { case (date, count) => logger.info(s"$date -> $count") }
  }

  def main(args: Array[String]): Unit = {
    val start = System.nanoTime()
    run()
    val timeSpan = (System.nanoTime - start) / 1e9
    logger.info(s"Done in $timeSpan seconds")
  }

}
