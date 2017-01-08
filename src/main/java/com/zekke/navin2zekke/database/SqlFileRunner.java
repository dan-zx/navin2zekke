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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.zekke.navin2zekke.database.DatabaseHelper.close;
import static com.zekke.navin2zekke.database.DatabaseHelper.commit;
import static com.zekke.navin2zekke.database.DatabaseHelper.rollback;
import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonBlank;
import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonNull;
import static com.zekke.navin2zekke.util.Messages.getMessage;
import static com.zekke.navin2zekke.util.Strings.BLANK_SPACE;
import static com.zekke.navin2zekke.util.Strings.EMPTY;
import static com.zekke.navin2zekke.util.Strings.isBlank;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * Utility class for executing *.sql files.
 *
 * @author Daniel Pedraza-Arcega
 */
public class SqlFileRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlFileRunner.class);
    private static final String SQL_DELIMITER = ";";

    /**
     * Executes the statements contained in the given file.
     *
     * @param classpath the classpath of the file.
     * @param connection an open connection to a database. The connection will be close when this
     * method is finished.
     */
    public static void runScriptFromClasspath(String classpath, Connection connection) {
        requireNonBlank(classpath, "error.arg.blank", "Classpath file");
        requireNonNull(connection, "error.arg.null", "JDBC Connection");
        List<String> statements = getSqlStatementsFromClasspath(classpath);
        LOGGER.debug("Running {} ...", classpath);
        try (Statement stmt = connection.createStatement()) {
            for (String statement : statements) {
                LOGGER.debug(statement);
                stmt.addBatch(statement);
            }
            stmt.executeBatch();
            commit(connection);
        } catch (SQLException ex) {
            rollback(connection);
            throw new DatabaseException.Builder()
                    .messageKey("error.database.stmt")
                    .cause(ex)
                    .build();
        } finally {
            close(connection);
        }
    }

    private static List<String> getSqlStatementsFromClasspath(String classpath) {
        String rawContent;
        try {
            Path path = Paths.get(SqlFileRunner.class.getResource(classpath).toURI());
            rawContent = new String(Files.readAllBytes(path));
        } catch (Exception ex) {
            throw new IllegalArgumentException(getMessage("error.file.not_valid", classpath), ex);
        }
        if (isBlank(rawContent)) return emptyList();
        rawContent = Patterns.SINGLE_LINE_COMMENT.matcher(rawContent).replaceAll(EMPTY);
        rawContent = Patterns.MULTI_LINE_COMMENT.matcher(rawContent).replaceAll(EMPTY);
        rawContent = Patterns.LINE_BRAKES.matcher(rawContent).replaceAll(BLANK_SPACE);
        rawContent = Patterns.WHITE_SPACES.matcher(rawContent).replaceAll(BLANK_SPACE);
        String[] rawStatements = rawContent.split(SQL_DELIMITER);
        List<String> statements = new ArrayList<>(rawStatements.length);
        for (String statement : rawStatements) {
            statement = statement.trim();
            if (!isBlank(statement)) statements.add(statement);
        }
        return unmodifiableList(statements);
    }

    private static class Patterns {
        private static final Pattern MULTI_LINE_COMMENT = Pattern.compile("\\/\\*([^*]|[\\r\\n]|(\\*+([^*\\/]|[\\r\\n])))*\\*\\/", Pattern.MULTILINE);
        private static final Pattern SINGLE_LINE_COMMENT = Pattern.compile("--.+$", Pattern.MULTILINE);
        private static final Pattern LINE_BRAKES = Pattern.compile("\\r?\\n|\\r", Pattern.MULTILINE);
        private static final Pattern WHITE_SPACES = Pattern.compile("\\s+", Pattern.MULTILINE);
    }
}
