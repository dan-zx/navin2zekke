/*
 * Copyright 2016-2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zekke.navin2zekke.domain.navin;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * A connection between two places.
 *
 * @author Daniel Pedraza-Arcega
 */
@Table("path")
public class Path extends Model {

    private static final long serialVersionUID = 6302564466286471659L;

    public Place getPlace1() {
        Integer placeId = getInteger(Columns.PLACE_ID1);
        if (placeId != null) return Place.findById(placeId);
        return null;
    }

    public Place getPlace2() {
        Integer placeId = getInteger(Columns.PLACE_ID2);
        if (placeId != null) return Place.findById(placeId);
        return null;
    }

    public double getDistance() {
        return getDouble(Columns.DISTANCE);
    }

    /** Path table columns */
    private static class Columns {

        private static final String PLACE_ID1 = "place_id1";
        private static final String PLACE_ID2 = "place_id2";
        private static final String DISTANCE = "distance";

        private Columns() {
            throw new AssertionError();
        }
    }
}
