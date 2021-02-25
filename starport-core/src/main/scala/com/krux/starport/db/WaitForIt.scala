/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.db

import scala.concurrent.{ Await, Awaitable }
import scala.concurrent.duration._

/**
 * Awaiting for future is bad, but sometimes you cannot avoid it
 */
trait WaitForIt {

  val waitTime = 1.minute

  implicit class WaitForAwaitable[T](val awaitable: Awaitable[T]) {

    def waitWhenRead: awaitable.type = Await.ready(awaitable, waitTime)
    def waitForResult: T = Await.result(awaitable, waitTime)

  }

}

object WaitForIt extends WaitForIt
