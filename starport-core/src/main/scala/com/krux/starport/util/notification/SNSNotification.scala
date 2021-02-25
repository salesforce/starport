/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.util.notification

import com.krux.starport.config.StarportSettings
import com.krux.starport.db.record.Pipeline

object SNSNotification extends Notification {
  override def send(summary: String, message: String, pipeline: Pipeline)(implicit conf: StarportSettings): String = {
    val snsTopicARN = pipeline.owner.getOrElse(conf.snsTopicARN)

    SendSNS(
      snsTopicARN,
      s"$summary - $message"
    )
  }
}
