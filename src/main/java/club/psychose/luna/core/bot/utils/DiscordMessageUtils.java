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

package club.psychose.luna.core.bot.utils;

import club.psychose.luna.core.bot.utils.records.DiscordCommandReaction;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public final class DiscordMessageUtils {
    public void addReaction (TextChannel textChannel, String messageID, DiscordCommandReaction discordCommandReaction) {
        if (messageID != null)
            textChannel.addReactionById(messageID, discordCommandReaction.getReactionEmoji()).queue();
    }

    public List<Message> getMessageHistory (TextChannel textChannel, int messages) {
        return new ArrayList<>(textChannel.getHistory().retrievePast(messages).complete());
    }
}