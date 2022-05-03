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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public final class DiscordBotUtils {
    public void checkVerificationChannel (String serverID, TextChannel verificationChannel, TextChannel botInformationChannel, Guild guild) {
        List<Message> messageList = verificationChannel.getIterableHistory().complete();

        for (Message message : messageList) {
            if (message.getContentRaw().equals("Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD))
                return;
        }

        Luna.DISCORD_MANAGER.getDiscordChannelUtils().deleteChannelHistory(serverID, verificationChannel);

        String channelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.VERIFICATION);

        if (!(channelID.equals(verificationChannel.getId())))
            verificationChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(channelID, guild.getTextChannels());

        if (verificationChannel != null) {
            verificationChannel.sendMessage("Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD).queue();

            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(verificationChannel, "Verification Process", "Welcome to this server!\nYou need to get verified to gain access to all of the server channels!\nYou accept automatically the server rules by verifying you with the bot and that the bot can maybe collect messages to improve the experience and stability!\n\nYou need to enable your PMs you'll not get any captcha if you disable your PMs!\nTo enable your PMs -> Server Menu -> Privacy Settings -> Allow direct messages from server members. -> Toggle on\n\nEnter L!verify to receive a PM!\n\nIf you need help to get verify please contact a server administrator!", "Luna was developed by psychose.club", Color.MAGENTA);
            this.sendChangelog(botInformationChannel);
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Verification channel not found after refresh on the server with the server id " + serverID + "!"), null);
        }
    }

    public void sendBotInformationMessage (String serverID, String message, HashMap<String, String> fieldHashMap, Color color, List<TextChannel> textChannelList) {
        String botInformationChannelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.BOT_INFORMATION);

        if (botInformationChannelID != null) {
            TextChannel botInformationChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(botInformationChannelID, textChannelList);

            if (botInformationChannel != null) {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(botInformationChannel, "Bot information", message, fieldHashMap, FooterType.ERROR, color);
            }
        }
    }

    public void sendLoggingMessage (String serverID, String message, HashMap<String, String> fieldHashMap, List<TextChannel> textChannelList) {
        String loggingChannelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.LOGGING);

        if (loggingChannelID != null) {
            TextChannel loggingTextChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getTextChannel(loggingChannelID, textChannelList);

            if (loggingTextChannel != null) {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(loggingTextChannel, "Logging", message, fieldHashMap, FooterType.ERROR, Color.RED);
            } else {
                CrashLog.saveLogAsCrashLog(new NullPointerException("Logging channel not found for the server with the id " + serverID + "!"), null);
            }
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Logging channel not found for the server with the id " + serverID + "!"), null);
        }
    }

    private void sendChangelog (TextChannel botInformationChannel) {
        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(botInformationChannel, "Changelog - Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD, "[=] Verification Bugfix", "Luna was developed by psychose.club", Color.MAGENTA);
    }
}