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
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.NukeLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public final class NukeDiscordCommand extends DiscordCommand {
    public NukeDiscordCommand () {
        super("nuke", "Deletes the complete channel history from a channel.", "", new String[] {"clear"}, CommandCategory.UTILITY, new PermissionRoles[] {PermissionRoles.OWNER, PermissionRoles.ADMIN, PermissionRoles.MODERATOR}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getMember() != null) {
            NukeLog.saveNukeLog(messageReceivedEvent.getTextChannel(), messageReceivedEvent.getMember());

            messageReceivedEvent.getTextChannel().sendMessage("Channel hit by a nuke :(\nhttps://media.giphy.com/media/3o7abwbzKeaRksvVaE/giphy.gif").queue();

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException ignored) {}

            Luna.DISCORD_MANAGER.getDiscordChannelUtils().deleteChannelHistory(messageReceivedEvent.getGuild().getId(), messageReceivedEvent.getTextChannel());
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Member not found!"), messageReceivedEvent.getJDA().getGuilds());
        }
    }
}