/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.cli

import java.time.LocalDateTime

import scopt.OParser

import com.krux.starport.BuildInfo

object SchedulerOptionParser extends Reads {

  val name = "start-scheduled-pipelines"

  val parser = {
    val builder = OParser.builder[SchedulerOptions]
    import builder._
    OParser.sequence(
      programName(name),
      head(name, BuildInfo.version),
      help("help").text("prints this usage text"),
      opt[LocalDateTime]("start")
        .valueName("<scheduledStart>")
        .action((x, c) => c.copy(scheduledStart = x)),
      opt[LocalDateTime]("end")
        .valueName("<scheduledEnd>")
        .action((x, c) => c.copy(scheduledEnd = x)),
      opt[LocalDateTime]("actual-start")
        .valueName("<actualStart>")
        .action((x, c) => c.copy(actualStart = x))
    )
  }

  def parse(args: Array[String]): Option[SchedulerOptions] =
    OParser.parse(parser, args, SchedulerOptions())

}
