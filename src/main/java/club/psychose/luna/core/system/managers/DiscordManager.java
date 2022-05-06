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

package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.bot.schedulers.ReactionScheduler;
import club.psychose.luna.core.bot.utils.DiscordChannelUtils;
import club.psychose.luna.core.bot.utils.DiscordMemberUtils;
import club.psychose.luna.core.bot.utils.DiscordMessageUtils;
import club.psychose.luna.core.bot.utils.DiscordBotUtils;
import club.psychose.luna.core.bot.utils.builder.message.DiscordMessageBuilder;
import club.psychose.luna.core.bot.utils.DiscordRoleUtils;

/*
 * This class manages the Discord utils.
 */

public final class DiscordManager {
    // These methods initialized the utils.
    private final DiscordBotUtils discordBotUtils = new DiscordBotUtils();
    private final DiscordChannelUtils discordChannelUtils = new DiscordChannelUtils();
    private final DiscordMessageBuilder discordMessageBuilder = new DiscordMessageBuilder();
    private final DiscordMessageUtils discordMessageUtils = new DiscordMessageUtils();
    private final DiscordMemberUtils discordMemberUtils = new DiscordMemberUtils();
    private final DiscordRoleUtils discordRoleUtils = new DiscordRoleUtils();
    private final ReactionScheduler reactionScheduler = new ReactionScheduler();

    // These methods return the utils.
    public DiscordBotUtils getDiscordBotUtils () {
        return this.discordBotUtils;
    }

    public DiscordChannelUtils getDiscordChannelUtils () {
        return this.discordChannelUtils;
    }

    public DiscordMessageBuilder getDiscordMessageBuilder () {
        return this.discordMessageBuilder;
    }

    public DiscordMessageUtils getDiscordMessageUtils () {
        return this.discordMessageUtils;
    }

    public DiscordMemberUtils getDiscordMemberUtils () {
        return this.discordMemberUtils;
    }

    public DiscordRoleUtils getDiscordRoleUtils () {
        return this.discordRoleUtils;
    }

    public ReactionScheduler getReactionScheduler () {
        return this.reactionScheduler;
    }
}