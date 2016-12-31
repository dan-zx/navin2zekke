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

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.zekke.navin2zekke.test.config.DatabaseUtil.acquireConnection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class SqlFileRunnerTest {

    @Test
    public void shouldParseAndExecuteSqls() {
        SqlFileRunner.runScriptFromClasspath("/scripts/sql/script.sql", acquireConnection());

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
        } catch (Exception ex) {
            fail("Unexpected exception", ex);
        }

        assertThat(names).isNotNull().isNotEmpty().hasSize(3).containsExactly("John", "Jane", "Dan");
    }
}
