/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.krux.starport.metric;

import java.io.Serializable;

import com.codahale.metrics.Gauge;


public class ConstantValueGauge<T> implements Gauge<T>, Serializable {

    private T value;

    public ConstantValueGauge(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }
}
