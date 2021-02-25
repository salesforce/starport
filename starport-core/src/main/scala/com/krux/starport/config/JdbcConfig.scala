/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.config

import com.typesafe.config.Config
import slick.jdbc.PostgresProfile.api.Database
import slick.jdbc.PostgresProfile.backend.DatabaseDef


case class JdbcConfig(config: Config) {

  def db: DatabaseDef = Database.forConfig("slick", config)

}
