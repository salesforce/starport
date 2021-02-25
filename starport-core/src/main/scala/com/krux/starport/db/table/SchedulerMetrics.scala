/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.db.table

import java.time.LocalDateTime

import slick.jdbc.PostgresProfile.api._

import com.krux.starport.db.record.SchedulerMetric


class SchedulerMetrics(tag: Tag) extends Table[SchedulerMetric](tag, "scheduler_metrics") {

  def startTime = column[LocalDateTime]("start_time", O.PrimaryKey)

  def pipelineCount = column[Option[Int]]("pipeline_count")

  def endTime = column[Option[LocalDateTime]]("end_time")

  def * = (startTime, pipelineCount, endTime) <> (SchedulerMetric.tupled, SchedulerMetric.unapply)

}

object SchedulerMetrics {
  def apply() = TableQuery[SchedulerMetrics]
}
