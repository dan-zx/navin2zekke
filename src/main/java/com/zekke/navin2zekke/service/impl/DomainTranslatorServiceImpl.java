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
package com.zekke.navin2zekke.service.impl;

import com.zekke.navin2zekke.dao.navin.PathDao;
import com.zekke.navin2zekke.dao.navin.PlaceDao;
import com.zekke.navin2zekke.domain.navin.Place;
import com.zekke.navin2zekke.domain.zekke.Path;
import com.zekke.navin2zekke.domain.zekke.Waypoint;
import com.zekke.navin2zekke.service.DomainTranslatorService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import static java.util.Collections.unmodifiableSet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Translates app domains.
 *
 * @author Daniel Pedraza-Arcega
 */
public class DomainTranslatorServiceImpl implements DomainTranslatorService {

    private final PlaceDao placeDao;
    private final PathDao pathDao;

    /**
     * Constructor.
     *
     * @param placeDao Navin Place DAO.
     * @param pathDao Navin Path DAO.
     */
    public @Inject DomainTranslatorServiceImpl(PlaceDao placeDao, PathDao pathDao) {
        this.placeDao = placeDao;
        this.pathDao = pathDao;
    }

    /** {@inheritDoc} */
    @Override
    public Set<Waypoint> translateNavinToZekke() {
        List<Place> places = placeDao.findAll();
        Map<Integer, Waypoint> waypointMap = places.stream().map(place -> {
            Waypoint poi = new Waypoint();
            poi.setId((int)place.getId());
            poi.setType(Waypoint.Type.POI);
            poi.setName(place.getName());
            poi.setCoordinates(place.getCoordinates());
            return poi;
        }).collect(toMap(Waypoint::getId, identity()));
        waypointMap.entrySet().forEach(waypointEntry -> {
            waypointEntry.getValue().setPaths(
                    pathDao.findByPlace1Id(waypointEntry.getKey()).stream().map(path -> {
                        Path zekkePath = new Path();
                        zekkePath.setFrom(waypointEntry.getValue());
                        zekkePath.setTo(waypointMap.get(path.getPlace2().getId()));
                        zekkePath.setDistance(path.getDistance());
                        return zekkePath;
                    }).collect(toSet())
            );
        });
        return unmodifiableSet(new HashSet<>(waypointMap.values()));
    }
}
