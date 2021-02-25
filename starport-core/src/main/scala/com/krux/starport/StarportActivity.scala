/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport

import slick.jdbc.PostgresProfile.api._

import com.krux.starport.config.StarportSettings
import com.krux.starport.db.table.ScheduledPipelines
import com.krux.starport.db.WaitForIt
import com.krux.starport.util.DateTimeFunctions


trait StarportActivity extends WaitForIt with Logging with DateTimeFunctions {

  // use -Dconf.resource=application.dev.conf for testing
  implicit val conf = StarportSettings()

  def db = conf.jdbc.db
  val parallel = conf.parallel

  def inConsoleManagedPipelineIds(): Set[String] = {
    val pipelineIds = db.run(
      ScheduledPipelines()
        .filter(_.inConsole)
        .distinctOn(_.awsId)
        .result
    ).waitForResult.map(_.awsId).toSet
    logger.info(s"Retrieved ${pipelineIds.size} in-console managed pipelines from Starport DB.")
    pipelineIds
  }

}
