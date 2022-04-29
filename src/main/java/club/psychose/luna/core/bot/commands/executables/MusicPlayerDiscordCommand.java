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

package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.musicplayer.MusicPlayer;
import club.psychose.luna.core.bot.musicplayer.youtube.YouTubeVideo;
import club.psychose.luna.core.bot.musicplayer.youtube.YoutubeSearch;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.DiscordUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;

public final class MusicPlayerDiscordCommand extends DiscordCommand {
    private final YoutubeSearch youtubeSearch = new YoutubeSearch();

    private final AudioPlayerManager audioPlayerManager;
    private int reInitializeMusicPlayerTrys = 0;
    private MusicPlayer musicPlayer = null;

    public MusicPlayerDiscordCommand () {
        super("music", "Music player for the server!", "!music <play | queue | pause | resume | skip | stop | volume> <Keywords | URL | (status | Value (0-100))>", new String[] {"m", "mp", "player"}, CommandCategory.FUN, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});

        // Initialize the audio player manager.
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        // Registers the sources.
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (arguments != null) {
            if (arguments.length >= 1) {
                String mode = arguments[0].trim();

                if (arguments.length >= 2) {
                    switch (mode) {
                        case "play" -> {
                            if (this.checkMusicPlayer(messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
                                String trackURL = arguments[1].trim();

                                boolean cancel = false;
                                if ((!(trackURL.startsWith("https://") && (trackURL.contains("youtube") || (trackURL.contains("youtu.be")) || (trackURL.contains("soundcloud")))))) {
                                    String[] keywords = Arrays.copyOfRange(arguments, 1, arguments.length);

                                    try {
                                        YouTubeVideo youTubeVideo = this.youtubeSearch.searchYouTubeVideo(keywords);

                                        if (youTubeVideo != null) {
                                            if ((youTubeVideo.getTitleURL() != null) && (youTubeVideo.getYoutubeURL() != null)) {
                                                HashMap<String, String> fieldHashMap = new HashMap<>();
                                                fieldHashMap.put("Title: ", youTubeVideo.getTitleURL());
                                                fieldHashMap.put("Identifier: ", youTubeVideo.getYoutubeURL());

                                                trackURL = "https://www.youtube.com/watch?v=" + youTubeVideo.getYoutubeURL();
                                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "We found this video:", "", fieldHashMap,"1234", Color.RED);
                                            } else {
                                                if (youTubeVideo.isLivestream()) {
                                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Please don't search for livestreams, load these directly!", "This is a sample text.", null, "1234", Color.RED);
                                                } else {
                                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Something went wrong while searching for the video!", "We notify the developers!", null, "00000", Color.RED);
                                                }

                                                cancel = true;
                                            }
                                        } else {
                                            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Video not found!", "We didn't find the YouTube video!\nPlease try another keywords!", null, "damn", Color.RED);
                                            cancel = true;
                                        }
                                    } catch (MalformedURLException malformedURLException) {
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid characters!", "YouTube didn't like special characters that much!\nPlease use no emojis or other special characters.", null, "https://www.youtube.com/watch?v=dQw4w9WgXcQ", Color.RED);
                                        cancel = true;
                                    } catch (IOException ioException) {
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Something went wrong while searching for the video!", "We notify the developers!", null, "damn", Color.RED);
                                        CrashLog.saveLogAsCrashLog(ioException, messageReceivedEvent.getJDA().getGuilds());
                                        cancel = true;
                                    }
                                }

                                if (!(cancel)) {
                                    String finalTrackURL = trackURL;
                                    this.musicPlayer.getAudioPlayerManager().loadItemOrdered(this.musicPlayer, trackURL, new AudioLoadResultHandler () {
                                        @Override
                                        public void trackLoaded (AudioTrack audioTrack) {
                                            // Initialize variable.
                                            HashMap<String, String> fieldHashMap = new HashMap<>();

                                            // FieldHashMap inputs.
                                            fieldHashMap.put("Title: ", audioTrack.getInfo().title);
                                            fieldHashMap.put("Author: ", audioTrack.getInfo().author);
                                            fieldHashMap.put("URL: ", finalTrackURL);
                                            fieldHashMap.put("Stream: ", String.valueOf(audioTrack.getInfo().isStream));
                                            fieldHashMap.put("Identifier: ", audioTrack.getInfo().identifier);

                                            // Adds the track to the queue.
                                            musicPlayer.getMusicPlayerTrackScheduler().addToQueue(audioTrack);

                                            // Sends an embed message to the text channel.
                                            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Added song to queue!", fieldHashMap, "Fetched cover from amogus by R3AP3", Color.WHITE);
                                        }

                                        @Override
                                        public void playlistLoaded (AudioPlaylist audioPlaylist) {
                                            // Initialize variable.
                                            HashMap<String, String> fieldHashMap = new HashMap<>();

                                            // FieldHashMap inputs.
                                            fieldHashMap.put("Playlist Name: ", audioPlaylist.getName());
                                            fieldHashMap.put("Playlist Songs: ", String.valueOf(audioPlaylist.getTracks().size()));
                                            fieldHashMap.put("URL: ", finalTrackURL);;

                                            // Adds the tracks to the queue.
                                            audioPlaylist.getTracks().forEach(audioTrack -> musicPlayer.getMusicPlayerTrackScheduler().addToQueue(audioTrack));

                                            // Sends an embed message to the text channel.
                                            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Added playlist to queue!", fieldHashMap, "Fetched cover from amogus by R3AP3", Color.WHITE);
                                        }

                                        @Override
                                        public void noMatches () {
                                            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Song not found :(", null, "searched whole internet and nothing found :(", Color.WHITE);
                                        }

                                        @Override
                                        public void loadFailed (FriendlyException friendlyException) {
                                            CrashLog.saveLogAsCrashLog(friendlyException, messageReceivedEvent.getJDA().getGuilds());
                                            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "An exception occurred!", null, "The developers already got a notification!", Color.RED);
                                        }
                                    });
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", null, "fox uwu", Color.RED);
                            }
                        }

                        case "volume" -> {
                            if (this.checkMusicPlayer(messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
                                String volumeString = arguments[1].trim();

                                if (volumeString.equalsIgnoreCase("status")) {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Current volume level: " + this.musicPlayer.getAudioPlayer().getVolume(), null, "catfish on the internet right now...", Color.WHITE);
                                } else {
                                    try {
                                        int volume = Integer.parseInt(volumeString);

                                        if ((volume >= 0) && (volume <= 100)) {
                                            this.musicPlayer.getAudioPlayer().setVolume(volume);
                                            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "New volume set to " + this.musicPlayer.getAudioPlayer().getVolume() + "!", null, "party", Color.WHITE);
                                        } else {
                                            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid volume number!", "Please enter a number from 0 - 100!", null, "oh no qwq", Color.RED);
                                        }
                                    } catch (NumberFormatException numberFormatException) {
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid volume number!", "Please enter a number from 0 - 100!", null, "oh no qwq", Color.RED);
                                    }
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", null, "fox uwu", Color.RED);
                            }
                        }

                        default -> DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Please check the syntax!", null, "oh no qwq", Color.RED);
                    }
                } else {
                    switch (mode) {
                        case "queue" -> {
                            if (this.checkMusicPlayer(messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
                                if (this.musicPlayer.getAudioPlayer().getPlayingTrack() != null) {
                                    // Initialize variable.
                                    HashMap<String, String> fieldHashMap = new HashMap<>();

                                    // FieldHashMap inputs.
                                    fieldHashMap.put("Songs: ", String.valueOf(this.musicPlayer.getMusicPlayerTrackScheduler().getQueue().size() + 1));
                                    fieldHashMap.put("Current Song: ", this.musicPlayer.getAudioPlayer().getPlayingTrack().getInfo().title);

                                    // Checks if the queue size is bigger or equals 1.
                                    // It's adds then another FieldHashMap input.
                                    if (this.musicPlayer.getMusicPlayerTrackScheduler().getQueue().size() >= 1)
                                        fieldHashMap.put("Next Song: ", this.musicPlayer.getMusicPlayerTrackScheduler().getQueue().get(0).getInfo().title);

                                    // Sends an embed message to the text channel.
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Queue", "Current queue:", fieldHashMap, "owo", Color.WHITE);

                                } else {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Queue", "No song is currently playing!", null, "fox uwu", Color.WHITE);
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", null, "fox uwu", Color.RED);
                            }
                        }

                        case "pause" -> {
                            if (this.checkMusicPlayer(messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
                                if (this.musicPlayer.getAudioPlayer().getPlayingTrack() != null) {
                                    if (!(this.musicPlayer.getAudioPlayer().isPaused())) {
                                        this.musicPlayer.getAudioPlayer().setPaused(true);
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Music player paused!", null, "mp3 player!", Color.WHITE);
                                    } else {
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Music player already paused!", null, "mp3 player?", Color.WHITE);
                                    }
                                } else {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "No song is currently playing!", null, "fox uwu", Color.WHITE);
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", null, "fox uwu", Color.RED);
                            }
                        }

                        case "resume" -> {
                            if (this.checkMusicPlayer(messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
                                if (this.musicPlayer.getAudioPlayer().getPlayingTrack() != null) {
                                    if (this.musicPlayer.getAudioPlayer().isPaused()) {
                                        this.musicPlayer.getAudioPlayer().setPaused(false);
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Music player resumed!", null, "mommy :o", Color.WHITE);
                                    } else {
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Music player is not paused!", null, "visit kescher.at", Color.WHITE);
                                    }
                                } else {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "No song is currently playing!", null, "fox uwu", Color.WHITE);
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", null, "fox uwu", Color.RED);
                            }
                        }

                        case "skip" -> {
                            if (this.checkMusicPlayer(messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
                                if (this.musicPlayer.getAudioPlayer().getPlayingTrack() != null) {
                                    if (this.musicPlayer.getMusicPlayerTrackScheduler().playNextTrack()) {
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Why skipping this killer beat?\nOkay... skip!", null, "beats provided by jvlix666", Color.WHITE);
                                    } else {
                                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Can't skip the track because it's the last in the queue!", null, "sponsored by SoSBunker", Color.WHITE);
                                    }
                                } else {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "No song is currently playing!", null, "fox uwu", Color.WHITE);
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", null, "fox uwu", Color.RED);
                            }
                        }

                        case "stop" -> {
                            if (this.checkMusicPlayer(messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
                                this.musicPlayer.getMusicPlayerTrackScheduler().stopMusicBot();
                                this.musicPlayer = null;

                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Hermine stepped on the stop button!\nBot stopped :c", null, "https://damian.psychose.club/", Color.WHITE);
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", null, "fox uwu", Color.RED);
                            }
                        }

                        default -> DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Please check the syntax!", null, "oh no qwq", Color.RED);
                    }
                }
            } else {
                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", null, "oh no qwq", Color.RED);
            }
        } else {
            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", null, "oh no qwq", Color.RED);
        }
    }

    // Checks if the music player is successfully initialized.
    private boolean checkMusicPlayer (MessageReceivedEvent messageReceivedEvent, TextChannel musicChannel) {
        // Trys 3 times to reinitialize the music player.
        if (this.reInitializeMusicPlayerTrys != 3) {
            // Checks if the member is not null and if the voice state of the member is not null.
            if ((messageReceivedEvent.getMember() != null) && (messageReceivedEvent.getMember().getVoiceState() != null)) {
                // Tries to fetch the member voice channel.
                VoiceChannel memberVoiceChannel = DiscordUtils.getVoiceChannel(messageReceivedEvent.getMember(), messageReceivedEvent.getGuild());

                // Checks if the voice channel is not null.
                if (memberVoiceChannel != null) {
                    // Checks if the music player is not null.
                    if (this.musicPlayer != null) {
                        // Resets the music player music channel (For the case if the channel id was reset).
                        this.musicPlayer.getMusicPlayerTrackScheduler().resetMusicChannel(musicChannel);

                        // Checks if the bot is connected to any voice channel.
                        if (!(this.musicPlayer.getMusicPlayerTrackScheduler().isConnectedWithAnyVoiceChannel())) {
                            // Stops the music bot.
                            this.musicPlayer.getMusicPlayerTrackScheduler().stopMusicBot();

                            // Nulls the music player.
                            this.musicPlayer = null;

                            // Adds a try.
                            this.reInitializeMusicPlayerTrys ++;

                            // ReInitialize the music player.
                            return this.checkMusicPlayer(messageReceivedEvent, musicChannel);
                        }

                        // Sets the trys to 0.
                        this.reInitializeMusicPlayerTrys = 0;

                        // Returns if the music bot is in the same channel as the member.
                        return this.musicPlayer.getMusicPlayerTrackScheduler().isInSameVoiceChannel(messageReceivedEvent.getMember());
                    } else {
                        this.musicPlayer = new MusicPlayer(this.audioPlayerManager, messageReceivedEvent.getMember().getGuild().getAudioManager(), memberVoiceChannel);

                        // Resets the music player music channel (For the case if the channel id was reset).
                        this.musicPlayer.getMusicPlayerTrackScheduler().resetMusicChannel(musicChannel);

                        // Joins the voice channel.
                        this.musicPlayer.getMusicPlayerTrackScheduler().joinVoiceChannel();

                        // Sets the sending handler.
                        messageReceivedEvent.getGuild().getAudioManager().setSendingHandler(this.musicPlayer.getMusicAudioSendHandler());

                        // Returns true.
                        return true;
                    }
                }
            }
        } else {
            // Resets the trys to 0.
            this.reInitializeMusicPlayerTrys = 0;
        }

        // Returns false.
        return false;
    }
}