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
package com.zekke.navin2zekke.test.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

import com.zekke.navin2zekke.dao.navin.PathDao;
import com.zekke.navin2zekke.dao.navin.PlaceDao;
import com.zekke.navin2zekke.dao.navin.impl.PathActiveJdbcDao;
import com.zekke.navin2zekke.dao.navin.impl.PlaceActiveJdbcDao;
import com.zekke.navin2zekke.database.DatabaseHelper;

import java.util.Properties;

public class TestDatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Properties.class)
                .annotatedWith(Names.named("jdbcProperties"))
                .toInstance(testProperties());
        bind(String[].class)
                .annotatedWith(Names.named("dbScriptLocations"))
                .toInstance(new String[]{"/scripts/sql/init.sql"});
        bind(PathDao.class)
                .to(PathActiveJdbcDao.class)
                .in(Scopes.SINGLETON);
        bind(PlaceDao.class)
                .to(PlaceActiveJdbcDao.class)
                .in(Scopes.SINGLETON);
        bind(DatabaseHelper.class)
                .in(Scopes.SINGLETON);
    }

    private Properties testProperties() {
        Properties props = new Properties();
        props.setProperty("jdbc.driver_class_name", "org.h2.Driver");
        props.setProperty("jdbc.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        props.setProperty("jdbc.user", "root");
        props.setProperty("jdbc.password", "");
        return props;
    }
}
