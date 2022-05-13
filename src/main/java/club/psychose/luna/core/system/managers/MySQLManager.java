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

package club.psychose.luna.core.system.managers;

import club.psychose.luna.utils.MySQLDatabase;
import club.psychose.luna.utils.logging.CrashLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/*
 * This class contains the methods to establish a valid MySQL database connection and execute statements.
 */

public final class MySQLManager {
    // Initialize the database.
    private final MySQLDatabase mySQLDatabase = new MySQLDatabase();

    // This method creates the MySQL tables.
    public void createTables () {
        // Prepares the statements.
        String serversTable = "CREATE TABLE IF NOT EXISTS servers (ID VARCHAR(18), OWNER_ROLE_ID VARCHAR(18), ADMIN_ROLE_ID VARCHAR(18), MODERATOR_ROLE_ID VARCHAR(18), VERIFICATION_ROLE_ID VARCHAR(18), BOT_INFORMATION_CHANNEL_ID VARCHAR(18), LOGGING_CHANNEL_ID VARCHAR(18), VERIFICATION_CHANNEL_ID VARCHAR(18))";

        try (PreparedStatement serversTablePreparedStatement = Objects.requireNonNull(this.mySQLDatabase.getMySQLConnection()).prepareStatement(serversTable)) {
            // Executes the request.
            serversTablePreparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            CrashLog.saveLogAsCrashLog(sqlException);
        }

        // Closes the MySQL connection.
        try {
            this.mySQLDatabase.closeMySQLConnection();
        } catch (SQLException sqlException) {
            CrashLog.saveLogAsCrashLog(sqlException);
        }
    }

    // This method setups the Hikari configuration.
    public void setupHikariConfig () {
        this.mySQLDatabase.setupHikariConfig();
    }

    // This method returns the MySQL connection.
    public Connection getMySQLConnection () throws SQLException {
        return this.mySQLDatabase.getMySQLConnection();
    }

    // This method closes the MySQL connection.
    public void closeMySQLConnection () throws SQLException {
        this.mySQLDatabase.closeMySQLConnection();
    }
}