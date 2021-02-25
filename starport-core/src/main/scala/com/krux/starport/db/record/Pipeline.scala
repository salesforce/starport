/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.db.record

import java.time.LocalDateTime


case class Pipeline(
  id: Option[Int],
  name: String,
  jar: String,
  `class`: String,
  isActive: Boolean,
  retention: Int,
  start: LocalDateTime,
  period: String,  // TODO make this of type period
  end: Option[LocalDateTime],
  nextRunTime: Option[LocalDateTime],
  backfill: Boolean,
  owner: Option[String]
)
