/*
 * Copyright © 2022 psychose.club
 * Discord: https://www.psychose.club/discord
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.psychose.luna.utils;

import club.psychose.luna.Luna;
import club.psychose.luna.utils.logging.ConsoleLogger;
import club.psychose.luna.utils.logging.CrashLog;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/*
 * This class contains the methods to establish a valid MySQL database connection.
 */

public final class MySQLDatabase {
    private HikariDataSource hikariDataSource;

    // Public constructor.
    public MySQLDatabase () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException classNotFoundException) {
            CrashLog.saveLogAsCrashLog(classNotFoundException, null);
        }
    }

    // This method setups the Hikari config.
    public void setupHikariConfig () {
        HikariConfig hikariConfig = new HikariConfig();

        // Sets the JDBC URL.
        hikariConfig.setJdbcUrl(Luna.SETTINGS_MANAGER.getMySQLSettings().getMySQLJDBCURL().replace("[HOST]", Luna.SETTINGS_MANAGER.getMySQLSettings().getMySQLHostname()).replace("[PORT]", Luna.SETTINGS_MANAGER.getMySQLSettings().getMySQLPort()).replace("[DATABASE]", Luna.SETTINGS_MANAGER.getMySQLSettings().getMySQLDatabaseName()).trim());

        // Sets the username.
        hikariConfig.setUsername(Luna.SETTINGS_MANAGER.getMySQLSettings().getMySQLUsername());

        // Sets the password.
        hikariConfig.setPassword(Luna.SETTINGS_MANAGER.getMySQLSettings().getMySQLPassword());

        // Sets the properties.
        hikariConfig.addDataSourceProperty("cachePrepStmts", Luna.SETTINGS_MANAGER.getMySQLSettings().isCachePrepStmts());
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", Luna.SETTINGS_MANAGER.getMySQLSettings().getPrepStmtCacheSize());
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", Luna.SETTINGS_MANAGER.getMySQLSettings().getPrepStmtCacheSqlLimit());
        hikariConfig.setMinimumIdle(Integer.parseInt(Luna.SETTINGS_MANAGER.getMySQLSettings().getMinimumIdle()));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(Luna.SETTINGS_MANAGER.getMySQLSettings().getMaximumPoolSize()));
        hikariConfig.setIdleTimeout(Integer.parseInt(Luna.SETTINGS_MANAGER.getMySQLSettings().getIdleTimeOut()));
        hikariConfig.setLeakDetectionThreshold(Integer.parseInt(Luna.SETTINGS_MANAGER.getMySQLSettings().getLeakDetectionThreshold()));
        hikariConfig.setConnectionTimeout(Integer.parseInt(Luna.SETTINGS_MANAGER.getMySQLSettings().getConnectionTimeout()));
        hikariConfig.setValidationTimeout(Integer.parseInt(Luna.SETTINGS_MANAGER.getMySQLSettings().getValidationTimeout()));
        hikariConfig.setMaxLifetime(Integer.parseInt(Luna.SETTINGS_MANAGER.getMySQLSettings().getMaxLifetime()));

        // Overwrites the old hikari data source with a new one.
        this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    // This method returns the MySQL connection.
    public Connection getMySQLConnection () throws SQLException {
        // Checks if the data source is not null!
        if (this.hikariDataSource != null) {

            // Checks if the datasource is closed.
            // If it's closed it'll set the hikari config.
            if (this.hikariDataSource.isClosed())
                this.setupHikariConfig();

            // Returns the connection.
            return this.hikariDataSource.getConnection();
        } else {
            // Sets the hikari config.
            this.setupHikariConfig();

            // Checks if the datasource is not null.
            // If it's not null it'll return the connection.
            if (this.hikariDataSource != null)
                return this.hikariDataSource.getConnection();

            // Debug stuff.
            ConsoleLogger.debug("ERROR! HikariDataSource is null!");

            // Returns null.
            return null;
        }
    }

    // This method closes the MySQL connection and Hikari DataSource.
    public void closeMySQLConnection () throws SQLException {
        // Checks if the datasource is not null.
        if (((this.hikariDataSource != null)) && (this.getMySQLConnection() != null)) {
            // Checks if the MySQL connection is not closed.
            // If it's not closed it'll close it.
            if (!(this.getMySQLConnection().isClosed()))
                this.getMySQLConnection().close();

            // Checks if the datasource is not closed.
            // If it's not closed it'll close it. (Prevents connection leaks)
            if (!(this.hikariDataSource.isClosed()))
                this.hikariDataSource.close();
        }
    }
}