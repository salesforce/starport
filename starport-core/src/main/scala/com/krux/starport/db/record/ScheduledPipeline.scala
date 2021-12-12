/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.db.record

import java.time.LocalDateTime


case class ScheduledPipeline(
                              awsId: String,
                              pipelineId: Int,
                              pipelineName: String,
                              scheduledStart: LocalDateTime,
                              actualStart: LocalDateTime,
                              deployedTime: LocalDateTime,
                              status: String,
                              inConsole: Boolean,
                              pipelineStatus: String,
                              actualEnd: Option[LocalDateTime]
                            )
