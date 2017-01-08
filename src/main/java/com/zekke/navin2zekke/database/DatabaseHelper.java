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
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.inject.Named;

import static com.zekke.navin2zekke.util.MessageBundleValidations.requireNonNull;

/**
 * Helps in database setup and destruction.
 *
 * @author Daniel Pedraza-Arcega
 */
public class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final Set<String> LOADED_DRIVERS = new HashSet<>();

    private final String driverClassName;
    private final String connectionUrl;
    private final String databaseUser;
    private final String databasePassword;

    private Optional<String[]> initScripts = Optional.empty();

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
        databasePassword = connectionProperties.getProperty("jdbc.password");
        requireNonNull(databasePassword, "error.arg.null", "JDBC Password");
    }

    /**
     * Acquires a database connection using the provided parameters. The connection is set to not
     * autocommitable.
     *
     * @param driverClassName a valid driver class name.
     * @param url a valid connection url.
     * @param user a valid database user.
     * @param password a valid user password.
     * @return a database connection.
     */
    public static Connection acquireConnection(String driverClassName, String url, String user, String password) {
        loadDriverIfNecessary(driverClassName);
        try {
            LOGGER.trace("Open connection {} user={}", url, user);
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            throw new DatabaseException.Builder()
                    .messageKey("error.database.not_connected")
                    .messageArgs(url)
                    .cause(ex)
                    .build();
        }
    }

    private static void loadDriverIfNecessary(String driverClassName) {
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

    /**
     * Closes the provided connection.
     *
     * @param connection an open connection.
     */
    static void close(Connection connection) {
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

    /**
     * Commits the current transaction.
     *
     * @param connection an open connection.
     */
    static void commit(Connection connection) {
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

    /**
     * Rollbacks the current transaction.
     *
     * @param connection an open connection.
     */
    static void rollback(Connection connection) {
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

    /** Initializes connections. */
    public void setup() {
        openActiveJdbc();
        initDatabase();
    }

    /** Closes connections. */
    public void close() {
        closeActiveJdbc();
    }

    /** @return an initialized connection with the properties set in this object. */
    public Connection acquireConnection() {
        return acquireConnection(driverClassName, connectionUrl, databaseUser, databasePassword);
    }

    /**
     * Sets script classpath location to initialize the database with them.
     *
     * @param scripts an array of classpath locations.
     */
    public @Inject(optional = true) void setInitScripts(@Named("dbScriptLocations") String[] scripts) {
        initScripts = Optional.ofNullable(scripts);
    }

    private void openActiveJdbc() {
        LOGGER.trace("Open ActiveJDBC");
        Base.open(driverClassName, connectionUrl, databaseUser, databasePassword);
    }

    private void initDatabase() {
        initScripts.ifPresent(scripts -> {
            LOGGER.trace("Running init scripts...");
            for (String script : scripts) {
                runScript(script);
            }
        });
    }

    private void runScript(String scriptLocation) {
        Connection connection = acquireConnection();
        SqlFileRunner.runScriptFromClasspath(scriptLocation, connection);
    }

    private void closeActiveJdbc() {
        LOGGER.trace("Close ActiveJDBC");
        Base.close();
    }
}
