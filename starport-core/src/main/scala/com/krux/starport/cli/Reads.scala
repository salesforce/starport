/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.cli

import java.time.{LocalDateTime, ZoneOffset, ZonedDateTime}

import scopt.Read
import scopt.Read.reads

import com.krux.starport.util.PipelineState

trait Reads {

  implicit val dateTimeRead: Read[LocalDateTime] = reads { r =>
    ZonedDateTime.parse(r).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
  }

  implicit val pipelineStateRead: scopt.Read[PipelineState.State] =
    scopt.Read.reads(PipelineState.withName)

}
