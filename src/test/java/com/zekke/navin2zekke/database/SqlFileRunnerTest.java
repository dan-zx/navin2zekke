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

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.zekke.navin2zekke.database.SqlFileRunner.runScriptFromClasspath;
import static com.zekke.navin2zekke.test.config.DatabaseUtil.acquireConnection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

public class SqlFileRunnerTest {

    private static final String TEST_SCRIPT = "/scripts/sql/script.sql";

    @Test(dataProvider = "blankStrings", dataProviderClass = CommonDataProviders.class)
    public void shouldThrowIllegalArgumentExceptionWhenClasspathIsBlank(String classpath) {
        assertThatThrownBy(() -> runScriptFromClasspath(classpath, null)).isInstanceOf(IllegalArgumentException.class).hasNoCause();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenClasspathIsNull() {
        assertThatThrownBy(() -> runScriptFromClasspath(null, null)).isInstanceOf(NullPointerException.class).hasNoCause();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenConnectionIsNull() {
        assertThatThrownBy(() -> runScriptFromClasspath(TEST_SCRIPT, null)).isInstanceOf(NullPointerException.class).hasNoCause();
    }

    @Test
    public void shouldExecuteNothingWhenScriptIsBlank() {
        runScriptFromClasspath("/scripts/sql/blank.sql", acquireConnection());
        // TODO: validate nothing is persited in database
    }

    @Test
    public void shouldParseAndExecuteSqls() {
        runScriptFromClasspath(TEST_SCRIPT, acquireConnection());

        List<String> names = null;
        try (Connection conn = acquireConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT name FROM test_user")) {
                    names = new ArrayList<>();
                    while (rs.next()) {
                        names.add(rs.getString(1));
                    }
                }
            }
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DROP TABLE IF EXISTS test_user");
            }
        } catch (Exception ex) {
            fail("Unexpected exception", ex);
        }

        assertThat(names).isNotNull().isNotEmpty().hasSize(3).containsExactly("John", "Jane", "Dan");
    }
}
