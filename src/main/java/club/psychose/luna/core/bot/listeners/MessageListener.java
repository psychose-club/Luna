package club.psychose.luna.core.bot.listeners;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.filter.MessageFilter;
import club.psychose.luna.core.bot.mute.Mute;
import club.psychose.luna.core.logging.BadWordLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.utils.DiscordUtils;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class MessageListener extends ListenerAdapter {
    private final MessageFilter messageFilter = new MessageFilter();
    private final Mute mute = new Mute();

    @Override
    public void onMessageReceived (MessageReceivedEvent messageReceivedEvent) {
         if (!(messageReceivedEvent.getAuthor().isBot())) {
             if (!(messageReceivedEvent.getChannelType().equals(ChannelType.PRIVATE))) {
                 Member member = messageReceivedEvent.getMember();
                 String message = messageReceivedEvent.getMessage().getContentRaw();

                 if (member != null) {
                     if (!(this.mute.isMemberMuted(member))) {
                         if (this.messageFilter.checkMessage(message)) {
                             if (message.startsWith("!")) {
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

                                 if (messageReceivedEvent.getTextChannel().getId().equals(Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(messageReceivedEvent.getGuild().getId(), DiscordChannels.VERIFICATION)))
                                     messageReceivedEvent.getMessage().delete().queue();
                             }
                         } else {
                             messageReceivedEvent.getMessage().delete().queue();
                             this.mute.addMuteCount(member);

                             DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Message removed!", "This message contains inappropriate words!\nWarnings: " + this.mute.getMuteCount(member) + "\nMuted: " + this.mute.isMemberMuted(member), null, "\uD83D\uDC08", Color.RED);

                             String timestamp = StringUtils.getDateAndTime("LOG");

                             HashMap<String, String> fieldHashMap = new HashMap<>();
                             fieldHashMap.put("Member", member.getAsMention());
                             fieldHashMap.put("Mute Count", String.valueOf(this.mute.getMuteCount(member)));
                             fieldHashMap.put("Muted", String.valueOf(this.mute.isMemberMuted(member)));
                             fieldHashMap.put("Detected Word", this.messageFilter.getLastBadWord());
                             fieldHashMap.put("Log file", BadWordLog.createBadWordLog(member, messageReceivedEvent.getGuild().getId(), this.messageFilter.getLastBadWord(), message, timestamp));

                             DiscordUtils.sendLoggingMessage(messageReceivedEvent.getGuild().getId(), "Inappropriate word detected!", fieldHashMap, "Timestamp: " + timestamp, messageReceivedEvent.getGuild().getTextChannels());
                         }
                     } else {
                         messageReceivedEvent.getMessage().delete().queue();
                     }
                 }
             } else {
                 // TODO: Verification
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
                    if (!(this.mute.isMemberMuted(member))) {
                        if (!(this.messageFilter.checkMessage(message))) {
                            messageUpdateEvent.getMessage().delete().queue();
                            this.mute.addMuteCount(member);

                            DiscordUtils.sendEmbedMessage(messageUpdateEvent.getTextChannel(), "Message removed!", "This message contains inappropriate words!\nWarnings: " + this.mute.getMuteCount(member) + "\nMuted: " + this.mute.isMemberMuted(member), null, "\uD83D\uDC08", Color.RED);

                            String timestamp = StringUtils.getDateAndTime("LOG");

                            HashMap<String, String> fieldHashMap = new HashMap<>();
                            fieldHashMap.put("Member", member.getAsMention());
                            fieldHashMap.put("Mute Count", String.valueOf(this.mute.getMuteCount(member)));
                            fieldHashMap.put("Muted", String.valueOf(this.mute.isMemberMuted(member)));
                            fieldHashMap.put("Detected Word", this.messageFilter.getLastBadWord());
                            fieldHashMap.put("Log file", BadWordLog.createBadWordLog(member, messageUpdateEvent.getGuild().getId(), this.messageFilter.getLastBadWord(), message, timestamp));

                            DiscordUtils.sendLoggingMessage(messageUpdateEvent.getGuild().getId(), "Inappropriate word detected!", fieldHashMap, "Timestamp: " + timestamp, messageUpdateEvent.getGuild().getTextChannels());
                        }
                    } else {
                        messageUpdateEvent.getMessage().delete().queue();
                    }
                }
            }
        }
    }
}