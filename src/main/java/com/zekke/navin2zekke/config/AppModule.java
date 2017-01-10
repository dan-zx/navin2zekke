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
package com.zekke.navin2zekke.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.zekke.navin2zekke.dao.navin.PathDao;
import com.zekke.navin2zekke.dao.navin.PlaceDao;
import com.zekke.navin2zekke.dao.navin.impl.PathActiveJdbcDao;
import com.zekke.navin2zekke.dao.navin.impl.PlaceActiveJdbcDao;
import com.zekke.navin2zekke.database.DatabaseHelper;
import com.zekke.navin2zekke.service.DomainTranslatorService;
import com.zekke.navin2zekke.service.OutputService;
import com.zekke.navin2zekke.service.impl.DomainTranslatorServiceImpl;
import com.zekke.navin2zekke.service.impl.JsonOutputService;

import java.util.Properties;

import static com.google.inject.name.Names.named;
import static com.zekke.navin2zekke.util.PropertiesLoader.propertiesFromClasspath;

/**
 * Configuration class.
 *
 * @author Daniel Pedraza-Arcega
 */
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Properties.class)
                .annotatedWith(named("jdbcProperties"))
                .toInstance(propertiesFromClasspath(Locations.DATABASE_PROPERTIES));
        bind(String[].class)
                .annotatedWith(named("dbScriptLocations"))
                .toInstance(new String[]{Locations.DATABASE_SCHEMA, Locations.DATABASE_DATA_LOAD});
        bind(PathDao.class)
                .to(PathActiveJdbcDao.class)
                .in(Scopes.SINGLETON);
        bind(PlaceDao.class)
                .to(PlaceActiveJdbcDao.class)
                .in(Scopes.SINGLETON);
        bind(DatabaseHelper.class)
                .in(Scopes.SINGLETON);
        bind(DomainTranslatorService.class)
                .to(DomainTranslatorServiceImpl.class)
                .in(Scopes.SINGLETON);
        bind(OutputService.class)
                .to(JsonOutputService.class)
                .in(Scopes.SINGLETON);
    }

    private static class Locations {
        private static final String DATABASE_PROPERTIES = "/configs/database.properties";
        private static final String DATABASE_SCHEMA = "/scripts/sql/navin_schema.sql";
        private static final String DATABASE_DATA_LOAD = "/scripts/sql/navin_data.sql";

        private Locations() {
            throw new AssertionError();
        }
    }
}
