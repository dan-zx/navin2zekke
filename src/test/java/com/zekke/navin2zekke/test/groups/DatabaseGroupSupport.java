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
package com.zekke.navin2zekke.test.groups;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.zekke.navin2zekke.database.DatabaseHelper;
import com.zekke.navin2zekke.test.config.TestDatabaseModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

public class DatabaseGroupSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseGroupSupport.class);
    private static Injector injector;

    private DatabaseHelper databaseHelper;

    public static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

    @BeforeGroups(groups = "database")
    public void beforeDatabaseTests() {
        LOGGER.trace("Wiring dependencies...");
        injector = Guice.createInjector(new TestDatabaseModule());
        databaseHelper = injector.getInstance(DatabaseHelper.class);
        LOGGER.trace("Creating database...");
        databaseHelper.init();
    }

    @AfterGroups(groups = "database")
    public void afterDatabaseTests() {
        LOGGER.trace("Destroying database...");
        databaseHelper.destroy();
    }
}
