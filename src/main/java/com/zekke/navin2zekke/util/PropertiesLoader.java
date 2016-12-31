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
package com.zekke.navin2zekke.util;

import java.io.InputStream;
import java.util.Properties;

import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonBlank;
import static com.zekke.navin2zekke.util.Messages.getMessage;

/**
 * Properties loader utility.
 *
 * @author Daniel Pedraza-Arcega
 */
public class PropertiesLoader {

    private PropertiesLoader() {
        throw new AssertionError();
    }

    /**
     * Loads properties file from classpath.
     *
     * @param classpath classpath location.
     * @return a properties object.
     */
    public static Properties propertiesFromClasspath(String classpath) {
        requireNonBlank(classpath, "error.arg.blank", "Properties classpath location");
        Properties props;
        try (InputStream stream = PropertiesLoader.class.getResourceAsStream(classpath)) {
            props = new Properties();
            props.load(stream);
            return props;
        } catch (Exception ex) {
            throw new IllegalArgumentException(getMessage("error.file.not_valid", classpath), ex);
        }
    }
}
