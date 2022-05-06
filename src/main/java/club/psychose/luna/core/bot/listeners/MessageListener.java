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
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.filter.MessageFilter;
import club.psychose.luna.core.bot.mute.Mute;
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.logging.BadWordLog;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * This class handles all sent messages in a text channel on Discord.
 */

public final class MessageListener extends ListenerAdapter {
    private final MessageFilter messageFilter = new MessageFilter();
    private final Mute mute = new Mute();

    // Received message event.
    @Override
    public void onMessageReceived (MessageReceivedEvent messageReceivedEvent) {
        // Checks if the message is not sent by a bot.
         if (!(messageReceivedEvent.getAuthor().isBot())) {
             // Gets the member and the message.
             Member member = messageReceivedEvent.getMember();
             String message = messageReceivedEvent.getMessage().getContentRaw();

             // The bot checks if the message is not sent over the private channel.
             // If it's sent over the private channel it'll check for an open unsolved captcha.
             if (!(messageReceivedEvent.getChannelType().equals(ChannelType.PRIVATE))) {
                 // Checks if the member is not null.
                 if (member != null) {
                     // Checks if the message is not blacklisted.
                     if (this.messageFilter.checkMessage(message)) {
                         // Checks if the message is starts with the bot prefix.
                         if (message.startsWith(Luna.SETTINGS_MANAGER.getBotSettings().getPrefix())) {
                             // Resolves the command and command arguments from the message.
                             String command = message.substring(Luna.SETTINGS_MANAGER.getBotSettings().getPrefix().length()).trim();
                             String[] commandArguments = null;

                             if (command.contains(" ")) {
                                 commandArguments = command.split(" ");
                                 command = commandArguments[0].trim();

                                 commandArguments = Arrays.copyOfRange(commandArguments, 1, commandArguments.length);
                             }

                             // Searches for the command or an alias from the command.
                             boolean commandFound = false;
                             DiscordCommand foundDiscordCommand = null;

                             for (DiscordCommand discordCommand : DiscordBot.COMMAND_MANAGER.getDiscordCommandsArrayList()) {
                                 if (discordCommand.getCommandName().equalsIgnoreCase(command)) {
                                     commandFound = true;
                                     foundDiscordCommand = discordCommand;
                                     break;
                                 } else if (discordCommand.getAliases().length != 0) {
                                     for (String alias : discordCommand.getAliases()) {
                                         if (alias.equalsIgnoreCase(command)) {
                                             commandFound = true;
                                             foundDiscordCommand = discordCommand;
                                             break;
                                         }
                                     }
                                 }
                             }

                             // Checks if the command was found.
                             if (commandFound) {
                                 // Here we'll check if the command requires the bot owner permission.
                                 boolean skipServerCheck = Arrays.asList(foundDiscordCommand.getPermissions()).contains(PermissionRoles.BOT_OWNER);

                                 // If not the bot permission is required we'll look if the channel is valid for the command.
                                 // So never ever a command should have the bot owner permission and another permission because then the channel will not be checked.
                                 if (!(skipServerCheck))
                                     if (!(Luna.DISCORD_MANAGER.getDiscordChannelUtils().isChannelValidForTheDiscordCommand(messageReceivedEvent.getTextChannel(), messageReceivedEvent.getGuild().getId(), foundDiscordCommand.getDiscordChannels())))
                                         return;

                                 // Here we check if the user has the permissions to execute the command.
                                 if (Luna.DISCORD_MANAGER.getDiscordMemberUtils().checkUserPermission(member, messageReceivedEvent.getGuild().getId(), foundDiscordCommand.getPermissions()))
                                     foundDiscordCommand.onCommandExecution(commandArguments, messageReceivedEvent);
                             }
                         }

                         // If the channel is the verification channel it'll automatically delete the message.
                         if (messageReceivedEvent.getTextChannel().getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(messageReceivedEvent.getGuild().getId(), DiscordChannels.VERIFICATION)))
                             messageReceivedEvent.getMessage().delete().queue();
                     } else {
                         // We'll warn a member about the behaviour.
                         this.warnMember(messageReceivedEvent.getMessage(), messageReceivedEvent.getGuild().getId(), messageReceivedEvent.getTextChannel(), messageReceivedEvent.getGuild().getTextChannels(), member);
                     }
                 }
             } else {
                 // Gets the user from the member.
                 User user = messageReceivedEvent.getMessage().getAuthor();

                 // Checks if the user has a captcha open.
                 if (DiscordBot.CAPTCHA_MANAGER.hasMemberACaptcha(user)) {
                     // Resolve the user captcha.
                     Captcha captcha = DiscordBot.CAPTCHA_MANAGER.getMemberCaptcha(user);

                     // Checks if the captcha is not null.
                     if (captcha != null) {
                         // Resolves the captcha code.
                         String captchaCode = captcha.getCaptchaCode();

                         // Checks if the message is the captcha code.
                         if (captchaCode.equals(message)) {
                             // Gets the server to give the user the verification role.
                             Guild serverGuild = messageReceivedEvent.getJDA().getGuilds().stream().filter(guild -> guild.getId().equals(captcha.getServerID())).findFirst().orElse(null);

                             // Checks if the server is not null.
                             if (serverGuild != null) {
                                 // Checks if the user has not already got the verification role.
                                 if (!(Luna.DISCORD_MANAGER.getDiscordMemberUtils().checkUserPermission(member, serverGuild.getId(), new PermissionRoles[] {PermissionRoles.VERIFICATION}))) {
                                     // Resolves the verification role.
                                     Role verificationRole = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordServerRoleViaPermissionRole(serverGuild.getId(), PermissionRoles.VERIFICATION, serverGuild.getRoles());

                                     // Checks if the verification role is not null.
                                     if (verificationRole != null) {
                                         // Adds the role to the user.
                                         Luna.DISCORD_MANAGER.getDiscordRoleUtils().addVerificationRoleToUser(user, captcha, verificationRole);

                                         try {
                                             // Deletes the captcha.
                                             Files.deleteIfExists(captcha.getImageFile().toPath());
                                         } catch (IOException ioException) {
                                             CrashLog.saveLogAsCrashLog(ioException, serverGuild.getJDA().getGuilds());
                                         }

                                         // Removes the captcha.
                                         DiscordBot.CAPTCHA_MANAGER.removeCaptcha(captcha);
                                         Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(user, "Verification was successful!", "You are now verified!", FooterType.SUCCESS, Color.GREEN);
                                     } else {
                                         CrashLog.saveLogAsCrashLog(new NullPointerException("Verification Role not found!"), serverGuild.getJDA().getGuilds());
                                         Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(user, "Something went wrong!", "Please try it again later!", FooterType.ERROR, Color.RED);
                                     }
                                 } else {
                                     Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(user, "Something went wrong!", "You are already verified!", FooterType.ERROR, Color.RED);
                                 }
                             } else {
                                 CrashLog.saveLogAsCrashLog(new NullPointerException("Guild not found!"), null);
                                 Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(user, "Something went wrong!", "Please try it again later!", FooterType.ERROR, Color.RED);
                             }
                         } else {
                             Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(user, "Verification failed!", "You have entered an invalid captcha code!", FooterType.ERROR, Color.RED);
                         }
                     }
                 }
             }
         }
    }

    // Update message event.
    @Override
    public void onMessageUpdate (MessageUpdateEvent messageUpdateEvent) {
        // Checks if the message is not sent by a bot.
        if (!(messageUpdateEvent.getAuthor().isBot())) {
            // The bot checks if the message is not sent over the private channel.
            if (!(messageUpdateEvent.getChannelType().equals(ChannelType.PRIVATE))) {
                // Gets the member and the message.
                Member member = messageUpdateEvent.getMember();
                String message = messageUpdateEvent.getMessage().getContentRaw();

                // Checks if the member is not null.
                if (member != null) {
                    // Checks if the message is not blacklisted.
                    // If it's blacklisted the member will be warned.
                    if (!(this.messageFilter.checkMessage(message)))
                        this.warnMember(messageUpdateEvent.getMessage(), messageUpdateEvent.getGuild().getId(), messageUpdateEvent.getTextChannel(), messageUpdateEvent.getGuild().getTextChannels(), member);
                }
            }
        }
    }

    // This method warns the member.
    private void warnMember (Message message, String guildID, TextChannel textChannel, List<TextChannel> textChannelList, Member member) {
        // Deletes the message.
        message.delete().queue();

        // Adds a mute count.
        this.mute.addMuteCount(member);

        // Checks if the text channel is not the verification channel.
        if (!(textChannel.getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(guildID, DiscordChannels.VERIFICATION))))
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(textChannel, "Message removed!", "This message contains inappropriate words!\nWarnings: " + this.mute.getMuteCount(member), FooterType.ERROR, Color.RED);

        // Fetches the current timestamp.
        String timestamp = StringUtils.getDateAndTime("LOG");

        // Creates a FieldHashMap.
        HashMap<String, String> fieldHashMap = new HashMap<>();
        fieldHashMap.put("Member", member.getAsMention());
        fieldHashMap.put("Mute Count", String.valueOf(this.mute.getMuteCount(member)));
        fieldHashMap.put("Detected Word", this.messageFilter.getLastBadWord());
        fieldHashMap.put("Log file", BadWordLog.createBadWordLog(member, guildID, this.messageFilter.getLastBadWord(), message.getContentRaw(), timestamp));
        fieldHashMap.put("Timestamp: ", timestamp);

        // Sends a logging message.
        Luna.DISCORD_MANAGER.getDiscordBotUtils().sendLoggingMessage(guildID, "Inappropriate word detected!", fieldHashMap, textChannelList);
    }
}