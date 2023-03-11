/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.util

import scala.jdk.CollectionConverters._

import com.amazonaws.services.datapipeline.model.PipelineDescription

case class PipelineStatus(
  awsId: String,
  name: String,
  healthStatus: Option[String],
  pipelineState: Option[PipelineState.State],
  creationTime: Option[String]
)

object PipelineStatus {

  final val HealthStatusKey = "@healthStatus"
  final val PipelineStateKey = "@pipelineState"
  final val CreationTimeKey = "@creationTime"

  def apply(desc: PipelineDescription) = {
    val awsId = desc.getPipelineId()
    val fields = desc.getFields().asScala
      .map { f => f.getKey -> f.getStringValue }
      .toMap

    new PipelineStatus(
      awsId,
      desc.getName,
      fields.get(HealthStatusKey),
      fields.get(PipelineStateKey).map(PipelineState.withName),
      fields.get(CreationTimeKey)
    )
  }

}
