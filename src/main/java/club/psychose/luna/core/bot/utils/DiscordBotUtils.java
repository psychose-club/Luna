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

package club.psychose.luna.core.bot.utils;

import club.psychose.luna.Luna;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidConfigurationDataException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

/*
 * This class provides the utils for the Discord bot.
 */

public final class DiscordBotUtils {
    // This method checks the channels from all registered servers.
    public void checkServers (List<TextChannel> textChannelList) {
        // Gets the server configurations.
        Luna.SETTINGS_MANAGER.getServerSettings().getServerConfigurationHashMap().forEach((serverID, serverSetting) -> {
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
                            this.checkVerificationChannel(serverID, verificationTextChannel, botInformationTextChannel, verificationTextChannel.getGuild());
                        } else {
                            CrashLog.saveLogAsCrashLog(new NullPointerException("Verification channel not found for the server with the id " + serverID + "!"));
                        }
                    } else {
                        CrashLog.saveLogAsCrashLog(new NullPointerException("Bot information channel not found for the server with the id " + serverID + "!"));
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Invalid configuration for the server with the id " + serverID + "!"));
                }
            } else {
                CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Invalid configuration for the server with the id " + serverID + "!"));
            }
        });
    }

    // This method sends a bot information message.
    public void sendBotInformationMessage (String serverID, String message, HashMap<String, String> fieldHashMap, Color color, List<TextChannel> textChannelList) {
        String botInformationChannelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.BOT_INFORMATION);

        if (botInformationChannelID != null) {
            TextChannel botInformationChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(botInformationChannelID, textChannelList);

            if (botInformationChannel != null) {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(botInformationChannel, "Bot information", message, fieldHashMap, FooterType.ERROR, color);
            }
        }
    }

    // This method sends a message to the logging channel.
    public void sendLoggingMessage (String serverID, String message, HashMap<String, String> fieldHashMap, List<TextChannel> textChannelList) {
        String loggingChannelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.LOGGING);

        if (loggingChannelID != null) {
            TextChannel loggingTextChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(loggingChannelID, textChannelList);

            if (loggingTextChannel != null) {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(loggingTextChannel, "Logging", message, fieldHashMap, FooterType.ERROR, Color.RED);
            } else {
                CrashLog.saveLogAsCrashLog(new NullPointerException("Logging channel not found for the server with the id " + serverID + "!"));
            }
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Logging channel not found for the server with the id " + serverID + "!"));
        }
    }

    // This method checks if the verification channel is on the latest version.
    private void checkVerificationChannel (String serverID, TextChannel verificationChannel, TextChannel botInformationChannel, Guild guild) {
        List<Message> messageList = verificationChannel.getIterableHistory().complete();

        // Checks if the version message exist and if it's exist it'll check if it's on the latest version.
        for (Message message : messageList)
            if (message.getContentRaw().equals("Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD))
                return;

        // If not it'll delete the channel history.
        Luna.DISCORD_MANAGER.getDiscordChannelUtils().deleteChannelHistory(serverID, verificationChannel);

        // Fetches the channel id.
        String channelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.VERIFICATION);

        // Checks if the channel id is not equals to the verification channel and fetches the new channel id.
        if (!(channelID.equals(verificationChannel.getId())))
            verificationChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(channelID, guild.getTextChannels());

        // Checks if the channel is not null and sends the bot messages.
        if (verificationChannel != null) {
            verificationChannel.sendMessage("Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD).queue();

            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(verificationChannel, "Verification Process", "Welcome to this server!\nYou need to get verified to gain access to all of the server channels!\nYou accept automatically the server rules by verifying you with the bot and that the bot can maybe collect messages to improve the experience and stability!\n\nYou need to enable your PMs you'll not get any captcha if you disable your PMs!\nTo enable your PMs -> Server Menu -> Privacy Settings -> Allow direct messages from server members. -> Toggle on\n\nEnter " + Luna.SETTINGS_MANAGER.getBotSettings().getPrefix() + "verification to receive a PM!\n\nIf you need help to get verify please contact a server administrator!", "Luna was developed by psychose.club", Color.MAGENTA);
            this.sendChangelog(botInformationChannel);
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Verification channel not found after refresh on the server with the server id " + serverID + "!"));
        }
    }

    // This method sends the latest changelog.
    private void sendChangelog (TextChannel botInformationChannel) {
        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(botInformationChannel, "Changelog - Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD, "[+] Documentation\n[=] Bugfixes", "Luna was developed by psychose.club", Color.MAGENTA);
    }
}