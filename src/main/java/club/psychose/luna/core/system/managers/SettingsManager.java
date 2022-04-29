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

import club.psychose.luna.Luna;
import club.psychose.luna.core.system.settings.*;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidConfigurationDataException;
import club.psychose.luna.utils.Constants;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public final class SettingsManager {
    private final BotSettings botSettings = new BotSettings();
    private final FilterSettings filterSettings = new FilterSettings();
    private final MySQLSettings mySQLSettings = new MySQLSettings();
    private final ServerSettings serverSettings = new ServerSettings();

    private int retryBotSettings = 0;
    private int retryMySQLSettings = 0;

    public void loadSettings () {
        this.loadBotSettings();
        this.loadMySQLSettings();
        this.loadFilterSettings();
    }

    public void loadBotSettings () {
        if (this.retryBotSettings <= 1) {
            if (Files.exists(Constants.getLunaFolderPath("\\settings\\bot_settings.json"))) {
                JsonObject botSettingsJsonObject = Luna.FILE_MANAGER.readJsonObject(Constants.getLunaFolderPath("\\settings\\bot_settings.json"));

                if (botSettingsJsonObject != null) {
                    if ((botSettingsJsonObject.has("Bot Token") && (botSettingsJsonObject.has("YouTube API Key")) && (botSettingsJsonObject.has("Message Filter URL") && (botSettingsJsonObject.has("Message Whitelist Filter URL"))))) {
                        this.getBotSettings().setBotToken(botSettingsJsonObject.get("Bot Token").getAsString());
                        this.getBotSettings().setYoutubeAPIKey(botSettingsJsonObject.get("YouTube API Key").getAsString());
                        this.getBotSettings().setMessageFilterURL(botSettingsJsonObject.get("Message Filter URL").getAsString());
                        this.getBotSettings().setMessageWhitelistFilterURL(botSettingsJsonObject.get("Message Whitelist Filter URL").getAsString());
                    } else {
                        CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Bot settings are invalid!"), null);
                        System.exit(1);
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Bot settings are invalid!"), null);
                    System.exit(1);
                }
            } else {
                this.retryBotSettings ++;
                this.saveSettings();
                this.loadBotSettings();
            }
        } else {
            CrashLog.saveLogAsCrashLog(new IOException("Bot settings cannot be created!"), null);
            System.exit(1);
        }
    }

    public void loadMySQLSettings () {
        if (this.retryMySQLSettings <= 1) {
            if (Files.exists(Constants.getLunaFolderPath("\\settings\\mysql_settings.json"))) {
                JsonObject mySQLSettingsJsonObject = Luna.FILE_MANAGER.readJsonObject(Constants.getLunaFolderPath("\\settings\\mysql_settings.json"));

                if (mySQLSettingsJsonObject != null) {
                    if (mySQLSettingsJsonObject.has("MySQL Hostname")) {
                        this.getMySQLSettings().setMySQLHostname(mySQLSettingsJsonObject.get("MySQL Hostname").getAsString());

                        if (mySQLSettingsJsonObject.has("MySQL Port")) {
                            this.getMySQLSettings().setMySQLPort(mySQLSettingsJsonObject.get("MySQL Port").getAsString());

                            if (mySQLSettingsJsonObject.has("MySQL Username")) {
                                this.getMySQLSettings().setMySQLUsername(mySQLSettingsJsonObject.get("MySQL Username").getAsString());

                                if (mySQLSettingsJsonObject.has("MySQL Password")) {
                                    this.getMySQLSettings().setMySQLPassword(mySQLSettingsJsonObject.get("MySQL Password").getAsString());

                                    if (mySQLSettingsJsonObject.has("MySQL JDBC URL")) {
                                        this.getMySQLSettings().setMySQLJDBCURL(mySQLSettingsJsonObject.get("MySQL JDBC URL").getAsString());

                                        if (mySQLSettingsJsonObject.has("MySQL Cache Prepared Statement")) {
                                            this.getMySQLSettings().setCachePrepStmts(mySQLSettingsJsonObject.get("MySQL Cache Prepared Statement").getAsString());

                                            if (mySQLSettingsJsonObject.has("MySQL Prepared Statement Cache Size")) {
                                                this.getMySQLSettings().setPrepStmtCacheSize(mySQLSettingsJsonObject.get("MySQL Prepared Statement Cache Size").getAsString());

                                                if (mySQLSettingsJsonObject.has("MySQL Prepared Statement Cache SQL Limit")) {
                                                    this.getMySQLSettings().setPrepStmtCacheSqlLimit(mySQLSettingsJsonObject.get("MySQL Prepared Statement Cache SQL Limit").getAsString());

                                                    if (mySQLSettingsJsonObject.has("MySQL Minimum Idle")) {
                                                        this.getMySQLSettings().setMinimumIdle(mySQLSettingsJsonObject.get("MySQL Minimum Idle").getAsString());

                                                        if (mySQLSettingsJsonObject.has("MySQL Maximum Pool Size")) {
                                                            this.getMySQLSettings().setMaximumPoolSize(mySQLSettingsJsonObject.get("MySQL Maximum Pool Size").getAsString());

                                                            if (mySQLSettingsJsonObject.has("MySQL Idle Timeout")) {
                                                                this.getMySQLSettings().setIdleTimeOut(mySQLSettingsJsonObject.get("MySQL Idle Timeout").getAsString());

                                                                if (mySQLSettingsJsonObject.has("MySQL Leak Detection Threshold")) {
                                                                    this.getMySQLSettings().setLeakDetectionThreshold(mySQLSettingsJsonObject.get("MySQL Leak Detection Threshold").getAsString());

                                                                    if (mySQLSettingsJsonObject.has("MySQL Connection Timeout")) {
                                                                        this.getMySQLSettings().setConnectionTimeout(mySQLSettingsJsonObject.get("MySQL Connection Timeout").getAsString());

                                                                        if (mySQLSettingsJsonObject.has("MySQL Validation Timeout")) {
                                                                            this.getMySQLSettings().setValidationTimeout(mySQLSettingsJsonObject.get("MySQL Validation Timeout").getAsString());

                                                                            if (mySQLSettingsJsonObject.has("MySQL Max Lifetime")) {
                                                                                this.getMySQLSettings().setMaxLifetime(mySQLSettingsJsonObject.get("MySQL Max Lifetime").getAsString());

                                                                                Luna.MY_SQL_MANAGER.setupHikariConfig();
                                                                                Luna.MY_SQL_MANAGER.createTables();
                                                                                this.loadServerSettings();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                this.retryMySQLSettings ++;
                this.saveMySQLSettings();
                this.loadMySQLSettings();
            }
        } else {
            CrashLog.saveLogAsCrashLog(new IOException("MySQL settings cannot be created!"), null);
            System.exit(1);
        }
    }

    public void loadServerSettings () {
        String mySQLStatement = "SELECT * FROM servers";

        try (PreparedStatement preparedStatement = Luna.MY_SQL_MANAGER.getMySQLConnection().prepareStatement(mySQLStatement)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String serverID = resultSet.getString("ID");
                String ownerRoleID = resultSet.getString("OWNER_ROLE_ID");
                String adminRoleID = resultSet.getString("ADMIN_ROLE_ID");
                String moderatorRoleID = resultSet.getString("MODERATOR_ROLE_ID");
                String verificationRoleID = resultSet.getString("VERIFICATION_ROLE_ID");
                String botInformationChannelID = resultSet.getString("BOT_INFORMATION_CHANNEL_ID");
                String loggingChannelID = resultSet.getString("LOGGING_CHANNEL_ID");
                String verificationChannelID = resultSet.getString("VERIFICATION_CHANNEL_ID");

                ServerSetting serverSetting = new ServerSetting(ownerRoleID, adminRoleID, moderatorRoleID, verificationRoleID, botInformationChannelID, loggingChannelID, verificationChannelID);
                this.getServerSettings().addServerConfiguration(serverID, serverSetting);
            }

            resultSet.close();
            preparedStatement.close();
            Luna.MY_SQL_MANAGER.closeMySQLConnection();
        } catch (SQLException sqlException) {
            CrashLog.saveLogAsCrashLog(sqlException, null);
            System.exit(1);
        }
    }

    public void updateWhitelist () {
        try {
            URL url = new URL(this.getBotSettings().getMessageWhitelistFilterURL());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

            bufferedReader.lines().filter(line -> !(this.getFilterSettings().getWhitelistedWords().contains(line))).forEachOrdered(line -> this.getFilterSettings().getWhitelistedWords().add(line));
            bufferedReader.close();
        } catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException, null);
        }
    }

    public void loadFilterSettings () {
        this.updateWhitelist();
        this.updateBlacklist();
        this.updateFilters();
    }

    public void updateBlacklist () {
        try {
            URL url = new URL(this.getBotSettings().getMessageFilterURL());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

            this.getFilterSettings().getBlacklistedWords().clear();
            bufferedReader.lines().filter(line -> !(this.getFilterSettings().getBlacklistedWords().contains(line))).forEachOrdered(line -> this.getFilterSettings().getBlacklistedWords().add(line));
            bufferedReader.close();

            if (this.getFilterSettings().getBlacklistedWords().size() > 3) {
                this.getFilterSettings().getBlacklistedWords().remove(0);
                this.getFilterSettings().getBlacklistedWords().remove(0);
                this.getFilterSettings().getBlacklistedWords().remove(0);
            } else {
                this.getFilterSettings().getBlacklistedWords().clear();
            }
        } catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException, null);
        }
    }

    public void updateFilters () {
        JsonObject filterJsonObject = Luna.FILE_MANAGER.readJsonObject(Constants.getLunaFolderPath("\\settings\\filter.json"));

        if (filterJsonObject != null) {
            filterJsonObject.entrySet().stream().filter(wordFilterMapEntry -> !(this.getFilterSettings().getBypassDetectionHashMap().containsKey(wordFilterMapEntry.getKey()))).forEachOrdered(wordFilterMapEntry -> this.getFilterSettings().getBypassDetectionHashMap().put(wordFilterMapEntry.getKey(), wordFilterMapEntry.getValue().getAsString()));
        } else {
            CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Filter cannot resolved!"), null);
        }
    }

    public void saveSettings () {
        this.saveBotSettings();
        this.saveMySQLSettings();
        this.saveServerSettings();
    }

    public void saveBotSettings () {
        JsonObject botSettingsJsonObject = new JsonObject();

        botSettingsJsonObject.addProperty("Bot Token", this.getBotSettings().getBotToken());
        botSettingsJsonObject.addProperty("YouTube API Key", this.getBotSettings().getYoutubeAPIKey());
        botSettingsJsonObject.addProperty("Message Filter URL", this.getBotSettings().getMessageFilterURL());
        botSettingsJsonObject.addProperty("Message Whitelist Filter URL", this.getBotSettings().getMessageWhitelistFilterURL());

        Luna.FILE_MANAGER.saveJsonObject(Constants.getLunaFolderPath("\\settings\\bot_settings.json"), botSettingsJsonObject);
    }

    public void saveMySQLSettings () {
        JsonObject mySQLJsonObject = new JsonObject();

        mySQLJsonObject.addProperty("MySQL Hostname", this.getMySQLSettings().getMySQLHostname());
        mySQLJsonObject.addProperty("MySQL Port", this.getMySQLSettings().getMySQLPort());
        mySQLJsonObject.addProperty("MySQL Username", this.getMySQLSettings().getMySQLUsername());
        mySQLJsonObject.addProperty("MySQL Password", this.getMySQLSettings().getMySQLPassword());
        mySQLJsonObject.addProperty("MySQL Database Name", this.getMySQLSettings().getMySQLDatabaseName());
        mySQLJsonObject.addProperty("MySQL JDBC URL", this.getMySQLSettings().getMySQLJDBCURL());
        mySQLJsonObject.addProperty("MySQL Cache Prepared Statement", this.getMySQLSettings().isCachePrepStmts());
        mySQLJsonObject.addProperty("MySQL Prepared Statement Cache Size", this.getMySQLSettings().getPrepStmtCacheSize());
        mySQLJsonObject.addProperty("MySQL Prepared Statement Cache SQL Limit", this.getMySQLSettings().getPrepStmtCacheSqlLimit());
        mySQLJsonObject.addProperty("MySQL Minimum Idle", this.getMySQLSettings().getMinimumIdle());
        mySQLJsonObject.addProperty("MySQL Maximum Pool Size", this.getMySQLSettings().getMaximumPoolSize());
        mySQLJsonObject.addProperty("MySQL Idle Timeout", this.getMySQLSettings().getIdleTimeOut());
        mySQLJsonObject.addProperty("MySQL Leak Detection Threshold", this.getMySQLSettings().getLeakDetectionThreshold());
        mySQLJsonObject.addProperty("MySQL Connection Timeout", this.getMySQLSettings().getConnectionTimeout());
        mySQLJsonObject.addProperty("MySQL Validation Timeout", this.getMySQLSettings().getValidationTimeout());
        mySQLJsonObject.addProperty("MySQL Max Lifetime", this.getMySQLSettings().getMaxLifetime());

        Luna.FILE_MANAGER.saveJsonObject(Constants.getLunaFolderPath("\\settings\\mysql_settings.json"), mySQLJsonObject);
    }

    public void saveServerSettings () {
        String mySQLStatement = "SELECT * FROM servers";

        ArrayList<String> serverIDExistsArrayList = new ArrayList<>();
        ArrayList<String> savedServerSettingsArrayList = new ArrayList<>();
        try (PreparedStatement preparedStatement = Luna.MY_SQL_MANAGER.getMySQLConnection().prepareStatement(mySQLStatement)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String serverID = resultSet.getString("ID");

                ServerSetting serverSetting = this.getServerSettings().getServerConfigurationHashMap().getOrDefault(serverID, null);

                if (serverSetting != null) {
                    if (!(serverIDExistsArrayList.contains(serverID)))
                        serverIDExistsArrayList.add(serverID);

                    String ownerRoleID = resultSet.getString("OWNER_ROLE_ID");
                    String adminRoleID = resultSet.getString("ADMIN_ROLE_ID");
                    String moderatorRoleID = resultSet.getString("MODERATOR_ROLE_ID");
                    String verificationRoleID = resultSet.getString("VERIFICATION_ROLE_ID");
                    String botInformationChannelID = resultSet.getString("BOT_INFORMATION_CHANNEL_ID");
                    String loggingChannelID = resultSet.getString("LOGGING_CHANNEL_ID");
                    String verificationChannelID = resultSet.getString("VERIFICATION_CHANNEL_ID");

                    if ((serverSetting.getOwnerRoleID().equals(ownerRoleID)) && (serverSetting.getAdminRoleID().equals(adminRoleID)) && (serverSetting.getModeratorRoleID().equals(moderatorRoleID)) && (serverSetting.getVerificationRoleID().equals(verificationRoleID)) && (serverSetting.getBotInfoChannelID().equals(botInformationChannelID)) && (serverSetting.getLoggingChannelID().equals(loggingChannelID)) && (serverSetting.getVerificationChannelID().equals(verificationChannelID))) {
                        if (!(savedServerSettingsArrayList.contains(serverID)))
                            savedServerSettingsArrayList.add(serverID);
                    }
                }
            }

            resultSet.close();
            preparedStatement.close();
            Luna.MY_SQL_MANAGER.closeMySQLConnection();
        } catch (SQLException sqlException) {
            CrashLog.saveLogAsCrashLog(sqlException, null);
        }

        for (Map.Entry<String, ServerSetting> serverSettingMapEntry : this.getServerSettings().getServerConfigurationHashMap().entrySet()) {
            String serverID = serverSettingMapEntry.getKey();
            ServerSetting serverSetting = serverSettingMapEntry.getValue();

            if (!(savedServerSettingsArrayList.contains(serverID))) {
                boolean updateEntry = false;
                if (!(serverIDExistsArrayList.contains(serverID))) {
                    mySQLStatement = "INSERT INTO servers (ID, OWNER_ROLE_ID, ADMIN_ROLE_ID, MODERATOR_ROLE_ID, VERIFICATION_ROLE_ID, BOT_INFORMATION_CHANNEL_ID, LOGGING_CHANNEL_ID, VERIFICATION_CHANNEL_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                } else {
                    updateEntry = true;
                    mySQLStatement = "UPDATE servers SET OWNER_ROLE_ID = ?, ADMIN_ROLE_ID = ?, MODERATOR_ROLE_ID = ?, VERIFICATION_ROLE_ID = ?, BOT_INFORMATION_CHANNEL_ID = ?, LOGGING_CHANNEL_ID = ?, VERIFICATION_CHANNEL_ID = ? WHERE ID = ?";
                }

                try (PreparedStatement preparedStatement = Luna.MY_SQL_MANAGER.getMySQLConnection().prepareStatement(mySQLStatement)) {
                    preparedStatement.setString(!(updateEntry) ? 0 : 8, serverID);
                    preparedStatement.setString(1, serverSetting.getOwnerRoleID());
                    preparedStatement.setString(2, serverSetting.getAdminRoleID());
                    preparedStatement.setString(3, serverSetting.getModeratorRoleID());
                    preparedStatement.setString(4, serverSetting.getVerificationRoleID());
                    preparedStatement.setString(5, serverSetting.getBotInfoChannelID());
                    preparedStatement.setString(6, serverSetting.getLoggingChannelID());
                    preparedStatement.setString(7, serverSetting.getVerificationChannelID());

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    Luna.MY_SQL_MANAGER.closeMySQLConnection();
                } catch (SQLException sqlException) {
                    CrashLog.saveLogAsCrashLog(sqlException, null);
                }
            }
        }
    }

    public BotSettings getBotSettings () {
        return this.botSettings;
    }

    public FilterSettings getFilterSettings () {
        return this.filterSettings;
    }

    public MySQLSettings getMySQLSettings () {
        return this.mySQLSettings;
    }

    public ServerSettings getServerSettings () {
        return this.serverSettings;
    }
}