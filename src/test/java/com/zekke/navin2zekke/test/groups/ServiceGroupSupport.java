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
import com.zekke.navin2zekke.database.SqlFileRunner;
import com.zekke.navin2zekke.test.config.TestDatabaseModule;
import com.zekke.navin2zekke.test.config.TestServiceModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

public class ServiceGroupSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceGroupSupport.class);
    private static Injector injector;

    private DatabaseHelper databaseHelper;

    public static <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

    @BeforeGroups(groups = "service")
    public void beforeServiceTests() {
        LOGGER.trace("Initializing dependencies and creating database...");
        injector = Guice.createInjector(new TestDatabaseModule(), new TestServiceModule());
        databaseHelper = injector.getInstance(DatabaseHelper.class);
        databaseHelper.setup();
    }

    @AfterGroups(groups = "service")
    public void afterServiceTests() {
        LOGGER.trace("Destroying database...");
        SqlFileRunner.runScriptFromClasspath("/scripts/sql/destroy.sql", databaseHelper.acquireConnection());
        databaseHelper.close();
    }
}