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

import com.zekke.navin2zekke.domain.Coordinates;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.List;

/**
 * A point of interest in the map.
 *
 * @author Daniel Pedraza-Arcega
 */
@Table("place")
public class Place extends Model {

    private static final long serialVersionUID = 168444589297557197L;

    public String getName() {
        return getString(Columns.NAME);
    }

    public Coordinates getCoordinates() {
        Double lat = getDouble(Columns.LATITUDE);
        if (lat == null) return null;
        Double lng = getDouble(Columns.LONGITUDE);
        if (lng == null) return null;
        return Coordinates.newLatLng(lat, lng);
    }

    public List<Path> getPaths() {
        return getAll(Path.class);
    }

    /** Place table columns */
    private static class Columns {

        private static final String NAME = "name";
        private static final String LATITUDE = "latitude";
        private static final String LONGITUDE = "longitude";

        private Columns() {
            throw new AssertionError();
        }
    }
}
