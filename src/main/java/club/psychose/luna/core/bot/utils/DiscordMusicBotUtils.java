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

import club.psychose.luna.Luna;
import club.psychose.luna.api.iloveradio.ILoveRadioFetcher;
import club.psychose.luna.api.youtube.YoutubeSearch;
import club.psychose.luna.core.bot.musicplayer.MusicPlayer;
import club.psychose.luna.core.bot.utils.records.DiscordMusicPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;

/*
 * This class provides the utils for the Discord music bot.
 */

public final class DiscordMusicBotUtils {
    // Initialize the methods.
    private final HashMap<Guild, DiscordMusicPlayer> discordMusicPlayerHashMap = new HashMap<>();
    private final ILoveRadioFetcher iLoveRadioFetcher = new ILoveRadioFetcher();
    private final YoutubeSearch youtubeSearch = new YoutubeSearch();

    // This method registers a music player for a guild.
    public void registerMusicPlayer (Guild guild) {
        if (!(this.discordMusicPlayerHashMap.containsKey(guild))) {
            AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

            AudioSourceManagers.registerRemoteSources(audioPlayerManager);
            AudioSourceManagers.registerLocalSource(audioPlayerManager);

            this.discordMusicPlayerHashMap.put(guild, new DiscordMusicPlayer(audioPlayerManager, null));
        }
    }

    // This method replaces the DiscordMusicPlayer from a guild.
    public void replaceDiscordMusicPlayer (Guild guild, DiscordMusicPlayer discordMusicPlayer) {
        this.discordMusicPlayerHashMap.replace(guild, discordMusicPlayer);
    }

    // This method checks if the music player is successfully initialized.
    public boolean checkMusicPlayer (Guild guild, MessageReceivedEvent messageReceivedEvent, TextChannel musicChannel) {
        // Checks if the member, voice state and the guild is not null.
        if ((messageReceivedEvent.getMember() != null) && (messageReceivedEvent.getMember().getVoiceState() != null) && (guild != null)) {
            // Here we'll check if the music player exist for the guild and if not it'll register one.
            if (!(this.isMusicPlayerExistForGuild(guild)))
                this.registerMusicPlayer(guild);

            // We'll fetch here the music player.
            DiscordMusicPlayer discordMusicPlayer = this.getDiscordMusicPlayer(guild);
            MusicPlayer musicPlayer = discordMusicPlayer.getMusicPlayer();

            // Tries to fetch the member voice channel.
            VoiceChannel memberVoiceChannel = Luna.DISCORD_MANAGER.getDiscordChannelUtils().getVoiceChannel(messageReceivedEvent.getMember(), guild);

            // The music player has three initialization trys.
            for (int musicPlayerInitialization = 0; musicPlayerInitialization < 3; musicPlayerInitialization ++) {
                // Checks if the voice channel is not null.
                if (memberVoiceChannel != null) {
                    // Checks if the music player is not null.
                    if (musicPlayer != null) {
                        // Resets the music player music channel (For the case if the channel id was reset).
                        musicPlayer.getMusicPlayerTrackScheduler().resetMusicChannel(musicChannel);

                        // Checks if the bot is connected to any voice channel.
                        if (!(musicPlayer.getMusicPlayerTrackScheduler().isConnectedWithAnyVoiceChannel())) {
                            // Stops the music bot.
                            musicPlayer.getMusicPlayerTrackScheduler().stopMusicBot();

                            // Nulls the music player.
                            musicPlayer = null;

                            // Creates the DiscordMusicPlayer.
                            discordMusicPlayer = new DiscordMusicPlayer(discordMusicPlayer.getAudioPlayerManager(), musicPlayer);

                            // Replaces the DiscordMusicPlayer.
                            this.replaceDiscordMusicPlayer(guild, discordMusicPlayer);

                            // ReInitialize the music player.
                            return this.checkMusicPlayer(guild, messageReceivedEvent, musicChannel);
                        }

                        // Returns if the music bot is in the same channel as the member.
                        return musicPlayer.getMusicPlayerTrackScheduler().isInSameVoiceChannel(messageReceivedEvent.getMember());
                    } else {
                        // Creates the MusicPlayer.
                        musicPlayer = new MusicPlayer(discordMusicPlayer.getAudioPlayerManager(), messageReceivedEvent.getMember().getGuild().getAudioManager(), memberVoiceChannel);

                        // Resets the music player music channel (For the case if the channel id was reset).
                        musicPlayer.getMusicPlayerTrackScheduler().resetMusicChannel(musicChannel);

                        // Joins the voice channel.
                        musicPlayer.getMusicPlayerTrackScheduler().joinVoiceChannel();

                        // Sets the sending handler.
                        messageReceivedEvent.getGuild().getAudioManager().setSendingHandler(musicPlayer.getMusicAudioSendHandler());

                        // Creates the DiscordMusicPlayer.
                        discordMusicPlayer = new DiscordMusicPlayer(discordMusicPlayer.getAudioPlayerManager(), musicPlayer);

                        // ReInitialize the music player.
                        this.replaceDiscordMusicPlayer(guild, discordMusicPlayer);

                        // Returns true.
                        return true;
                    }
                }
            }
        }

        // Returns false.
        return false;
    }

    // This method checks if the music player exists for a specific guild.
    public boolean isMusicPlayerExistForGuild (Guild guild) {
        return this.discordMusicPlayerHashMap.containsKey(guild);
    }

    // This method returns a DiscordMusicPlayer from a guild.
    public DiscordMusicPlayer getDiscordMusicPlayer (Guild guild) {
        return this.discordMusicPlayerHashMap.getOrDefault(guild, null);
    }

    // This method returns the ILoveRadioFetcher.
    public ILoveRadioFetcher getILoveRadioFetcher () {
        return this.iLoveRadioFetcher;
    }

    // This method returns the YouTube search function.
    public YoutubeSearch getYoutubeSearch () {
        return this.youtubeSearch;
    }
}