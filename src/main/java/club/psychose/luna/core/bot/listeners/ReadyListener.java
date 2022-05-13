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
import club.psychose.luna.utils.logging.ConsoleLogger;
import club.psychose.luna.utils.logging.CrashLog;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

/*
 * This class handles the ready event when the bot is started and connected successfully to the Discord websocket.
 */

public final class ReadyListener extends ListenerAdapter {
    // Ready event.
    @Override
    public void onReady (ReadyEvent readyEvent) {
        // Fetches all channels.
        List<TextChannel> textChannelList = readyEvent.getJDA().getTextChannels();
        List<VoiceChannel> voiceChannelList = readyEvent.getJDA().getVoiceChannels();

        // Safety call if bot already joined a voice channel it'll automatically disconnect the bot.
        voiceChannelList.stream().filter(voiceChannel -> voiceChannel.getMembers().size() != 0).forEachOrdered(voiceChannel -> voiceChannel.getMembers().stream().filter(member -> member.getId().equals(readyEvent.getJDA().getSelfUser().getId())).forEachOrdered(member -> voiceChannel.getGuild().getAudioManager().closeAudioConnection()));

        // Checks the servers.
        Luna.DISCORD_MANAGER.getDiscordBotUtils().checkServers(textChannelList);

        // Clears the temp folder.
        Luna.FILE_MANAGER.clearTempFolder();

        // We'll add all guilds that are added to the server configuration to the CrashLog class.
        readyEvent.getJDA().getGuilds().stream().filter(guild -> Luna.SETTINGS_MANAGER.getServerSettings().isGuildAdded(guild)).forEachOrdered(CrashLog::addGuild);

        // Debug stuff.
        ConsoleLogger.debug("Bot started successfully!");
        ConsoleLogger.printEmptyLine();
    }
}