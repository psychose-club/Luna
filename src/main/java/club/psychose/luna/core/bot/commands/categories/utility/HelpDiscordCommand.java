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

package club.psychose.luna.core.bot.commands.categories.utility;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.utils.records.DiscordCommandReaction;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.Constants;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public final class HelpDiscordCommand extends DiscordCommand {
    public HelpDiscordCommand () {
        super("help", "Shows the help usage of other commands!", "", new String[] {"?"}, CommandCategory.UTILITY, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public boolean onMessageReaction (DiscordCommandReaction discordCommandReaction, MessageReactionAddEvent messageReactionAddEvent) {
        assert messageReactionAddEvent.getMember() == null;

        Message message = messageReactionAddEvent.retrieveMessage().complete();

        if (message != null) {
            message.clearReactions().queue();

            if (discordCommandReaction.getReactionEmoji().equals(Constants.GO_BACK_EMOJI.getName())) {
                HashMap<String, String> commandCategoryHashMap = Arrays.stream(CommandCategory.values()).collect(Collectors.toMap(Enum::name, commandCategory -> commandCategory.getEmoji().getName(), (a, b) -> b, HashMap::new));

                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().editEmbedMessage(message, "Command - Help", "Please select a category below to see the usage of the commands.", commandCategoryHashMap, "\uD83D\uDC08 L U N A \uD83D\uDC08", Color.MAGENTA, (queueMessage) -> Arrays.stream(CommandCategory.values()).forEachOrdered(commandCategory -> this.addReaction(queueMessage.getTextChannel(), commandCategory.getEmoji().getName(), messageReactionAddEvent.getMember().getId(), queueMessage.getId(), null)));
            } else {
                for (CommandCategory commandCategory : CommandCategory.values()) {
                    if (discordCommandReaction.getReactionEmoji().equals(commandCategory.getEmoji().getName())) {
                        HashMap<String, String> commandCategoryHashMap = DiscordBot.COMMAND_MANAGER.getDiscordCommandsArrayList().stream().filter(discordCommand -> discordCommand.getCommandCategory().equals(commandCategory)).collect(Collectors.toMap(DiscordCommand::getSyntaxString, DiscordCommand::getCommandDescription, (a, b) -> b, HashMap::new));

                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().editEmbedMessage(message, commandCategory.name() + " - Help", "Usages:", commandCategoryHashMap, "\uD83D\uDC08 L U N A \uD83D\uDC08", Color.MAGENTA, (queueMessage) -> this.addReaction(queueMessage.getTextChannel(), Constants.GO_BACK_EMOJI.getName(), messageReactionAddEvent.getMember().getId(), queueMessage.getId(), null));
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        HashMap<String, String> commandCategoryHashMap = Arrays.stream(CommandCategory.values()).collect(Collectors.toMap(Enum::name, commandCategory -> commandCategory.getEmoji().getName(), (a, b) -> b, HashMap::new));
        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Command - Help", "Please select a category below to see the usage of the commands.", commandCategoryHashMap, "\uD83D\uDC08 L U N A \uD83D\uDC08", Color.MAGENTA, (message) -> {
            Arrays.stream(CommandCategory.values()).forEachOrdered(commandCategory -> this.addReaction(messageReceivedEvent.getTextChannel(), commandCategory.getEmoji().getName(), messageReceivedEvent.getAuthor().getId(), message.getId(), null));
        });
    }
}