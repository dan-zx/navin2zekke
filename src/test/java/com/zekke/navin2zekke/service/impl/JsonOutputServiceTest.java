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

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import com.zekke.navin2zekke.domain.zekke.Path;
import com.zekke.navin2zekke.domain.zekke.Waypoint;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;

import static com.zekke.navin2zekke.domain.Coordinates.newLatLng;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

public class JsonOutputServiceTest {

    @Test
    public void shouldWriteJson() {
        JsonOutputService jsonOutputService = new JsonOutputService();
        jsonOutputService.setJsonFilePath("build/output");
        jsonOutputService.setEnableJsonPrettyPrinting(false);
        jsonOutputService.write(testGraph());

        Gson gson = new Gson();
        try {
            JsonElement actualJson = gson.fromJson(new BufferedReader(new FileReader("build/output/zekke_graph.json")), JsonElement.class);
            assertThat(actualJson).isNotNull().extracting(JsonElement::toString).isNotNull().isNotEmpty();
        } catch (Exception ex) {
            fail("Unexpected exception", ex);
        }
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGraphIsNull() {
        JsonOutputService jsonOutputService = new JsonOutputService();
        assertThatThrownBy(() -> jsonOutputService.write(null)).isInstanceOf(NullPointerException.class);
    }

    private Collection<Waypoint> testGraph() {
        Waypoint cdmx = new Waypoint();
        cdmx.setId(1);
        cdmx.setName("CDMX");
        cdmx.setCoordinates(newLatLng(19.389696, -99.118652));
        cdmx.setType(Waypoint.Type.POI);
        Waypoint puebla = new Waypoint();
        puebla.setId(2);
        puebla.setName("Puebla");
        puebla.setCoordinates(newLatLng(19.031775, -98.184814));
        puebla.setType(Waypoint.Type.POI);
        Waypoint veracruz = new Waypoint();
        veracruz.setId(4);
        veracruz.setName("Veracruz");
        veracruz.setCoordinates(newLatLng(19.153357, -98.130371));
        veracruz.setType(Waypoint.Type.POI);

        Path cdmx2puebla = new Path();
        cdmx2puebla.setDistance(120000);
        cdmx2puebla.setFrom(cdmx);
        cdmx2puebla.setTo(puebla);
        Path puebla2cdmx = new Path();
        puebla2cdmx.setDistance(120000);
        puebla2cdmx.setFrom(puebla);
        puebla2cdmx.setTo(cdmx);
        Path puebla2veracruz = new Path();
        puebla2veracruz.setDistance(270000);
        puebla2veracruz.setFrom(puebla);
        puebla2veracruz.setTo(veracruz);
        Path veracruz2puebla = new Path();
        veracruz2puebla.setDistance(270000);
        veracruz2puebla.setFrom(veracruz);
        veracruz2puebla.setTo(puebla);

        cdmx.setPaths(new HashSet<>(asList(cdmx2puebla)));
        puebla.setPaths(new HashSet<>(asList(puebla2cdmx, puebla2veracruz)));
        veracruz.setPaths(new HashSet<>(asList(veracruz2puebla)));

        return asList(cdmx, puebla, veracruz);
    }
}
