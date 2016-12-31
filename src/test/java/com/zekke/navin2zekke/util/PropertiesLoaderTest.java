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

import com.zekke.navin2zekke.test.CommonDataProviders;

import org.testng.annotations.Test;

import java.util.Properties;

import static com.zekke.navin2zekke.util.PropertiesLoader.propertiesFromClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

public class PropertiesLoaderTest {

    @Test(dataProvider = "blankStrings", dataProviderClass = CommonDataProviders.class)
    public void shouldThrowIllegalArgumentExceptionWhenClasspathIsBlank(String classpath) {
        assertThatThrownBy(() -> propertiesFromClasspath(classpath)).isInstanceOf(IllegalArgumentException.class).hasNoCause();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenClasspathIsNull() {
        assertThatThrownBy(() -> propertiesFromClasspath(null)).isInstanceOf(NullPointerException.class).hasNoCause();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenClasspathDoesNotExist() {
        assertThatThrownBy(() -> propertiesFromClasspath("/not_existing_package/any.properties")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldLoadProperties() {
        Properties props = propertiesFromClasspath("/test.properties");
        assertThat(props).isNotNull().containsExactly(entry("a2_prop", "Prop2"), entry("a1_prop", "Prop1"));
    }
}
