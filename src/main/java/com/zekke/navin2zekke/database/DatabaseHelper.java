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

import com.google.inject.Inject;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.inject.Named;

import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonNull;

/**
 * Helps in database initialization and destruction.
 *
 * @author Daniel Pedraza-Arcega
 */
public class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final Set<String> LOADED_DRIVERS = new HashSet<>();

    private final String driverClassName;
    private final String connectionUrl;
    private final String databaseUser;
    private final String databaseUserPassword;

    private Optional<String[]> initScripts = Optional.empty();
    private Optional<String[]> destroyScripts = Optional.empty();

    /**
     * Constructor.
     *
     * @param connectionProperties Properties object with the following propeties set:
     * jdbc.driver_class_name, jdbc.url, jdbc.user, jdbc.password
     */
    public @Inject DatabaseHelper(@Named("jdbcProperties") Properties connectionProperties) {
        driverClassName = connectionProperties.getProperty("jdbc.driver_class_name");
        requireNonNull(driverClassName, "error.arg.null", "JDBC Driver");
        connectionUrl = connectionProperties.getProperty("jdbc.url");
        requireNonNull(connectionUrl, "error.arg.null", "JDBC URL");
        databaseUser = connectionProperties.getProperty("jdbc.user");
        requireNonNull(databaseUser, "error.arg.null", "JDBC User");
        databaseUserPassword = connectionProperties.getProperty("jdbc.password");
        requireNonNull(databaseUserPassword, "error.arg.null", "JDBC User Password");
    }

    /** Initializes connections and, optionally, initializes the database. */
    public void init() {
        openActiveJdbc();
        initDatabase();
    }

    /** Closes connections and, optionally, destroys the database. */
    public void destroy() {
        closeActiveJdbc();
        destroyDatabase();
    }

    /**
     * Sets script classpath location to initialize the database with them.
     *
     * @param scripts an array of classpath locations.
     */
    public @Inject(optional = true) void setInitScripts(@Named("dbInitScriptLocations") String[] scripts) {
        initScripts = Optional.ofNullable(scripts);
    }

    /**
     * Sets script classpath location to destroy the database with them.
     *
     * @param scripts an array of classpath locations.
     */
    public @Inject(optional = true) void setDestroyScripts(@Named("dbDestroyScriptLocations") String[] scripts) {
        destroyScripts = Optional.ofNullable(scripts);
    }

    private void openActiveJdbc() {
        LOGGER.trace("Open ActiveJDBC");
        Base.open(driverClassName, connectionUrl, databaseUser, databaseUserPassword);
    }

    private void initDatabase() {
        initScripts.ifPresent(scripts -> {
            LOGGER.trace("Running init scripts...");
            for (String script : scripts) {
                runScript(script);
            }
        });
    }

    private void destroyDatabase() {
        destroyScripts.ifPresent(scripts -> {
            LOGGER.trace("Running destroy scripts...");
            for (String script : scripts) {
                runScript(script);
            }
        });
    }

    private void runScript(String scriptLocation) {
        LOGGER.debug("Parsing {} ...", scriptLocation);
        List<String> statements = SqlFileParser.newFromClasspath(scriptLocation).getStatements();
        Connection connection = acquireConnection();
        try {
            LOGGER.debug("Running {} ...", scriptLocation);
            try (Statement stmt = connection.createStatement()) {
                for (String statement : statements) {
                    LOGGER.debug(statement);
                    stmt.addBatch(statement);
                }
                stmt.executeBatch();
                commit(connection);
            }
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

    private Connection acquireConnection() {
        loadDriverIfNecessary(driverClassName);
        try {
            LOGGER.trace("Open connection {} user={}", connectionUrl, databaseUser);
            Connection connection = DriverManager.getConnection(connectionUrl, databaseUser, databaseUserPassword);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            throw new DatabaseException.Builder()
                    .messageKey("error.database.not_connected")
                    .messageArgs(connectionUrl)
                    .cause(ex)
                    .build();
        }
    }

    private void loadDriverIfNecessary(String driverClassName) {
        if (!LOADED_DRIVERS.contains(driverClassName)) {
            try {
                LOGGER.trace("Load driver {}", driverClassName);
                Class.forName(driverClassName);
                LOADED_DRIVERS.add(driverClassName);
            } catch (ClassNotFoundException ex) {
                throw new DatabaseException.Builder()
                        .messageKey("error.database.driver_not_loaded")
                        .messageArgs(driverClassName)
                        .cause(ex)
                        .build();
            }
        }
    }

    private void commit(Connection connection) {
        try {
            LOGGER.trace("Commit transaction");
            connection.commit();
        } catch (SQLException ex) {
            throw new DatabaseException.Builder()
                    .messageKey("error.database.commit_tx")
                    .cause(ex)
                    .build();
        }
    }

    private void rollback(Connection connection) {
        try {
            LOGGER.trace("Rollback transaction");
            connection.rollback();
        } catch (SQLException ex) {
            throw new DatabaseException.Builder()
                    .messageKey("error.database.abort_tx")
                    .cause(ex)
                    .build();
        }
    }

    private void close(Connection connection) {
        try {
            LOGGER.trace("Close connection");
            connection.close();
        } catch (SQLException ex) {
            throw new DatabaseException.Builder()
                    .messageKey("error.database.close_connection")
                    .cause(ex)
                    .build();
        }
    }

    private void closeActiveJdbc() {
        LOGGER.trace("Close ActiveJDBC");
        Base.close();
    }
}
