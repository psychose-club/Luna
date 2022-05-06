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
import club.psychose.luna.core.bot.utils.records.DiscordCommandReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/*
 * This class handles all message reaction on a Discord server.
 */

public final class MessageReactionListener extends ListenerAdapter {
    // Reaction add event.
    @Override
    public void onMessageReactionAdd (MessageReactionAddEvent messageReactionAddEvent) {
        // If the member is null it'll stop the method execution.
        assert messageReactionAddEvent.getMember() == null;

        // Checks if the message is not sent by a bot.
        if (!(messageReactionAddEvent.getMember().getUser().isBot())) {
            // Discord command to remove the member reactions.
            DiscordCommand removeMemberReactionsDiscordCommand = null;

            // Fetch all discord commands.
            for (DiscordCommand discordCommand : DiscordBot.COMMAND_MANAGER.getDiscordCommandsArrayList()) {
                // Fetch all available command reactions from a command.
                for (DiscordCommandReaction discordCommandReaction : Luna.DISCORD_MANAGER.getReactionScheduler().getDiscordCommandReactionArrayList(discordCommand)) {
                    // Checks if the reaction is not null.
                    if (discordCommandReaction != null) {
                        // Checks if the reaction contains the member.
                        if (discordCommandReaction.getMemberID().equals(messageReactionAddEvent.getMember().getId())) {
                            // Checks if the reaction is the message.
                            if (discordCommandReaction.getMessageID().equals(messageReactionAddEvent.getReaction().getMessageId())) {
                                // Prevents crashing of custom emojis.
                                try {
                                    // Checks if the reaction emoji is added to the command.
                                    if (discordCommandReaction.getReactionEmoji().equals(messageReactionAddEvent.getReaction().getReactionEmote().getEmoji())) {
                                        // Executes the message reaction.
                                        if (discordCommand.onMessageReaction(discordCommandReaction, messageReactionAddEvent)) {
                                            removeMemberReactionsDiscordCommand = discordCommand;
                                            break;
                                        }
                                    }
                                } catch (IllegalStateException ignored) {}
                            }
                        }
                    }
                }

                // When the method was executed it'll break the loop.
                if (removeMemberReactionsDiscordCommand != null)
                    break;
            }

            // Removes the member reactions from the command.
            // New reactions shouldn't be possible from the user before the remove because mostly the editEmbedMessage method is used, and it'll wait from discord for a response.
            // If I see this false please create an issue. (Hopefully it isn't false)
            if (removeMemberReactionsDiscordCommand != null)
                Luna.DISCORD_MANAGER.getReactionScheduler().removeMemberReactions(removeMemberReactionsDiscordCommand, messageReactionAddEvent.getMember().getId(), messageReactionAddEvent.getMessageId());
        }
    }
}