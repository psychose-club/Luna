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
import club.psychose.luna.api.iloveradio.ILoveRadioStream;
import club.psychose.luna.api.youtube.YouTubeVideo;
import club.psychose.luna.core.bot.commands.DiscordSubCommand;
import club.psychose.luna.core.bot.musicplayer.MusicPlayer;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.logging.CrashLog;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;

/*
 * This is the subcommand class for the MusicPlayer.
 */

public final class MusicPlayerPlayDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public MusicPlayerPlayDiscordSubCommand () {
        super("play", new String[] {"URL | Keywords (YouTube) | Stream Name (ILoveRadio)"}, 1);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Initialize the music player.
        if (Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().checkMusicPlayer(messageReceivedEvent.getGuild(), messageReceivedEvent, messageReceivedEvent.getTextChannel())) {
            String trackURL = arguments[0].trim();
            boolean cancel = false;

            // Checks if the track starts with an url or not.
            if (!(trackURL.startsWith("https://"))) {
                try {
                    // If not it'll check if the argument is an ILoveRadio stream name.
                    boolean isILoveRadioStream = false;

                    for (ILoveRadioStream iLoveRadioStream : Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getILoveRadioFetcher().getILoveRadioStreamsArrayList()) {
                        if (trackURL.equalsIgnoreCase(iLoveRadioStream.getStreamName())) {
                            trackURL = iLoveRadioStream.getStreamURL();
                            isILoveRadioStream = true;
                            break;
                        }
                    }

                    // If it's not an ILoveRadio stream it'll parse the keywords for a YouTube search.
                    if (!(isILoveRadioStream)) {
                        // Parses the keywords.
                        String[] keywords = Arrays.copyOfRange(arguments, 1, arguments.length);
                        // Gets the YouTube video.
                        YouTubeVideo youTubeVideo = Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getYoutubeSearch().searchYouTubeVideo(keywords);

                        // Builds the needed message.
                        if (youTubeVideo != null) {
                            if ((youTubeVideo.getTitleURL() != null) && (youTubeVideo.getYoutubeURL() != null)) {
                                HashMap<String, String> fieldHashMap = new HashMap<>();
                                fieldHashMap.put("Title: ", youTubeVideo.getTitleURL());
                                fieldHashMap.put("Identifier: ", youTubeVideo.getYoutubeURL());

                                trackURL = "https://www.youtube.com/watch?v=" + youTubeVideo.getYoutubeURL();
                                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "We found this video:", "", fieldHashMap, FooterType.ERROR, Color.RED);
                            } else {
                                if (youTubeVideo.isLivestream()) {
                                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Please don't search for livestreams, load these directly!", "This is a sample text.", FooterType.ERROR, Color.RED);
                                } else {
                                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Something went wrong while searching for the video!", "We notify the developers!", FooterType.ERROR, Color.RED);
                                }

                                cancel = true;
                            }
                        } else {
                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Video not found!", "We didn't find the YouTube video!\nPlease try another keywords!", FooterType.ERROR, Color.RED);
                            cancel = true;
                        }
                    }
                } catch (MalformedURLException malformedURLException) {
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid characters!", "YouTube didn't like special characters that much!\nPlease use no emojis or other special characters.", FooterType.ERROR, Color.RED);
                    cancel = true;
                } catch (IOException ioException) {
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Something went wrong while searching for the video!", "We notify the developers!", FooterType.ERROR, Color.RED);
                    CrashLog.saveLogAsCrashLog(ioException);
                    cancel = true;
                }
            }

            // If the playback is not cancelled it'll start to load the song.
            if (!(cancel)) {
                MusicPlayer musicPlayer = Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getDiscordMusicPlayer(messageReceivedEvent.getGuild()).getMusicPlayer();
                String finalTrackURL = trackURL;

                musicPlayer.getAudioPlayerManager().loadItemOrdered(musicPlayer, trackURL, new AudioLoadResultHandler() {
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
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Added song to queue!", fieldHashMap, FooterType.MUSIC, Color.WHITE);
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
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Added playlist to queue!", fieldHashMap, FooterType.MUSIC, Color.WHITE);
                    }

                    @Override
                    public void noMatches () {
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "Song not found :(", FooterType.MUSIC, Color.WHITE);
                    }

                    @Override
                    public void loadFailed (FriendlyException friendlyException) {
                        CrashLog.saveLogAsCrashLog(friendlyException);
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Music Bot", "An exception occurred!", FooterType.ERROR, Color.RED);
                    }
                });
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Forbidden action!", "You need to be in a voice channel or make sure that the bot is not currently in another voice channel!", FooterType.ERROR, Color.RED);
        }
    }
}