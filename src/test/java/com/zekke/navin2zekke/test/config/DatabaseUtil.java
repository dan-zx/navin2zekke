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

import com.zekke.navin2zekke.database.DatabaseHelper;
import com.zekke.navin2zekke.util.PropertiesLoader;

import java.sql.Connection;
import java.util.Properties;

public class DatabaseUtil {

    private static final Properties DB_PROPS = PropertiesLoader.propertiesFromClasspath("/configs/database.properties");

    public static Connection acquireConnection() {
        return DatabaseHelper.acquireConnection(
                DB_PROPS.getProperty("jdbc.driver_class_name"),
                DB_PROPS.getProperty("jdbc.url"),
                DB_PROPS.getProperty("jdbc.user"),
                DB_PROPS.getProperty("jdbc.password"));
    }

    public static Properties dbProperties() {
        return DB_PROPS;
    }
}
