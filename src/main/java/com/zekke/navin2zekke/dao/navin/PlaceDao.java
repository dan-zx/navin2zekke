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
package com.zekke.navin2zekke.dao.navin;

import com.zekke.navin2zekke.domain.navin.Place;

import java.util.List;

/**
 * CRUD data store operations related to Place objects.
 *
 * @author Daniel Pedraza-Arcega
 */
public interface PlaceDao {

    /**
     * Finds all places in the datastore.
     *
     * @return a list of places.
     */
    List<Place> findAll();
}
