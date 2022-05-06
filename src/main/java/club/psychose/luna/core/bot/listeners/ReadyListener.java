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

package club.psychose.luna.core.bot.listeners;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidConfigurationDataException;
import club.psychose.luna.core.system.settings.ServerSetting;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.logging.ConsoleLogger;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/*
 * This class handles the ready event when the bot is started and connected successfully to the Discord websocket.
 */

public final class ReadyListener extends ListenerAdapter {
    // Ready event.
    @Override
    public void onReady (ReadyEvent readyEvent) {
        // Fetches all text channels.
        List<TextChannel> textChannelList = readyEvent.getJDA().getTextChannels();

        // Gets the server configurations.
        for (Map.Entry<String, ServerSetting> serverSettingEntry : Luna.SETTINGS_MANAGER.getServerSettings().getServerConfigurationHashMap().entrySet()) {
            String serverID = serverSettingEntry.getKey();
            ServerSetting serverSetting = serverSettingEntry.getValue();

            // Checks if the setting is not null.
            if (serverSetting != null) {
                // Checks if the verification and bot information channel exist.
                if ((serverSetting.getVerificationChannelID() != null) && (serverSetting.getBotInformationChannelID() != null)) {
                    // Gets the channels.
                    TextChannel botInformationTextChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(serverSetting.getBotInformationChannelID(), textChannelList);
                    TextChannel verificationTextChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(serverSetting.getVerificationChannelID(), textChannelList);

                    // Checks if the channels are not null.
                    if (botInformationTextChannel != null) {
                        if (verificationTextChannel != null) {
                            // Checks the version state of the verification channel.
                            Luna.DISCORD_MANAGER.getDiscordBotUtils().checkVerificationChannel(serverID, verificationTextChannel, botInformationTextChannel, verificationTextChannel.getGuild());
                        } else {
                            CrashLog.saveLogAsCrashLog(new NullPointerException("Verification channel not found for the server with the id " + serverID +  "!"), readyEvent.getJDA().getGuilds());
                        }
                    } else {
                        CrashLog.saveLogAsCrashLog(new NullPointerException("Bot information channel not found for the server with the id " + serverID +  "!"), readyEvent.getJDA().getGuilds());
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Invalid configuration for the server with the id " + serverID + "!"), readyEvent.getJDA().getGuilds());
                }
            } else {
                CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Invalid configuration for the server with the id " + serverID + "!"), readyEvent.getJDA().getGuilds());
            }
        }

        // Checks if the temp folder exist and fetches the folder content.
        if (Files.exists(Constants.getLunaFolderPath("\\temp\\"))) {
            File[] tempFiles = Constants.getLunaFolderPath("\\temp\\").toFile().listFiles();

            // Checks if the folder has contents available.
            if (tempFiles != null) {
                // Deletes the file if it's not a bot used captcha file.
                for (File file : tempFiles) {
                    boolean isCaptchaFile = false;
                    for (Captcha captcha : DiscordBot.CAPTCHA_MANAGER.getCaptchaArrayList()) {
                        if (captcha.getImageFile().equals(file)) {
                            isCaptchaFile = true;
                            break;
                        }
                    }

                    if (!(isCaptchaFile)) {
                        try {
                            Files.deleteIfExists(file.toPath());
                        } catch (IOException ioException) {
                            CrashLog.saveLogAsCrashLog(ioException, readyEvent.getJDA().getGuilds());
                        }
                    }
                }
            }
        }

        // Debug stuff.
        ConsoleLogger.debug("Bot started successfully!");
        ConsoleLogger.printEmptyLine();
    }
}