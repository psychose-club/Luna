/*
 * Copyright © 2022 psychose.club
 * Contact: psychose.club@gmail.com
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
import club.psychose.luna.core.logging.BadWordLog;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.DiscordUtils;
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

public final class MessageListener extends ListenerAdapter {
    private final MessageFilter messageFilter = new MessageFilter();
    private final Mute mute = new Mute();

    @Override
    public void onMessageReceived (MessageReceivedEvent messageReceivedEvent) {
         if (!(messageReceivedEvent.getAuthor().isBot())) {
             Member member = messageReceivedEvent.getMember();
             String message = messageReceivedEvent.getMessage().getContentRaw();

             if (!(messageReceivedEvent.getChannelType().equals(ChannelType.PRIVATE))) {
                 if (member != null) {
                     if (this.messageFilter.checkMessage(message)) {
                         if (message.startsWith("L!")) {
                             String command = message.substring(1).trim();
                             String[] commandArguments = null;

                             if (command.contains(" ")) {
                                 commandArguments = command.split(" ");
                                 command = commandArguments[0].trim();

                                 commandArguments = Arrays.copyOfRange(commandArguments, 1, commandArguments.length);
                             }

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

                             if (commandFound) {
                                 if (DiscordUtils.isChannelValidForTheDiscordCommand(messageReceivedEvent.getTextChannel(), messageReceivedEvent.getGuild().getId(), foundDiscordCommand.getDiscordChannels())) {
                                     if (DiscordUtils.hasUserPermissions(member, messageReceivedEvent.getGuild().getId(), foundDiscordCommand.getPermissions())) {
                                         foundDiscordCommand.onCommandExecution(commandArguments, messageReceivedEvent);
                                     }
                                 }
                             }
                         }

                         // TODO: Fix bot sent things in verification channel.
                         if (messageReceivedEvent.getTextChannel().getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(messageReceivedEvent.getGuild().getId(), DiscordChannels.VERIFICATION)))
                             messageReceivedEvent.getMessage().delete().queue();
                     } else {
                         messageReceivedEvent.getMessage().delete().queue();
                         this.mute.addMuteCount(member);

                         if (!(messageReceivedEvent.getTextChannel().getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(messageReceivedEvent.getGuild().getId(), DiscordChannels.VERIFICATION))))
                             DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Message removed!", "This message contains inappropriate words!\nWarnings: " + this.mute.getMuteCount(member), null, "\uD83D\uDC08", Color.RED);

                         String timestamp = StringUtils.getDateAndTime("LOG");

                         HashMap<String, String> fieldHashMap = new HashMap<>();
                         fieldHashMap.put("Member", member.getAsMention());
                         fieldHashMap.put("Mute Count", String.valueOf(this.mute.getMuteCount(member)));
                         fieldHashMap.put("Detected Word", this.messageFilter.getLastBadWord());
                         fieldHashMap.put("Log file", BadWordLog.createBadWordLog(member, messageReceivedEvent.getGuild().getId(), this.messageFilter.getLastBadWord(), message, timestamp));

                         DiscordUtils.sendLoggingMessage(messageReceivedEvent.getGuild().getId(), "Inappropriate word detected!", fieldHashMap, "Timestamp: " + timestamp, messageReceivedEvent.getGuild().getTextChannels());
                     }
                 }
             } else {
                 User user = messageReceivedEvent.getMessage().getAuthor();
                 if (DiscordBot.CAPTCHA_MANAGER.hasMemberACaptcha(user)) {
                     Captcha captcha = DiscordBot.CAPTCHA_MANAGER.getMemberCaptcha(user);

                     if (captcha != null) {
                         String captchaCode = captcha.getCaptchaCode();

                         if (captchaCode.equals(message)) {
                             Guild serverGuild = messageReceivedEvent.getJDA().getGuilds().stream().filter(guild -> guild.getId().equals(captcha.getServerID())).findFirst().orElse(null);

                             if (serverGuild != null) {
                                 if (!(DiscordUtils.hasUserPermission(user, serverGuild.getId(), PermissionRoles.VERIFICATION, user.getMutualGuilds()))) {
                                     Role verificationRole = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordServerRoleViaID(serverGuild.getId(), PermissionRoles.VERIFICATION, serverGuild.getRoles());

                                     if (verificationRole != null) {
                                         DiscordUtils.addVerificationRoleToUser(user, captcha, verificationRole);

                                         try {
                                             Files.deleteIfExists(captcha.getImageFile().toPath());
                                         } catch (IOException ioException) {
                                             CrashLog.saveLogAsCrashLog(ioException, serverGuild.getJDA().getGuilds());
                                         }

                                         DiscordBot.CAPTCHA_MANAGER.removeCaptcha(captcha);
                                         DiscordUtils.sendEmbedMessage(user, "Verification was successful!", "You are now verified!", null, "have fun and :)", Color.GREEN);
                                     } else {
                                         CrashLog.saveLogAsCrashLog(new NullPointerException("Verification Role not found!"), serverGuild.getJDA().getGuilds());
                                         DiscordUtils.sendEmbedMessage(user, "Something went wrong!", "Please try it again later!", null, "The developers got a notification to fix this issue!", Color.RED);
                                     }
                                 } else {
                                     DiscordUtils.sendEmbedMessage(user, "Something went wrong!", "You are already verified!", null, "sketchy", Color.RED);
                                 }
                             } else {
                                 CrashLog.saveLogAsCrashLog(new NullPointerException("Guild not found!"), null);
                                 DiscordUtils.sendEmbedMessage(user, "Something went wrong!", "Please try it again later!", null, "The developers got a notification to fix this issue!", Color.RED);
                             }
                         } else {
                             DiscordUtils.sendEmbedMessage(user, "Verification failed!", "You have entered an invalid captcha code!", null, "robot beep boop", Color.RED);
                         }
                     }
                 }
             }
         }
    }

    @Override
    public void onMessageUpdate (MessageUpdateEvent messageUpdateEvent) {
        if (!(messageUpdateEvent.getAuthor().isBot())) {
            if (!(messageUpdateEvent.getChannelType().equals(ChannelType.PRIVATE))) {
                Member member = messageUpdateEvent.getMember();
                String message = messageUpdateEvent.getMessage().getContentRaw();

                if (member != null) {
                    if (!(this.messageFilter.checkMessage(message))) {
                        messageUpdateEvent.getMessage().delete().queue();
                        this.mute.addMuteCount(member);

                        if (!(messageUpdateEvent.getTextChannel().getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(messageUpdateEvent.getGuild().getId(), DiscordChannels.VERIFICATION))))
                            DiscordUtils.sendEmbedMessage(messageUpdateEvent.getTextChannel(), "Message removed!", "This message contains inappropriate words!\nWarnings: " + this.mute.getMuteCount(member), null, "\uD83D\uDC08", Color.RED);

                        String timestamp = StringUtils.getDateAndTime("LOG");

                        HashMap<String, String> fieldHashMap = new HashMap<>();
                        fieldHashMap.put("Member", member.getAsMention());
                        fieldHashMap.put("Mute Count", String.valueOf(this.mute.getMuteCount(member)));
                        fieldHashMap.put("Detected Word", this.messageFilter.getLastBadWord());
                        fieldHashMap.put("Log file", BadWordLog.createBadWordLog(member, messageUpdateEvent.getGuild().getId(), this.messageFilter.getLastBadWord(), message, timestamp));

                        DiscordUtils.sendLoggingMessage(messageUpdateEvent.getGuild().getId(), "Inappropriate word detected!", fieldHashMap, "Timestamp: " + timestamp, messageUpdateEvent.getGuild().getTextChannels());
                    }
                }
            }
        }
    }
}