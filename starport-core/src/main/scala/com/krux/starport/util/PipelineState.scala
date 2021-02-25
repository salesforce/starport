/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.util

object PipelineState extends Enumeration {
  type State = Value
  /** Alas, AWS never publishes the exhaustive list of states.
    * The states here are learned from our past errors and AWS documentation at
    * http://docs.aws.amazon.com/datapipeline/latest/DeveloperGuide/dp-pipeline-status.html
   */
  val ACTIVATING, CANCELED, CASCADE_FAILED, DEACTIVATING, DELETING, FAILED, FINISHED, INACTIVE, PAUSED, PENDING,
    RUNNING, SCHEDULING, SCHEDULED, SHUTTING_DOWN, SKIPPED, TIMEDOUT, VALIDATING,
    WAITING_FOR_RUNNER, WAITING_ON_DEPENDENCIES = Value
}


