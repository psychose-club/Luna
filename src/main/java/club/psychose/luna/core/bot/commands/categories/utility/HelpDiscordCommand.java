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

/*
 * This class provides the methods for a specific discord bot command.
 */

public final class HelpDiscordCommand extends DiscordCommand {
    // Public constructor.
    public HelpDiscordCommand () {
        super("help", "Shows the help usage of other commands!", "", new String[] {"?"}, CommandCategory.UTILITY, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    // Message reaction method.
    @Override
    public boolean onMessageReaction (DiscordCommandReaction discordCommandReaction, MessageReactionAddEvent messageReactionAddEvent) {
        // If the member is null it'll stop the method execution.
        assert messageReactionAddEvent.getMember() == null;

        // Fetches the message.
        Message message = messageReactionAddEvent.retrieveMessage().complete();

        // Checks if the message is not null.
        if (message != null) {
            // Clears the message reactions.
            message.clearReactions().queue();

            // Checks if it's the "GO_BACK_EMOJI".
            if (discordCommandReaction.getReactionEmoji().equals(Constants.GO_BACK_EMOJI.getName())) {
                // Collects the command categories.
                HashMap<String, String> commandCategoryHashMap = Arrays.stream(CommandCategory.values()).collect(Collectors.toMap(Enum::name, commandCategory -> commandCategory.getEmoji().getName(), (a, b) -> b, HashMap::new));

                // Sends the message.
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().editEmbedMessage(message, "Command - Help", "Please select a category below to see the usage of the commands.", commandCategoryHashMap, "\uD83D\uDC08 L U N A \uD83D\uDC08", Color.MAGENTA, (queueMessage) -> Arrays.stream(CommandCategory.values()).forEachOrdered(commandCategory -> this.addReaction(queueMessage.getTextChannel(), commandCategory.getEmoji().getName(), messageReactionAddEvent.getMember().getId(), queueMessage.getId(), null)));
            } else {
                // Goes through every command category.
                for (CommandCategory commandCategory : CommandCategory.values()) {
                    // Checks if the command category has the reacted emoji.
                    if (discordCommandReaction.getReactionEmoji().equals(commandCategory.getEmoji().getName())) {
                        // Collects the commands.
                        HashMap<String, String> commandCategoryHashMap = DiscordBot.COMMAND_MANAGER.getDiscordCommandsArrayList().stream().filter(discordCommand -> discordCommand.getCommandCategory().equals(commandCategory)).collect(Collectors.toMap(DiscordCommand::getSyntaxString, DiscordCommand::getCommandDescription, (a, b) -> b, HashMap::new));

                        // Sends the message.
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().editEmbedMessage(message, commandCategory.name() + " - Help", "Usages:", commandCategoryHashMap, "\uD83D\uDC08 L U N A \uD83D\uDC08", Color.MAGENTA, (queueMessage) -> this.addReaction(queueMessage.getTextChannel(), Constants.GO_BACK_EMOJI.getName(), messageReactionAddEvent.getMember().getId(), queueMessage.getId(), null));
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Command execution method.
    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Collects the command categories.
        HashMap<String, String> commandCategoryHashMap = Arrays.stream(CommandCategory.values()).collect(Collectors.toMap(Enum::name, commandCategory -> commandCategory.getEmoji().getName(), (a, b) -> b, HashMap::new));

        // Sends the message.
        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Command - Help", "Please select a category below to see the usage of the commands.", commandCategoryHashMap, "\uD83D\uDC08 L U N A \uD83D\uDC08", Color.MAGENTA, (message) -> Arrays.stream(CommandCategory.values()).forEachOrdered(commandCategory -> this.addReaction(messageReceivedEvent.getTextChannel(), commandCategory.getEmoji().getName(), messageReceivedEvent.getAuthor().getId(), message.getId(), null)));
    }
}