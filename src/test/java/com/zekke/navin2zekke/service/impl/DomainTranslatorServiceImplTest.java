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

import com.zekke.navin2zekke.domain.zekke.Path;
import com.zekke.navin2zekke.domain.zekke.Waypoint;
import com.zekke.navin2zekke.test.groups.ServiceGroupSupport;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static com.zekke.navin2zekke.domain.Coordinates.newLatLng;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DomainTranslatorServiceImplTest {

    @Test(groups = "service")
    public void shouldTranslateNavinToZekke() {
        DomainTranslatorServiceImpl domainTranslator = ServiceGroupSupport.getInstance(DomainTranslatorServiceImpl.class);
        Set<Waypoint> graph = domainTranslator.translateNavinToZekke();
        assertThat(graph).isNotNull().isNotEmpty().containsOnlyElementsOf(expectedGraph());
    }

    private Set<Waypoint> expectedGraph() {
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
        Waypoint pachuca = new Waypoint();
        pachuca.setId(3);
        pachuca.setName("Pachuca");
        pachuca.setCoordinates(newLatLng(20.095030, -98.750610));
        pachuca.setType(Waypoint.Type.POI);
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
        Path puebla2pachuca = new Path();
        puebla2pachuca.setDistance(150000);
        puebla2pachuca.setFrom(puebla);
        puebla2pachuca.setTo(pachuca);
        Path pachuca2puebla = new Path();
        pachuca2puebla.setDistance(150000);
        pachuca2puebla.setFrom(pachuca);
        pachuca2puebla.setTo(puebla);
        Path cdmx2pachuca = new Path();
        cdmx2pachuca.setDistance(110000);
        cdmx2pachuca.setFrom(cdmx);
        cdmx2pachuca.setTo(pachuca);
        Path pachuca2cdmx = new Path();
        pachuca2cdmx.setDistance(110000);
        pachuca2cdmx.setFrom(pachuca);
        pachuca2cdmx.setTo(cdmx);
        Path puebla2veracruz = new Path();
        puebla2veracruz.setDistance(270000);
        puebla2veracruz.setFrom(puebla);
        puebla2veracruz.setTo(veracruz);
        Path veracruz2puebla = new Path();
        veracruz2puebla.setDistance(270000);
        veracruz2puebla.setFrom(veracruz);
        veracruz2puebla.setTo(puebla);

        cdmx.setPaths(new HashSet<>(asList(cdmx2pachuca, cdmx2puebla)));
        puebla.setPaths(new HashSet<>(asList(puebla2cdmx, puebla2pachuca, puebla2veracruz)));
        pachuca.setPaths(new HashSet<>(asList(pachuca2cdmx, pachuca2puebla)));
        veracruz.setPaths(new HashSet<>(asList(veracruz2puebla)));

        return new HashSet<>(asList(cdmx, puebla, pachuca, veracruz));
    }
}
