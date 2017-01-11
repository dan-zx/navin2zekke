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
package com.zekke.navin2zekke.database;

import com.zekke.navin2zekke.test.CommonDataProviders;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SqlFileParserTest {

    @Test(dataProvider = "blankStrings", dataProviderClass = CommonDataProviders.class)
    public void shouldThrowIllegalArgumentExceptionWhenClasspathIsBlank(String classpath) {
        assertThatThrownBy(() -> SqlFileParser.newFromClasspath(classpath)).isInstanceOf(IllegalArgumentException.class).hasNoCause();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenClasspathIsNull() {
        assertThatThrownBy(() -> SqlFileParser.newFromClasspath(null)).isInstanceOf(NullPointerException.class).hasNoCause();
    }

    @Test(dataProvider = "filesAndStatements")
    public void shouldParseFile(String fileClasspath, List<String> expectedStatements) {
        List<String> actualStatements = SqlFileParser.newFromClasspath(fileClasspath).getStatements();
        assertThat(actualStatements).isNotNull().hasSameSizeAs(expectedStatements).containsExactlyElementsOf(expectedStatements);
    }

    @DataProvider
    public Object[][] filesAndStatements() {
        return new Object[][]{
                {"scripts/sql/blank.sql", emptyList()},
                {"scripts/sql/script.sql", asList(
                        "DROP TABLE IF EXISTS test_user",
                        "CREATE TABLE test_user ( name VARCHAR(100) NOT NULL )",
                        "INSERT INTO test_user VALUES ('John')",
                        "INSERT INTO test_user VALUES ('Jane')",
                        "INSERT INTO test_user VALUES ('Dan')")}
        };
    }
}
