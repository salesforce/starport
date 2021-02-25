/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.cli

import java.time.LocalDateTime

import com.krux.starport.util.DateTimeFunctions


case class SchedulerOptions(
  scheduledStart: LocalDateTime = DateTimeFunctions.currentTimeUTC().toLocalDateTime,
  scheduledEnd: LocalDateTime = DateTimeFunctions.currentTimeUTC().toLocalDateTime,
  actualStart: LocalDateTime = DateTimeFunctions.currentTimeUTC().toLocalDateTime
)
