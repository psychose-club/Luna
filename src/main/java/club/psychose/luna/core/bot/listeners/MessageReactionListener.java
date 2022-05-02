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

import club.psychose.luna.core.bot.DiscordBot;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class MessageReactionListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd (MessageReactionAddEvent messageReactionAddEvent) {
        assert messageReactionAddEvent.getMember() == null;

        if (!(messageReactionAddEvent.getMember().getUser().isBot()))
            DiscordBot.COMMAND_MANAGER.getDiscordCommandsArrayList().forEach(discordCommand -> discordCommand.getDiscordCommandReactionArrayList().stream().filter(discordCommandReaction -> discordCommandReaction.getMemberID().equals(messageReactionAddEvent.getMember().getId())).filter(discordCommandReaction -> discordCommandReaction.getMessageID().equals(messageReactionAddEvent.getReaction().getMessageId())).forEachOrdered(discordCommandReaction -> discordCommand.onMessageReaction(discordCommandReaction, messageReactionAddEvent)));
    }
}