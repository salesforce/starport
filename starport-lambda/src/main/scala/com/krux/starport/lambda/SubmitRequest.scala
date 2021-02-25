/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.lambda

import scala.beans.BeanProperty

class SubmitRequest(@BeanProperty var args: Array[String]) {
  def this() = this(Array[String]())
}
