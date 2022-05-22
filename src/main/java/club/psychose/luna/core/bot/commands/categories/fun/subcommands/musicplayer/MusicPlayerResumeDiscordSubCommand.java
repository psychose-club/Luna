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

package club.psychose.luna.core.bot.commands.categories.fun.subcommands.musicplayer;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordSubCommand;
import club.psychose.luna.enums.FooterType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/*
 * This is the subcommand class for the MusicPlayer.
 */

public final class MusicPlayerResumeDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public MusicPlayerResumeDiscordSubCommand () {
        super("resume", null, 0);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Initialize the music player.
        if (Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().checkMusicPlayer(messageReceivedEvent.getGuild(), messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
            // Checks if a song is currently playing.
            if (Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getDiscordMusicPlayer(messageReceivedEvent.getGuild()).getMusicPlayer().getAudioPlayer().getPlayingTrack() != null) {
                // Checks if the song is paused.
                if (Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getDiscordMusicPlayer(messageReceivedEvent.getGuild()).getMusicPlayer().getAudioPlayer().isPaused()) {
                    // Resumes the song.
                    Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getDiscordMusicPlayer(messageReceivedEvent.getGuild()).getMusicPlayer().getAudioPlayer().setPaused(false);
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Music player resumed!", FooterType.MUSIC, Color.WHITE);
                } else {
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Music player is not paused!", FooterType.MUSIC, Color.WHITE);
                }
            } else {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "No song is currently playing!", FooterType.MUSIC, Color.WHITE);
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", FooterType.ERROR, Color.RED);
        }
    }
}