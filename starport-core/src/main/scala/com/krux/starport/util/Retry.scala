/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.util

import com.amazonaws.AmazonServiceException

import com.krux.stubborn.policy.ExponentialBackoffAndJitter
import com.krux.stubborn.Retryable


/**
 * Performs retry on throttle
 */
trait Retry extends Retryable with ExponentialBackoffAndJitter {

  override def maxRetry = 10

  override val base = 3 * 1000

  override val cap = 5 * 60 * 1000

  override def shouldRetry = {
    case e: AmazonServiceException if e.getErrorCode().startsWith("Throttling") && e.getStatusCode == 400 => e
  }

}
