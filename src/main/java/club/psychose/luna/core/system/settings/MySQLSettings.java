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

package club.psychose.luna.core.system.settings;

/*
 * This class contains the needed MySQL settings for the MySQL database connection.
 */

public final class MySQLSettings {
    private String mySQLHostname = "127.0.0.1";
    private String mySQLPort = "3306";
    private String mySQLUsername = "root";
    private String mySQLPassword = "root";
    private String mySQLDatabaseName = "Luna";
    private String mySQLJDBCURL = "jdbc:mysql://[HOST]:[PORT]/[DATABASE]?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false";
    private String cachePrepStmts = "true";
    private String prepStmtCacheSize = "250";
    private String prepStmtCacheSqlLimit = "2048";
    private String minimumIdle = "5";
    private String maximumPoolSize = "30";
    private String idleTimeOut = "600000";
    private String leakDetectionThreshold = "30000";
    private String connectionTimeout = "30000";
    private String validationTimeout = "60000";
    private String maxLifetime = "300000";

    // Sets the MySQL settings.
    public void setMySQLHostname (String value) {
        this.mySQLHostname = value;
    }

    public void setMySQLPort (String value) {
        this.mySQLPort = value;
    }

    public void setMySQLUsername (String value) {
        this.mySQLUsername = value;
    }

    public void setMySQLPassword (String value) {
        this.mySQLPassword = value;
    }

    public void setMySQLDatabaseName (String value) {
        this.mySQLDatabaseName = value;
    }

    public void setMySQLJDBCURL (String value) {
        this.mySQLJDBCURL = value;
    }

    public void setCachePrepStmts (String value) {
        this.cachePrepStmts = value;
    }

    public void setPrepStmtCacheSize (String value) {
        this.prepStmtCacheSize = value;
    }

    public void setPrepStmtCacheSqlLimit (String value) {
        this.prepStmtCacheSqlLimit = value;
    }

    public void setMinimumIdle (String value) {
        this.minimumIdle = value;
    }

    public void setMaximumPoolSize (String value) {
        this.maximumPoolSize = value;
    }

    public void setIdleTimeOut (String value) {
        this.idleTimeOut = value;
    }

    public void setLeakDetectionThreshold (String value) {
        this.leakDetectionThreshold = value;
    }

    public void setConnectionTimeout (String value) {
        this.connectionTimeout = value;
    }

    public void setValidationTimeout (String value) {
        this.validationTimeout = value;
    }

    public void setMaxLifetime (String value) {
        this.maxLifetime = value;
    }

    // Returns the MySQL settings.
    public String getMySQLHostname () {
        return this.mySQLHostname;
    }

    public String getMySQLPort () {
        return this.mySQLPort;
    }

    public String getMySQLUsername () {
        return this.mySQLUsername;
    }

    public String getMySQLPassword () {
        return this.mySQLPassword;
    }

    public String getMySQLDatabaseName () {
        return this.mySQLDatabaseName;
    }

    public String getMySQLJDBCURL () {
        return this.mySQLJDBCURL;
    }

    public String isCachePrepStmts () {
        return this.cachePrepStmts;
    }

    public String getPrepStmtCacheSize () {
        return this.prepStmtCacheSize;
    }

    public String getPrepStmtCacheSqlLimit () {
        return this.prepStmtCacheSqlLimit;
    }

    public String getMinimumIdle () {
        return this.minimumIdle;
    }

    public String getMaximumPoolSize () {
        return this.maximumPoolSize;
    }

    public String getIdleTimeOut () {
        return this.idleTimeOut;
    }

    public String getLeakDetectionThreshold () {
        return this.leakDetectionThreshold;
    }

    public String getConnectionTimeout () {
        return this.connectionTimeout;
    }

    public String getValidationTimeout () {
        return this.validationTimeout;
    }

    public String getMaxLifetime () {
        return this.maxLifetime;
    }
}