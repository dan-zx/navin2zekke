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
package com.zekke.navin2zekke;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.zekke.navin2zekke.config.AppModule;
import com.zekke.navin2zekke.database.DatabaseHelper;
import com.zekke.navin2zekke.domain.zekke.Waypoint;
import com.zekke.navin2zekke.service.DomainTranslatorService;
import com.zekke.navin2zekke.service.OutputService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Application starter.
 *
 * @author Daniel Pedraza-Arcega
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        try {
            LOGGER.info("Wiring dependencies...");
            Injector injector = Guice.createInjector(new AppModule());
            DatabaseHelper databaseHelper = injector.getInstance(DatabaseHelper.class);
            DomainTranslatorService domainTranslatorService = injector.getInstance(DomainTranslatorService.class);
            OutputService outputService = injector.getInstance(OutputService.class);

            LOGGER.info("Connecting and initializing database...");
            databaseHelper.init();

            LOGGER.info("Translating Navin domain to ZeKKe domain...");
            Set<Waypoint> waypoints = domainTranslatorService.translateNavinToZekke();
            SortedSet<Waypoint> sortedWaypoints = new TreeSet<>((w1, w2) -> Integer.compare(w1.getId(), w2.getId()));
            sortedWaypoints.addAll(waypoints);

            LOGGER.info("Writing translation...");
            outputService.write(waypoints);

            LOGGER.info("Closing and destroying database...");
            databaseHelper.destroy();

            LOGGER.info("Done");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error in the application", ex);
        }
    }
}
