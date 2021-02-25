/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.cli

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import com.krux.starport.util.{PipelineState, DateTimeFunctions}

case class CleanupUnmanagedOptions(
  excludePrefixes: Seq[String] = Seq(),
  pipelineState: PipelineState.State = PipelineState.FINISHED,
  cutoffDate: LocalDateTime = DateTimeFunctions.currentTimeUTC().minusMonths(2).toLocalDateTime().truncatedTo(ChronoUnit.DAYS),
  force: Boolean = false,
  dryRun: Boolean = false
)
