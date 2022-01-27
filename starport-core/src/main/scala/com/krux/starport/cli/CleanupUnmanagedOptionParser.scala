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
import com.krux.starport.util.PipelineState

object CleanupUnmanagedOptionParser extends Reads {

  val name = "cleanup-unmanaged-pipelines"

  var parser = {
    val builder = OParser.builder[CleanupUnmanagedOptions]
    import builder._
    OParser.sequence(
      programName(name),
      head(name, BuildInfo.version),
      help("help").text("prints this usage text"),
      opt[String]("excludePrefix")
        .valueName("<excludePrefix>")
        .action((x, c) => c.copy(excludePrefixes = c.excludePrefixes :+ x))
        .unbounded()
        .validate(x =>
          if (x.trim.nonEmpty) success
          else failure("Value <excludePrefix> must not be empty")
        ),
      opt[PipelineState.State]("pipelineState")
        .valueName("<pipelineState>")
        .action((x, c) => c.copy(pipelineState = x)),
      opt[LocalDateTime]("cutoffDate")
        .valueName("<cutoffDate> default value is 2 months before")
        .action((x, c) => c.copy(cutoffDate = x)),
      opt[Unit]("dryRun")
        .valueName("<dryRun>")
        .action((_, c) => c.copy(dryRun = true)),
      opt[Unit]("force")
        .valueName("<force> will ignore if the pipeline is managed by starport or not")
        .action((_, c) => c.copy(force = true))
    )
  }

  def parse(args: Array[String]): Option[CleanupUnmanagedOptions] =
    OParser.parse(parser, args, CleanupUnmanagedOptions())

}
