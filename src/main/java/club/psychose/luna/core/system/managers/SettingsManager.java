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
import club.psychose.luna.utils.JsonUtils;
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
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 * This class manages the settings from the application.
 */

public final class SettingsManager {
    // Initializing the settings.
    private final BotSettings botSettings = new BotSettings();
    private final MessageFilterSettings messageFilterSettings = new MessageFilterSettings();
    private final MySQLSettings mySQLSettings = new MySQLSettings();
    private final ServerSettings serverSettings = new ServerSettings();

    private int retryBotSettings = 0;
    private int retryMessageFilterSettings = 0;
    private int retryMySQLSettings = 0;

    // This method loads all settings.
    public void loadSettings () {
        this.loadBotSettings();
        this.loadMessageFilterSettings();
        this.loadMySQLSettings();
    }

    // This method loads the bot settings.
    public void loadBotSettings () {
        if (this.retryBotSettings <= 1) {
            if (Files.exists(Constants.getLunaFolderPath("\\settings\\bot_settings.json"))) {
                JsonObject botSettingsJsonObject = Luna.FILE_MANAGER.readJsonObject(Constants.getLunaFolderPath("\\settings\\bot_settings.json"));

                if (botSettingsJsonObject != null) {
                    if ((botSettingsJsonObject.has("Bot Token") && (botSettingsJsonObject.has("YouTube API Key")) && (botSettingsJsonObject.has("Bot Owner ID")) && (botSettingsJsonObject.has("Syntax Prefix")) && (botSettingsJsonObject.has("Time Period")) && (botSettingsJsonObject.has("Time Unit")))) {
                        this.getBotSettings().setBotToken(botSettingsJsonObject.get("Bot Token").getAsString());
                        this.getBotSettings().setYoutubeAPIKey(botSettingsJsonObject.get("YouTube API Key").getAsString());
                        this.getBotSettings().setBotOwnerID(botSettingsJsonObject.get("Bot Owner ID").getAsString());
                        this.getBotSettings().setPrefix(botSettingsJsonObject.get("Syntax Prefix").getAsString());

                        int timePeriod = 10;
                        try {
                            timePeriod = botSettingsJsonObject.get("Time Period").getAsInt();
                        } catch (NumberFormatException ignored) {}

                        this.getBotSettings().setTimePeriod(timePeriod);

                        String timeUnitString = botSettingsJsonObject.get("Time Unit").getAsString();
                        TimeUnit timeUnit = Arrays.stream(TimeUnit.values()).filter(validTimeUnit -> validTimeUnit.name().equalsIgnoreCase(timeUnitString)).findFirst().orElse(TimeUnit.MINUTES);

                        this.getBotSettings().setTimeUnit(timeUnit);
                    } else {
                        CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Bot settings are invalid!"));
                        System.exit(1);
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Bot settings are invalid!"));
                    System.exit(1);
                }
            } else {
                this.retryBotSettings ++;
                this.saveBotSettings();
                this.loadBotSettings();
            }
        } else {
            CrashLog.saveLogAsCrashLog(new IOException("Bot settings cannot be created!"));
            System.exit(1);
        }
    }

    // This method loads the message filter settings.
    public void loadMessageFilterSettings () {
        if (this.retryMessageFilterSettings <= 1) {
            if (Files.exists(Constants.getLunaFolderPath("\\settings\\message_filter_settings.json"))) {
                JsonObject messageFilterSettingsJsonObject = Luna.FILE_MANAGER.readJsonObject(Constants.getLunaFolderPath("\\settings\\message_filter_settings.json"));

                if (messageFilterSettingsJsonObject != null) {
                    if ((messageFilterSettingsJsonObject.has("Enable Blacklist")) && (messageFilterSettingsJsonObject.has("Custom Filters")) && (messageFilterSettingsJsonObject.has("Custom Blacklist URL")) && (messageFilterSettingsJsonObject.has("Custom Whitelist URL")) && (messageFilterSettingsJsonObject.has("Custom Character Filter URL")) && (messageFilterSettingsJsonObject.has("Fallback to default"))) {
                        this.getMessageFilterSettings().setEnableBlacklist(messageFilterSettingsJsonObject.get("Enable Blacklist").getAsBoolean());
                        this.getMessageFilterSettings().setCustomFiltersEnabled(messageFilterSettingsJsonObject.get("Custom Filters").getAsBoolean());
                        this.getMessageFilterSettings().setCustomBlackListURL(messageFilterSettingsJsonObject.get("Custom Blacklist URL").getAsString());
                        this.getMessageFilterSettings().setCustomWhitelistURL(messageFilterSettingsJsonObject.get("Custom Whitelist URL").getAsString());
                        this.getMessageFilterSettings().setCustomCharacterFilterURL(messageFilterSettingsJsonObject.get("Custom Character Filter URL").getAsString());
                        this.getMessageFilterSettings().setFallbackToDefault(messageFilterSettingsJsonObject.get("Fallback to default").getAsBoolean());
                    } else {
                        CrashLog.saveLogAsCrashLog(new IOException("Message filter settings are invalid!"));
                        System.exit(1);
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new IOException("Message filter settings are invalid!"));
                    System.exit(1);
                }
            } else {
                this.retryMessageFilterSettings ++;
                this.saveMessageFilterSettings();
                this.loadMessageFilterSettings();
            }
        } else {
            CrashLog.saveLogAsCrashLog(new IOException("Message filter settings cannot be created!"));
            System.exit(1);
        }
    }

    // This method loads the MySQL settings.
    public void loadMySQLSettings () {
        if (this.retryMySQLSettings <= 1) {
            if (Files.exists(Constants.getLunaFolderPath("\\settings\\mysql_settings.json"))) {
                JsonObject mySQLSettingsJsonObject = Luna.FILE_MANAGER.readJsonObject(Constants.getLunaFolderPath("\\settings\\mysql_settings.json"));

                if (mySQLSettingsJsonObject != null) {
                    if ((mySQLSettingsJsonObject.has("MySQL Hostname")) && (mySQLSettingsJsonObject.has("MySQL Port")) && (mySQLSettingsJsonObject.has("MySQL Username")) && (mySQLSettingsJsonObject.has("MySQL Password")) && (mySQLSettingsJsonObject.has("MySQL Database Name")) && (mySQLSettingsJsonObject.has("MySQL JDBC URL")) && (mySQLSettingsJsonObject.has("MySQL Cache Prepared Statement")) && (mySQLSettingsJsonObject.has("MySQL Prepared Statement Cache Size")) && (mySQLSettingsJsonObject.has("MySQL Prepared Statement Cache SQL Limit")) && (mySQLSettingsJsonObject.has("MySQL Minimum Idle")) && (mySQLSettingsJsonObject.has("MySQL Maximum Pool Size")) && (mySQLSettingsJsonObject.has("MySQL Idle Timeout")) && (mySQLSettingsJsonObject.has("MySQL Leak Detection Threshold")) && (mySQLSettingsJsonObject.has("MySQL Connection Timeout")) && (mySQLSettingsJsonObject.has("MySQL Validation Timeout")) && (mySQLSettingsJsonObject.has("MySQL Max Lifetime"))) {
                        this.getMySQLSettings().setMySQLHostname(mySQLSettingsJsonObject.get("MySQL Hostname").getAsString());
                        this.getMySQLSettings().setMySQLPort(mySQLSettingsJsonObject.get("MySQL Port").getAsString());
                        this.getMySQLSettings().setMySQLUsername(mySQLSettingsJsonObject.get("MySQL Username").getAsString());
                        this.getMySQLSettings().setMySQLPassword(mySQLSettingsJsonObject.get("MySQL Password").getAsString());
                        this.getMySQLSettings().setMySQLDatabaseName(mySQLSettingsJsonObject.get("MySQL Database Name").getAsString());
                        this.getMySQLSettings().setMySQLJDBCURL(mySQLSettingsJsonObject.get("MySQL JDBC URL").getAsString());
                        this.getMySQLSettings().setCachePrepStmts(mySQLSettingsJsonObject.get("MySQL Cache Prepared Statement").getAsString());
                        this.getMySQLSettings().setPrepStmtCacheSize(mySQLSettingsJsonObject.get("MySQL Prepared Statement Cache Size").getAsString());
                        this.getMySQLSettings().setPrepStmtCacheSqlLimit(mySQLSettingsJsonObject.get("MySQL Prepared Statement Cache SQL Limit").getAsString());
                        this.getMySQLSettings().setMinimumIdle(mySQLSettingsJsonObject.get("MySQL Minimum Idle").getAsString());
                        this.getMySQLSettings().setMaximumPoolSize(mySQLSettingsJsonObject.get("MySQL Maximum Pool Size").getAsString());
                        this.getMySQLSettings().setIdleTimeOut(mySQLSettingsJsonObject.get("MySQL Idle Timeout").getAsString());
                        this.getMySQLSettings().setLeakDetectionThreshold(mySQLSettingsJsonObject.get("MySQL Leak Detection Threshold").getAsString());
                        this.getMySQLSettings().setConnectionTimeout(mySQLSettingsJsonObject.get("MySQL Connection Timeout").getAsString());
                        this.getMySQLSettings().setValidationTimeout(mySQLSettingsJsonObject.get("MySQL Validation Timeout").getAsString());
                        this.getMySQLSettings().setMaxLifetime(mySQLSettingsJsonObject.get("MySQL Max Lifetime").getAsString());

                        Luna.MY_SQL_MANAGER.setupHikariConfig();
                        Luna.MY_SQL_MANAGER.createTables();
                        this.loadServerSettings();
                    } else {
                        CrashLog.saveLogAsCrashLog(new IOException("MySQL settings are invalid!"));
                        System.exit(1);
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new IOException("MySQL settings are invalid!"));
                    System.exit(1);
                }
            } else {
                this.retryMySQLSettings ++;
                this.saveMySQLSettings();
                this.loadMySQLSettings();
            }
        } else {
            CrashLog.saveLogAsCrashLog(new IOException("MySQL settings cannot be created!"));
            System.exit(1);
        }
    }

    // This method loads the server settings.
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
            CrashLog.saveLogAsCrashLog(sqlException);
            System.exit(1);
        }
    }

    // This method loads the filter settings.
    public void loadFilterSettings () {
        this.updateWhitelist();
        this.updateBlacklist();
        this.updateFilters();
    }

    // This method updates the whitelist.
    public void updateWhitelist () {
        if (this.getMessageFilterSettings().isBlacklistEnabled()) {
            boolean fallback = false;
            String whitelistURL = this.getMessageFilterSettings().isCustomFiltersEnabled() ? this.getMessageFilterSettings().getCustomWhitelistURL() : Constants.FALLBACK_WHITELIST_URL;

            while (true) {
                if (fallback)
                    whitelistURL = Constants.FALLBACK_WHITELIST_URL;

                if (!(this.getMessageFilterSettings().isCustomFiltersEnabled()))
                    fallback = true;

                try {
                    URL url = new URL(whitelistURL);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

                    bufferedReader.lines().filter(line -> !(this.getMessageFilterSettings().getWhitelistedWords().contains(line))).forEachOrdered(line -> this.getMessageFilterSettings().getWhitelistedWords().add(line));
                    bufferedReader.close();
                    return;
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException);

                    if (fallback)
                        return;

                    fallback = true;
                }
            }
        }
    }

    // This method updates the blacklist.
    public void updateBlacklist () {
        if (this.getMessageFilterSettings().isBlacklistEnabled()) {
            boolean fallback = false;
            String blacklistURL = this.getMessageFilterSettings().isCustomFiltersEnabled() ? this.getMessageFilterSettings().getCustomBlackListURL() : Constants.FALLBACK_BLACKLIST_URL;

            while (true) {
                if (fallback)
                    blacklistURL = Constants.FALLBACK_BLACKLIST_URL;

                if (!(this.getMessageFilterSettings().isCustomFiltersEnabled()))
                    fallback = true;

                try {
                    URL url = new URL(blacklistURL);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));

                    bufferedReader.lines().filter(line -> !(this.getMessageFilterSettings().getBlacklistedWords().contains(line))).forEachOrdered(line -> this.getMessageFilterSettings().getBlacklistedWords().add(line));
                    bufferedReader.close();
                    return;
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException);

                    if (fallback)
                        return;

                    fallback = true;
                }
            }
        }
    }

    // This method updates the filters.
    public void updateFilters () {
        if (this.getMessageFilterSettings().isBlacklistEnabled()) {
            boolean fallback = false;
            String characterFilterURL = this.getMessageFilterSettings().isCustomFiltersEnabled() ? this.getMessageFilterSettings().getCustomCharacterFilterURL() : Constants.FALLBACK_CHARACTER_FILTER_URL;

            while (true) {
                if (fallback)
                    characterFilterURL = Constants.FALLBACK_CHARACTER_FILTER_URL;

                if (!(this.getMessageFilterSettings().isCustomFiltersEnabled()))
                    fallback = true;

                try {
                    JsonObject characterFilterJsonObject = JsonUtils.fetchOnlineJsonObject(characterFilterURL);

                    if (characterFilterJsonObject != null) {
                        characterFilterJsonObject.entrySet().stream().filter(wordFilterMapEntry -> !(this.getMessageFilterSettings().getCharacterFilterHashMap().containsKey(wordFilterMapEntry.getKey()))).forEachOrdered(wordFilterMapEntry -> this.getMessageFilterSettings().getCharacterFilterHashMap().put(wordFilterMapEntry.getKey(), wordFilterMapEntry.getValue().getAsString()));
                        return;
                    } else {
                        CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Character filter resolving failed!"));

                        if (fallback)
                            return;

                        fallback = true;
                    }
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException);

                    if (fallback)
                        return;

                    fallback = true;
                }
            }
        }
    }

    // This method saves the bot settings.
    public void saveBotSettings () {
        JsonObject botSettingsJsonObject = new JsonObject();

        botSettingsJsonObject.addProperty("Config", "1.0.0");
        botSettingsJsonObject.addProperty("Bot Token", this.getBotSettings().getBotToken());
        botSettingsJsonObject.addProperty("YouTube API Key", this.getBotSettings().getYoutubeAPIKey());
        botSettingsJsonObject.addProperty("Bot Owner ID", this.getBotSettings().getBotOwnerID());
        botSettingsJsonObject.addProperty("Syntax Prefix", this.getBotSettings().getPrefix());
        botSettingsJsonObject.addProperty("Time Period", this.getBotSettings().getTimePeriod());
        botSettingsJsonObject.addProperty("Time Unit", this.getBotSettings().getTimeUnit().name());

        Luna.FILE_MANAGER.saveJsonObject(Constants.getLunaFolderPath("\\settings\\bot_settings.json"), botSettingsJsonObject);
    }

    // This method saves the message filter settings.
    public void saveMessageFilterSettings () {
        JsonObject messageFilterSettingsJsonObject = new JsonObject();

        messageFilterSettingsJsonObject.addProperty("Config", "1.0.0");
        messageFilterSettingsJsonObject.addProperty("Enable Blacklist", this.getMessageFilterSettings().isBlacklistEnabled());
        messageFilterSettingsJsonObject.addProperty("Custom Filters", this.getMessageFilterSettings().isCustomFiltersEnabled());
        messageFilterSettingsJsonObject.addProperty("Custom Blacklist URL", this.getMessageFilterSettings().getCustomBlackListURL());
        messageFilterSettingsJsonObject.addProperty("Custom Whitelist URL", this.getMessageFilterSettings().getCustomWhitelistURL());
        messageFilterSettingsJsonObject.addProperty("Custom Character Filter URL", this.getMessageFilterSettings().getCustomCharacterFilterURL());
        messageFilterSettingsJsonObject.addProperty("Fallback to default", this.getMessageFilterSettings().isFallbackToDefault());

        Luna.FILE_MANAGER.saveJsonObject(Constants.getLunaFolderPath("\\settings\\message_filter_settings.json"), messageFilterSettingsJsonObject);
    }

    // This method saves the MySQL settings.
    public void saveMySQLSettings () {
        JsonObject mySQLJsonObject = new JsonObject();

        mySQLJsonObject.addProperty("Config", "1.0.0");
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

    // This method saves the server settings.
    public void saveServerSettings () {
        String mySQLStatement = "SELECT * FROM servers";

        ArrayList<String> serverIDExistsArrayList = new ArrayList<>();
        ArrayList<String> savedServerSettingsArrayList = new ArrayList<>();
        ArrayList<String> deleteServerSettingsArrayList = new ArrayList<>();
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

                    if ((serverSetting.getOwnerRoleID().equals(ownerRoleID)) && (serverSetting.getAdminRoleID().equals(adminRoleID)) && (serverSetting.getModeratorRoleID().equals(moderatorRoleID)) && (serverSetting.getVerificationRoleID().equals(verificationRoleID)) && (serverSetting.getBotInformationChannelID().equals(botInformationChannelID)) && (serverSetting.getLoggingChannelID().equals(loggingChannelID)) && (serverSetting.getVerificationChannelID().equals(verificationChannelID))) {
                        if (!(savedServerSettingsArrayList.contains(serverID)))
                            savedServerSettingsArrayList.add(serverID);
                    }
                } else {
                    if (!(deleteServerSettingsArrayList.contains(serverID)))
                        deleteServerSettingsArrayList.add(serverID);
                }
            }

            resultSet.close();
            preparedStatement.close();
            Luna.MY_SQL_MANAGER.closeMySQLConnection();
        } catch (SQLException sqlException) {
            CrashLog.saveLogAsCrashLog(sqlException);
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
                    if (!(updateEntry)) {
                        preparedStatement.setString(1, serverID);
                        preparedStatement.setString(2, serverSetting.getOwnerRoleID());
                        preparedStatement.setString(3, serverSetting.getAdminRoleID());
                        preparedStatement.setString(4, serverSetting.getModeratorRoleID());
                        preparedStatement.setString(5, serverSetting.getVerificationRoleID());
                        preparedStatement.setString(6, serverSetting.getBotInformationChannelID());
                        preparedStatement.setString(7, serverSetting.getLoggingChannelID());
                        preparedStatement.setString(8, serverSetting.getVerificationChannelID());
                    } else {
                        preparedStatement.setString(1, serverSetting.getOwnerRoleID());
                        preparedStatement.setString(2, serverSetting.getAdminRoleID());
                        preparedStatement.setString(3, serverSetting.getModeratorRoleID());
                        preparedStatement.setString(4, serverSetting.getVerificationRoleID());
                        preparedStatement.setString(5, serverSetting.getBotInformationChannelID());
                        preparedStatement.setString(6, serverSetting.getLoggingChannelID());
                        preparedStatement.setString(7, serverSetting.getVerificationChannelID());
                        preparedStatement.setString(8, serverID);
                    }

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    Luna.MY_SQL_MANAGER.closeMySQLConnection();
                } catch (SQLException sqlException) {
                    CrashLog.saveLogAsCrashLog(sqlException);
                }
            }
        }

        for (String serverID : deleteServerSettingsArrayList) {
            mySQLStatement = "DELETE FROM servers WHERE ID = ?";

            try (PreparedStatement preparedStatement = Luna.MY_SQL_MANAGER.getMySQLConnection().prepareStatement(mySQLStatement)) {
                preparedStatement.setString(1, serverID);

                preparedStatement.executeUpdate();
                preparedStatement.close();
                Luna.MY_SQL_MANAGER.closeMySQLConnection();
            } catch (SQLException sqlException) {
                CrashLog.saveLogAsCrashLog(sqlException);
            }
        }
    }

    // Returns the settings.
    public BotSettings getBotSettings () {
        return this.botSettings;
    }

    public MessageFilterSettings getMessageFilterSettings () {
        return this.messageFilterSettings;
    }

    public MySQLSettings getMySQLSettings () {
        return this.mySQLSettings;
    }

    public ServerSettings getServerSettings () {
        return this.serverSettings;
    }
}