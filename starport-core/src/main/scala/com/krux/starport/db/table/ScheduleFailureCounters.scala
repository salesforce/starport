/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.db.table

import java.time.LocalDateTime

import slick.jdbc.PostgresProfile.api._

import com.krux.starport.db.record.ScheduleFailureCounter


class ScheduleFailureCounters(tag: Tag)
  extends Table[ScheduleFailureCounter](tag, "schedule_failure_counters") {

  def pipelineId = column[Int]("pipeline_id", O.PrimaryKey)

  def failureCount = column[Int]("failure_count")

  def updatedAt = column[LocalDateTime]("updated_at")

  def * = (pipelineId, failureCount, updatedAt).mapTo[ScheduleFailureCounter]

  def pipeline = foreignKey(
      "schedule_failure_counters_pipelines_fk", pipelineId, TableQuery[Pipelines]
    )(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

}

object ScheduleFailureCounters {
  def apply() = TableQuery[ScheduleFailureCounters]
}
