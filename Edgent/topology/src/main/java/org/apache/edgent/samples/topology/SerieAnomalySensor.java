/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package org.apache.edgent.samples.topology;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.edgent.function.Supplier;

import java.util.Random;

import static java.lang.StrictMath.cos;

/**
 * Every time get() is called, TempSensor generates a temperature reading.
 */
public class SerieAnomalySensor implements Supplier<Pair<Double, Long>> {
    private static final long serialVersionUID = 1L;
    double center = 4.5;
    float i = 5;

    @Override
    public Pair<Double, Long> get() {
        double value = center + cos(i/10);
        i += 1;
        return Pair.of(value, System.currentTimeMillis());
    }
}
