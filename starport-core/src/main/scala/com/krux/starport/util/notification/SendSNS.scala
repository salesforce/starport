/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.util.notification

import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sns.model.PublishRequest

object SendSNS {

  def apply(topicArn: String, message: String): String = {
    val client = AmazonSNSClientBuilder.defaultClient()
    val publishRequest = new PublishRequest(topicArn, message)
    client.publish(publishRequest).getMessageId
  }
}
