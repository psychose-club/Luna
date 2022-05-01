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
                             String command = message.substring(2).trim(); // TODO: Check length when adding custom prefix.
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
                                 boolean skipServerCheck = Arrays.asList(foundDiscordCommand.getPermissions()).contains(PermissionRoles.BOT_OWNER);

                                 if (!(skipServerCheck))
                                     if (!(Luna.DISCORD_MANAGER.getDiscordChannelUtils().isChannelValidForTheDiscordCommand(messageReceivedEvent.getTextChannel(), messageReceivedEvent.getGuild().getId(), foundDiscordCommand.getDiscordChannels())))
                                         return;

                                 if (Luna.DISCORD_MANAGER.getDiscordMemberUtils().checkUserPermission(member, messageReceivedEvent.getGuild().getId(), foundDiscordCommand.getPermissions()))
                                     foundDiscordCommand.onCommandExecution(commandArguments, messageReceivedEvent);
                             }
                         }

                         // TODO: Fix bot sent things in verification channel.
                         if (messageReceivedEvent.getTextChannel().getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(messageReceivedEvent.getGuild().getId(), DiscordChannels.VERIFICATION)))
                             messageReceivedEvent.getMessage().delete().queue();
                     } else {
                         this.warnMember(messageReceivedEvent.getMessage(), messageReceivedEvent.getGuild().getId(), messageReceivedEvent.getTextChannel(), messageReceivedEvent.getGuild().getTextChannels(), member);
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
                                 if (!(Luna.DISCORD_MANAGER.getDiscordMemberUtils().checkUserPermission(member, serverGuild.getId(), new PermissionRoles[] {PermissionRoles.VERIFICATION}))) {
                                     Role verificationRole = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordServerRoleViaID(serverGuild.getId(), PermissionRoles.VERIFICATION, serverGuild.getRoles());

                                     if (verificationRole != null) {
                                         Luna.DISCORD_MANAGER.getDiscordRoleUtils().addVerificationRoleToUser(user, captcha, verificationRole);

                                         try {
                                             Files.deleteIfExists(captcha.getImageFile().toPath());
                                         } catch (IOException ioException) {
                                             CrashLog.saveLogAsCrashLog(ioException, serverGuild.getJDA().getGuilds());
                                         }

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

    @Override
    public void onMessageUpdate (MessageUpdateEvent messageUpdateEvent) {
        if (!(messageUpdateEvent.getAuthor().isBot())) {
            if (!(messageUpdateEvent.getChannelType().equals(ChannelType.PRIVATE))) {
                Member member = messageUpdateEvent.getMember();
                String message = messageUpdateEvent.getMessage().getContentRaw();

                if (member != null) {
                    if (!(this.messageFilter.checkMessage(message)))
                        this.warnMember(messageUpdateEvent.getMessage(), messageUpdateEvent.getGuild().getId(), messageUpdateEvent.getTextChannel(), messageUpdateEvent.getGuild().getTextChannels(), member);
                }
            }
        }
    }

    private void warnMember (Message message, String guildID, TextChannel textChannel, List<TextChannel> textChannelList, Member member) {
        message.delete().queue();
        this.mute.addMuteCount(member);

        if (!(textChannel.getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(guildID, DiscordChannels.VERIFICATION))))
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(textChannel, "Message removed!", "This message contains inappropriate words!\nWarnings: " + this.mute.getMuteCount(member), FooterType.ERROR, Color.RED);

        String timestamp = StringUtils.getDateAndTime("LOG");

        HashMap<String, String> fieldHashMap = new HashMap<>();
        fieldHashMap.put("Member", member.getAsMention());
        fieldHashMap.put("Mute Count", String.valueOf(this.mute.getMuteCount(member)));
        fieldHashMap.put("Detected Word", this.messageFilter.getLastBadWord());
        fieldHashMap.put("Log file", BadWordLog.createBadWordLog(member, guildID, this.messageFilter.getLastBadWord(), message.getContentRaw(), timestamp));
        fieldHashMap.put("Timestamp: ", timestamp);

        Luna.DISCORD_MANAGER.getDiscordBotUtils().sendLoggingMessage(guildID, "Inappropriate word detected!", fieldHashMap, textChannelList);
    }
}