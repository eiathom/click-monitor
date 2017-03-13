/**
Copyright (C) 2017 eiathom. 
All rights reserved. Copyright (C) 2017 eiathom.
Permission is hereby granted, free of charge, 
to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, 
and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:
The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
 */
package com.eiathom.app.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eiathom.app.exception.DatabaseConnectionException;

/**
 * Data source connection to H2 database
 * 
 * @author eiathom
 *
 */
public final class DataSourceConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConnection.class);

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "sa";
    private static final DataSourceConnection INSTANCE = new DataSourceConnection();
    private Connection databaseConnection;

    private DataSourceConnection() {}

    public static DataSourceConnection getInstance() {
        return INSTANCE;
    }

    public Connection getDatabaseConnection() {
        LOGGER.info("getting a connection to the database");
        try {
            if (isConnectionActive()) {
                return databaseConnection;
            }
            return getConnection();
        } catch (final SQLException | DatabaseConnectionException exception) {
            throw new DatabaseConnectionException("unable to access the database", exception);
        }
    }

    public void checkDatabaseConnectionStatus() {
        try {
            if (isConnectionActive()) {
                LOGGER.info("database connection: {}, is active", databaseConnection);
            } else {
                LOGGER.warn("database connection: {}, is not active", databaseConnection);
            }
        } catch (final SQLException exception) {
            LOGGER.error("unable to check the database connection status", exception);
        }
    }

    private Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            databaseConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            LOGGER.info("accessing the database: '{}'", databaseConnection.getMetaData().getDriverName());
            return databaseConnection;
        } catch (final ClassNotFoundException | SQLException exception) {
            LOGGER.error("unable to load database driver class", exception);
            throw new DatabaseConnectionException("unable to access the database", exception);
        }
    }

    private boolean isConnectionActive() throws SQLException {
        return databaseConnection != null && !databaseConnection.isClosed();
    }

}
