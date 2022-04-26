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
import club.psychose.luna.core.system.settings.BotSettings;
import club.psychose.luna.core.system.settings.FilterSettings;
import club.psychose.luna.core.system.settings.ServerSetting;
import club.psychose.luna.core.system.settings.ServerSettings;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidConfigurationDataException;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.logging.ConsoleLogger;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public final class SettingsManager {
    private final BotSettings botSettings = new BotSettings();
    private final FilterSettings filterSettings = new FilterSettings();
    private final ServerSettings serverSettings = new ServerSettings();

    private int retryBotSettings = 0;

    public void loadBotSettings () {
        if (retryBotSettings <= 1) {
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
                retryBotSettings ++;
                this.saveSettings();
                this.loadBotSettings();
            }
        } else {
            CrashLog.saveLogAsCrashLog(new IOException("Bot settings cannot be created!"), null);
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

    public void loadFilterSettings () {
        this.updateWhitelist();
        this.updateBlacklist();
        this.updateFilters();
    }

    public void loadServerSettings () {
        if (Files.exists(Constants.getLunaFolderPath("\\servers\\"))) {
            File[] files = Constants.getLunaFolderPath("\\servers\\").toFile().listFiles();

            if (files != null) {
                ArrayList<String> configurationFilesArrayList = new ArrayList<>();

                Arrays.stream(files).filter(File::isDirectory).forEachOrdered(file -> {
                    File[] directoryFiles = file.listFiles();

                    if (directoryFiles != null) {
                        Arrays.stream(directoryFiles).filter(File::isDirectory).forEachOrdered(directoryFile -> {
                            File[] settingsDirectoryFiles = directoryFile.listFiles();

                            if (settingsDirectoryFiles != null) {
                                Arrays.stream(settingsDirectoryFiles).filter(File::isFile).forEachOrdered(settingsDirectoryFile -> {
                                    if (settingsDirectoryFile.getName().endsWith(".json")) {
                                        String absolutePath = settingsDirectoryFile.getAbsolutePath();
                                        configurationFilesArrayList.add(absolutePath);
                                    }
                                });
                            } else {
                                CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException(directoryFile.getAbsolutePath() + " has no files inside!"), null);
                            }
                        });
                    } else {
                        CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException(file.getAbsolutePath() + " has no files inside!"), null);
                    }
                });

                for (String configurationFile : configurationFilesArrayList) {
                    JsonObject jsonObject = Luna.FILE_MANAGER.readJsonObject(Paths.get(configurationFile));
                    if (jsonObject != null) {
                        if (((jsonObject.has("Server ID")) && (jsonObject.has("Owner Role ID")) && ((jsonObject.has("Admin Role ID")) && ((jsonObject.has("Moderator Role ID")) && ((jsonObject.has("Verification Role ID")) && ((jsonObject.has("Bot information Channel ID")) && ((jsonObject.has("Logging Channel ID")) && ((jsonObject.has("Verification Channel ID")))))))))) {
                            ServerSetting serverSetting = new ServerSetting(jsonObject.get("Owner Role ID").getAsString(), jsonObject.get("Admin Role ID").getAsString(), jsonObject.get("Moderator Role ID").getAsString(), jsonObject.get("Verification Role ID").getAsString(), jsonObject.get("Bot information Channel ID").getAsString(), jsonObject.get("Logging Channel ID").getAsString(), jsonObject.get("Verification Channel ID").getAsString());
                            this.getServerSettings().addServerConfiguration(jsonObject.get("Server ID").getAsString(), serverSetting);
                        } else {
                            CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException(configurationFile + " has an invalid JsonObject!"), null);
                        }
                    } else {
                        CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException(configurationFile + " has an invalid JsonObject!"), null);
                    }
                }
            } else {
                ConsoleLogger.debug("No server is available to be loaded!");
            }
        } else {
            ConsoleLogger.debug("No server is available to be loaded!");
        }
    }

    public void loadSettings () {
        this.loadBotSettings();
        this.loadFilterSettings();
        this.loadServerSettings();
    }

    public void saveBotSettings () {
        JsonObject botSettingsJsonObject = new JsonObject();

        botSettingsJsonObject.addProperty("Bot Token", this.getBotSettings().getBotToken());
        botSettingsJsonObject.addProperty("YouTube API Key", this.getBotSettings().getYoutubeAPIKey());
        botSettingsJsonObject.addProperty("Message Filter URL", this.getBotSettings().getMessageFilterURL());
        botSettingsJsonObject.addProperty("Message Whitelist Filter URL", this.getBotSettings().getMessageWhitelistFilterURL());

        Luna.FILE_MANAGER.saveJsonObject(Constants.getLunaFolderPath("\\settings\\bot_settings.json"), botSettingsJsonObject);
    }

    public void saveServerSettings () {
        JsonObject serverSettingsJsonObject;

        for (Map.Entry<String, ServerSetting> serverDataMapEntry : this.getServerSettings().getServerConfigurationHashMap().entrySet()) {
            serverSettingsJsonObject = new JsonObject();

            String serverID = serverDataMapEntry.getKey();
            ServerSetting serverSetting = serverDataMapEntry.getValue();

            serverSettingsJsonObject.addProperty("Server ID", serverID);
            serverSettingsJsonObject.addProperty("Owner Role ID", serverSetting.getOwnerRoleID());
            serverSettingsJsonObject.addProperty("Admin Role ID", serverSetting.getAdminRoleID());
            serverSettingsJsonObject.addProperty("Moderator Role ID", serverSetting.getModeratorRoleID());
            serverSettingsJsonObject.addProperty("Verification Role ID", serverSetting.getVerificationRoleID());
            serverSettingsJsonObject.addProperty("Bot information Channel ID", serverSetting.getBotInfoChannelID());
            serverSettingsJsonObject.addProperty("Logging Channel ID", serverSetting.getLoggingChannelID());
            serverSettingsJsonObject.addProperty("Verification Channel ID", serverSetting.getVerificationChannelID());

            Luna.FILE_MANAGER.saveJsonObject(Constants.getLunaFolderPath("\\servers\\" + serverID + "\\settings\\ids.json"), serverSettingsJsonObject);
        }
    }

    public void saveSettings () {
        this.saveBotSettings();
        this.saveServerSettings();
    }

    public BotSettings getBotSettings () {
        return this.botSettings;
    }

    public FilterSettings getFilterSettings () {
        return this.filterSettings;
    }

    public ServerSettings getServerSettings () {
        return this.serverSettings;
    }
}