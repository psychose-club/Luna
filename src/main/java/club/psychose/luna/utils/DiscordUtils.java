package club.psychose.luna.utils;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.button.DiscordButton;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordUtils {
    public static void addRoleToMember (Member member, String roleID, List<Role> roleList) {
        if (checkIfRoleExist(roleID, roleList)) {
            if (!(hasUserRole(member, roleID))) {
                member.getRoles().add(getRoleViaID(roleID, roleList));
            }
        }
    }

    public static void removeRoleFromMember (Member member, String roleID, List<Role> roleList) {
        if (checkIfRoleExist(roleID, roleList)) {
            if (hasUserRole(member, roleID)) {
                member.getRoles().remove(getRoleViaID(roleID, roleList));
            }
        }
    }

    // Method to delete a complete channel History.
    public static void deleteChannelHistory (String serverID, TextChannel textChannel) {
        // Saves the messages in an ArrayList to prevent a rate-limit from discord when checking the messages.
        // If we delete the message without checking if all messages written in the past 14 days we will unnecessarily abuse the discord api and get a rate limit.
        // To prevent this we will store temporarily the messages in the ArrayList, and we will only use this if no message is older than 14 days.
        ArrayList<Message> messageArrayList = new ArrayList<>();

        // Checks the messages from the text channel.
        for (Message message : textChannel.getHistory().getChannel().getIterableHistory().complete()) {
            // Initialize variables.
            OffsetDateTime messageOffsetDateTime = message.getTimeCreated();
            LocalDate messageLocalDateTime = LocalDate.from(messageOffsetDateTime);
            LocalDate localDateNow = LocalDate.now();

            // Checks the days between the message creation date and today.
            long daysDifference = ChronoUnit.DAYS.between(messageLocalDateTime, localDateNow);

            // Checks if the difference is not 14 or higher.
            if (daysDifference < 14) {
                // Adds the message to the ArrayList.
                messageArrayList.add(message);
            } else {
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
                        break;
                    }
                }

                // Deletes the text channel.
                textChannel.delete().queue();

                // Sets the position.
                copyOfTheOriginalChannel.getManager().setPosition(textChannelPosition).queue();

                // Returns.
                return;
            }
        }

        // Checks the messages.
        for (Message message : messageArrayList) {
            // Deletes the message.
            message.delete().queue();

            try {
                Thread.sleep(500);
            } catch (InterruptedException interruptedException) {
                CrashLog.saveLogAsCrashLog(interruptedException, textChannel.getJDA().getGuilds());
            }
        }
    }

    public static void refreshVerificationChannel (String serverID, TextChannel verificationChannel, List<TextChannel> textChannelList) {
        if (verificationChannel.hasLatestMessage()) {
            List<Message> messageList = verificationChannel.getHistory().getRetrievedHistory();

            for (Message message : messageList) {
                if (message.getContentRaw().startsWith("Version: " + Constants.VERSION)) {
                    return;
                }
            }

            deleteChannelHistory(serverID, verificationChannel);
        }

        String channelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, DiscordChannels.VERIFICATION);

        if (!(channelID.equals(verificationChannel.getId())))
            verificationChannel = getTextChannel(channelID, textChannelList);

        if (verificationChannel != null) {
            verificationChannel.sendMessage("Version: " + Constants.VERSION + " | Build Version: " + Constants.BUILD).queue();

            sendEmbedMessage(verificationChannel, "Verification Process", "Welcome to this server!\nYou need to get verified to access all channels!\nYou accept automatically the server rules by verifying you with the bot and that the bot can maybe collect messages to improve the experience and stability!\n\nYou need to enable your PMs!\nEnter !verify to receive a PM!", null, "Luna was developed by psychose.club", Color.MAGENTA);
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
    public static void sendEmbedMessage (User user, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color) {
        // Opens the private channel from the user and sends the message to the user.
        user.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessageEmbeds(getEmbedBuilder(title, description, fieldHashMap, footerText, color).build()).queue()));
    }

    // Sends the embed message.
    public static void sendEmbedMessageWithButtons (TextChannel textChannel, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color, ArrayList<DiscordButton> discordButtonArrayList) {
        // Checks if more than 5 buttons are in the list.
        if (discordButtonArrayList.size() > 5) {
            CrashLog.saveLogAsCrashLog(new IOException("Warning! You cannot use more than 5 buttons for the embedded message!"), textChannel.getJDA().getGuilds());
            return;
        }

        // Converts the discord button list to a normal button list.
        List<Button> discordButtonList = discordButtonArrayList.stream().map(DiscordButton::getButton).collect(Collectors.toCollection(ArrayList::new));

        // Builds the embed message.
        MessageEmbed messageEmbed = getEmbedBuilder(title, description, fieldHashMap, footerText, color).build();

        // Sends the message to the text channel.
        textChannel.sendMessageEmbeds(messageEmbed).setActionRow(discordButtonList).queue();
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

    public static boolean checkIfRoleExist (String roleID, List<Role> roleList) {
        return ((roleList.size() != 0) && (roleList.stream().anyMatch(role -> role.getId().equals(roleID))));
    }

    public static boolean hasUserRole (Member member, String roleID) {
        return (member != null) && (member.getRoles().size() != 0) && member.getRoles().stream().anyMatch(role -> role.getId().equals(roleID));
    }

    public static boolean hasUserPermission (Member member, String serverID, PermissionRoles permission) {
        return hasUserPermissions(member, serverID, new PermissionRoles[] {permission});
    }

    public static boolean hasUserPermissions (Member member, String serverID, PermissionRoles[] permissions) {
        return (((member != null) && (member.getRoles().size() != 0) && (Luna.SETTINGS_MANAGER.getServerSettings().containsPermission(serverID, member.getRoles(), permissions))));
    }

    public static boolean isChannelValidForTheDiscordCommand (TextChannel textChannel, String serverID, DiscordChannels[] discordChannels) {
        return ((textChannel != null) && (Luna.SETTINGS_MANAGER.getServerSettings().isValidChannel(serverID, textChannel.getId(), discordChannels)));
    }

    // Creates the embed builder.
    public static EmbedBuilder getEmbedBuilder (String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color)
    {
        // Initialize variable.
        EmbedBuilder embedBuilder = new EmbedBuilder();

        // Sets the embed builder.
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setAuthor("\uD83D\uDC08 L U N A \uD83D\uDC08");
        //embedBuilder.setImage("https://cdn.discordapp.com/icons/670667590812696623/a_824fb3dbf622ccb5434f91e35bb308f9.gif?size=4096");

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

    public static List<Message> getMessageHistory(TextChannel textChannel, int messages) {
        return new ArrayList<>(textChannel.getHistory().retrievePast(messages).complete());
    }

    public static Role getRoleViaID (String roleID, List<Role> roleList) {
        return (roleList.size() != 0 ? (roleList.stream().filter(role -> role.getId().equals(roleID)).findFirst().orElse(null)) : null);
    }

    public static TextChannel getTextChannel (String channelID, List<TextChannel> textChannelList) {
        return textChannelList.stream().filter(textChannel -> textChannel.getId().equals(channelID)).findFirst().orElse(null);
    }
}