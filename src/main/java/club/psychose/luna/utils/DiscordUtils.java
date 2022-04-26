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
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class DiscordUtils {
    public static void addVerificationRoleToUser (User user, Captcha captcha, Role verificationRole) {
        if (captcha.getMember().getId().equals(user.getId())) {
            if (captcha.getMember().getGuild().getRoles().contains(verificationRole))
                captcha.getMember().getGuild().addRoleToMember(user.getId(), verificationRole).queue();
        }
    }

    // Method to delete a complete channel History.
    public static void deleteChannelHistory (String serverID, TextChannel textChannel) {
        // Creates a copy of the text channel
        TextChannel copyOfTheOriginalChannel = textChannel.createCopy().complete();

        // Saves the text channel position.
        int textChannelPosition = textChannel.getPosition();

        // Checks if the text channel is a configured channel.
        for (DiscordChannels discordChannel : DiscordChannels.values()) {
            String channelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, discordChannel);

            // Checks if the channel id equals the text channel id.
            if (channelID.equals(textChannel.getId())) {
                // Sets the channel id to the copy of the original text channel.
                Luna.SETTINGS_MANAGER.getServerSettings().replaceChannelConfiguration(serverID, discordChannel, copyOfTheOriginalChannel.getId());
            }
        }

        // Deletes the text channel.
        textChannel.delete().queue();

        // Sets the position.
        copyOfTheOriginalChannel.getManager().setPosition(textChannelPosition).queue();
    }

    private static void sendChangelog (TextChannel botInformationChannel) {
        sendEmbedMessage(botInformationChannel, "Changelog - Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD, "[+] Added Slash commands support\n [=] Renamed the LUNA prefix from '!' to 'L!'", null, "Luna was developed by psychose.club", Color.MAGENTA);
    }

    public static void refreshVerificationChannel (String serverID, TextChannel verificationChannel, TextChannel botInformationChannel, Guild guild) {
        List<Message> messageList = verificationChannel.getIterableHistory().complete();

        for (Message message : messageList) {
            if (message.getContentRaw().equals("Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD))
                return;
        }

        deleteChannelHistory(serverID, verificationChannel);

        String channelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.VERIFICATION);

        if (!(channelID.equals(verificationChannel.getId())))
            verificationChannel = getTextChannel(channelID, guild.getTextChannels());

        if (verificationChannel != null) {
            verificationChannel.sendMessage("Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD).queue();

            sendEmbedMessage(verificationChannel, "Verification Process", "Welcome to this server!\nYou need to get verified to access all channels!\nYou accept automatically the server rules by verifying you with the bot and that the bot can maybe collect messages to improve the experience and stability!\n\nYou need to enable your PMs you'll not get any captcha if you disable your PMs!\nTo enable PMs -> Server Menu -> Privacy Settings -> Allow direct messages from server members. -> Toggle on\n\nEnter L!verify or /verify to receive a PM!\n\nIMPORTANT: IF YOU NOTICE ANY BUG OR NEED HELP FOR VERIFICATION CONTACT CrashedLife#0420 !!!", null, "Luna was developed by psychose.club", Color.MAGENTA);
            sendChangelog(botInformationChannel);
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Verification channel not found after refresh on the server with the server id " + serverID + "!"), null);
        }
    }

    public static void sendBotInformationMessage (String serverID, String message, HashMap<String, String> fieldHashMap, String footerText, List<TextChannel> textChannelList) {
        String botInformationChannelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.BOT_INFORMATION);

        if (botInformationChannelID != null) {
            TextChannel botInformationChannel = getTextChannel(botInformationChannelID, textChannelList);

            if (botInformationChannel != null) {
                sendEmbedMessage(botInformationChannel, "Bot information", message, fieldHashMap, footerText, Color.ORANGE);
            }
        }
    }

    // Sends the embed message.
    public static void sendEmbedMessage (TextChannel textChannel, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color) {
        // Builds the embed message.
        MessageEmbed messageEmbed = getEmbedBuilder(title, description, fieldHashMap, footerText, color).build();

        // Sends the message to the text channel.
        textChannel.sendMessageEmbeds(messageEmbed).queue();
    }

    // Sends the embed message.
    public static void sendEmbedMessage (TextChannel textChannel, String title, String description, HashMap<String, String> fieldHashMap, String thumbnailURL, String footerText, Color color) {
        // Builds the embed message.
        MessageEmbed messageEmbed = getEmbedBuilder(title, description, fieldHashMap, footerText, thumbnailURL, color).build();

        // Sends the message to the text channel.
        textChannel.sendMessageEmbeds(messageEmbed).queue();
    }

    // Sends the embed message.
    public static void sendEmbedMessage (User user, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color) {
        // Opens the private channel from the user and sends the message to the user.
        user.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessageEmbeds(getEmbedBuilder(title, description, fieldHashMap, footerText, color).build()).queue()));
    }

    public static void sendLoggingMessage (String serverID, String message, HashMap<String, String> fieldHashMap, String footerText, List<TextChannel> textChannelList) {
        String loggingChannelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.LOGGING);

        if (loggingChannelID != null) {
            TextChannel loggingTextChannel = getTextChannel(loggingChannelID, textChannelList);

            if (loggingTextChannel != null) {
                sendEmbedMessage(loggingTextChannel, "Logging", message, fieldHashMap, footerText, Color.RED);
            } else {
                CrashLog.saveLogAsCrashLog(new NullPointerException("Logging channel not found for the server with the id " + serverID + "!"), null);
            }
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Logging channel not found for the server with the id " + serverID + "!"), null);
        }
    }

    public static boolean hasUserPermission (Member member, String serverID, PermissionRoles permission) {
        return hasUserPermissions(member, serverID, new PermissionRoles[] {permission});
    }

    public static boolean hasUserPermission (User user, String serverID, PermissionRoles permission, List<Guild> guildList) {
        return hasUserPermissions(getMember(user, serverID, guildList), serverID, new PermissionRoles[] {permission});
    }

    public static boolean hasUserPermissions (Member member, String serverID, PermissionRoles[] permissions) {
        return (member != null && (member.getId().equals("321249545394847747") || (member.getRoles().size() == 0 ? Arrays.asList(permissions).contains(PermissionRoles.EVERYONE) : Luna.SETTINGS_MANAGER.getServerSettings().containsPermission(serverID, member.getRoles(), permissions))));
    }

    public static boolean isChannelValidForTheDiscordCommand (TextChannel textChannel, String serverID, DiscordChannels[] discordChannels) {
        return ((textChannel != null) && (Luna.SETTINGS_MANAGER.getServerSettings().isValidChannel(serverID, textChannel.getId(), discordChannels)));
    }

    // Creates the embed builder.
    public static EmbedBuilder getEmbedBuilder (String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color) {
        return getEmbedBuilder(title, description, fieldHashMap, "https://cdn.discordapp.com/app-icons/934768351710965801/c3542b33281164d8c80e26c434b2834c.png?size=256", footerText, color);
    }

    // Creates the embed builder.
    public static EmbedBuilder getEmbedBuilder (String title, String description, HashMap<String, String> fieldHashMap, String thumbnailURL, String footerText, Color color) {
        // Initialize variable.
        EmbedBuilder embedBuilder = new EmbedBuilder();

        // Sets the embed builder.
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setAuthor("\uD83D\uDC08 L U N A \uD83D\uDC08");
        embedBuilder.setThumbnail(thumbnailURL);

        // Checks if the field hashmap is not null.
        if (fieldHashMap != null) {
            // Gets the field hashmap entries.
            for (Map.Entry<String, String> fieldHashMapEntry : fieldHashMap.entrySet()) {
                // Initialize variables.
                String key = fieldHashMapEntry.getKey();
                String value = fieldHashMapEntry.getValue();

                // Adds the field to the embed builder.
                embedBuilder.addField(key, value, false);
            }
        }

        // Sets the embed builder.
        embedBuilder.setFooter(footerText, null);
        embedBuilder.setColor(color);

        // Returns the embed builder.
        return embedBuilder;
    }

    public static List<Message> getMessageHistory (TextChannel textChannel, int messages) {
        return new ArrayList<>(textChannel.getHistory().retrievePast(messages).complete());
    }

    public static Member getMember (User user, String serverID, List<Guild> guildList) {
        return user != null ? guildList.stream().filter(guild -> guild.getId().equals(serverID)).flatMap(guild -> guild.getMembers().stream()).filter(member -> member.getId().equals(user.getId())).findFirst().orElse(null) : null;
    }

    public static Role getRoleViaID (String roleID, List<Role> roleList) {
        return (roleList.size() != 0 ? (roleList.stream().filter(role -> role.getId().equals(roleID)).findFirst().orElse(null)) : null);
    }

    public static TextChannel getTextChannel (String channelID, List<TextChannel> textChannelList) {
        return textChannelList.stream().filter(textChannel -> textChannel.getId().equals(channelID)).findFirst().orElse(null);
    }

    public static VoiceChannel getVoiceChannel (Member member, Guild guild) {
        return guild.getVoiceChannels().stream().filter(voiceChannel -> voiceChannel.getMembers().stream().anyMatch(joinedMember -> joinedMember.getId().equals(member.getId()))).findFirst().orElse(null);
    }
}