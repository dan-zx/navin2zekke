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
package com.zekke.navin2zekke.dao.navin.impl;

import com.zekke.navin2zekke.domain.navin.Place;
import com.zekke.navin2zekke.test.groups.DatabaseGroupSupport;

import org.testng.annotations.Test;

import java.util.List;

import static com.zekke.navin2zekke.domain.Coordinates.newLatLng;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class PlaceActiveJdbcDaoTest {

    @Test(groups = "database")
    public void shouldFindAll() {
        PlaceActiveJdbcDao placeDao = DatabaseGroupSupport.getInstance(PlaceActiveJdbcDao.class);
        List<Place> places = placeDao.findAll();
        assertThat(places).isNotNull().isNotEmpty().hasSize(4).extracting(
                Place::getId, Place::getName, Place::getCoordinates)
                .containsOnly(
                        tuple(1, "CDMX", newLatLng(19.389696, -99.118652)),
                        tuple(2, "Puebla", newLatLng(19.031775, -98.184814)),
                        tuple(3, "Pachuca", newLatLng(20.095030, -98.750610)),
                        tuple(4, "Veracruz", newLatLng(19.153357, -98.130371)));
    }
}
