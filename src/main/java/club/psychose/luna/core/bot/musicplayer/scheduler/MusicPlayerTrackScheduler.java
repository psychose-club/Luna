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

package club.psychose.luna.core.bot.musicplayer.scheduler;

import club.psychose.luna.Luna;
import club.psychose.luna.enums.FooterType;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * This class is the MusicPlayerTrackScheduler and handles the AudioPlayer.
 */

public final class MusicPlayerTrackScheduler extends AudioEventAdapter {
    private final ArrayList<AudioTrack> musicPlayerQueueArrayList = new ArrayList<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final AudioPlayer audioPlayer;
    private final AudioManager audioManager;
    private final VoiceChannel voiceChannel;
    private TextChannel textChannel = null;

    private boolean songPlayed = false;

    // Public constructor.
    public MusicPlayerTrackScheduler (AudioPlayer audioPlayer, AudioManager audioManager, VoiceChannel voiceChannel)
    {
        // Initialize variables.
        this.audioPlayer = audioPlayer;
        this.audioManager = audioManager;
        this.voiceChannel = voiceChannel;
    }

    // onTrackStart event.
    @Override
    public void onTrackStart (AudioPlayer audioPlayer, AudioTrack audioTrack) {
        if (this.songPlayed)
            this.stopTimeoutScheduler();

        // Checks if the voice channel is not null.
        if (this.voiceChannel != null) {
            // Opens the audio connection.
            this.voiceChannel.getGuild().getAudioManager().openAudioConnection(this.voiceChannel);

            // Checks if the text channel is not null.
            if (this.textChannel != null) {
                // Sends an embedded message.
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(this.textChannel, "Music Bot", "I'm playing now the following song:\n" + this.audioPlayer.getPlayingTrack().getInfo().title + "\n" + this.audioPlayer.getPlayingTrack().getInfo().author, FooterType.MUSIC, Color.WHITE);
            }
        }
    }

    // onTrackEnd event.
    @Override
    public void onTrackEnd (AudioPlayer audioPlayer, AudioTrack audioTrack, AudioTrackEndReason audioTrackEndReason) {
        // Checks if should mayStartNext.
        if (audioTrackEndReason.mayStartNext) {
            // Checks if the next track can be played.
            if (this.checkIfNextTrackCanBePlayed()) {
                // Plays the next track.
                if (this.playNextTrack()) {
                    this.stopTimeoutScheduler();
                    return;
                }
            }
        }

        // This method starts the timeout scheduler.
        this.startTimeoutScheduler();
    }

    // Adds an AudioTrack to the queue.
    public void addToQueue (AudioTrack audioTrack) {
        // Checks if the audio track don't already exists in the queue (Only the variable, songs can be multiple added).
        if (!(this.musicPlayerQueueArrayList.contains(audioTrack))) {
            // Adds the audio track.
            this.musicPlayerQueueArrayList.add(audioTrack);

            // Checks if the next track can be played.
            // If the next track can be played it'll be played.
            if (this.checkIfNextTrackCanBePlayed())
                this.playNextTrack();
        }
    }

    // Plays the next track.
    public boolean playNextTrack () {
        // Checks if the queue size is bigger than 0.
        if (this.musicPlayerQueueArrayList.size() > 0) {
            // Gets the audio track.
            AudioTrack nextAudioTrack = this.musicPlayerQueueArrayList.get(0);

            // Removes the audio track from the queue.
            this.musicPlayerQueueArrayList.remove(nextAudioTrack);

            // Plays the audio track.
            this.audioPlayer.playTrack(nextAudioTrack);

            // Returns true.
            return true;
        }

        // Returns false.
        return false;
    }

    // Joins the voice channel.
    public void joinVoiceChannel () {
        // Checks if the voice channel is not null.
        if (this.voiceChannel != null) {
            // Checks if the bot is not already connected.
            if (!(this.audioManager.isConnected())) {
                // Opens the audio connection.
                this.audioManager.openAudioConnection(this.voiceChannel);
            }
        }
    }

    // Stops the music bot.
    public void stopMusicBot () {
        // Clears the queue.
        this.musicPlayerQueueArrayList.clear();

        // Closes the audio connection.
        this.audioManager.closeAudioConnection();
    }

    // Sets the new music channel variable.
    public void resetMusicChannel (TextChannel value) {
        this.textChannel = value;
    }

    // Checks if the next track can be played.
    public boolean checkIfNextTrackCanBePlayed () {
        // Returns if the track is null or not.
        return this.audioPlayer.getPlayingTrack() == null;
    }

    // This method starts the timeout scheduler.
    private void startTimeoutScheduler () {
        // Starts the scheduler.
        this.scheduledExecutorService.schedule(() -> {
            this.songPlayed = false;
            this.stopMusicBot();
        }, 1, TimeUnit.MINUTES);

        this.songPlayed = true;
    }

    // This method stops the timeout scheduler.
    private void stopTimeoutScheduler () {
        if (!(this.scheduledExecutorService.isShutdown()))
            this.scheduledExecutorService.shutdownNow();
    }

    // Checks if the member is in the same channel as the bot.
    public boolean isInSameVoiceChannel (Member member) {
        // Returns if the voice channel is not null and if the bot is connected to a voice channel and if the member is in the same voice channel.
        return ((this.voiceChannel != null) && (this.isConnectedWithAnyVoiceChannel())) && (this.voiceChannel.getMembers().contains(member));
    }

    // This method checks if a voice channel is the right voice channel where the audio manager is opened.
    public boolean isVoiceChannelTheSaneVoiceChannel (String voiceChannelID) {
        return ((this.voiceChannel != null) && (this.isConnectedWithAnyVoiceChannel()) && (this.voiceChannel.getId().equals(voiceChannelID)));
    }

    // Checks if the bot is connected to a voice channel.
    public boolean isConnectedWithAnyVoiceChannel () {
        // Returns if the bot is connected and if the connected channel is not null.
        return ((this.audioManager.isConnected()) && (this.audioManager.getConnectedChannel() != null));
    }

    // Returns the queue.
    public ArrayList<AudioTrack> getQueue () {
        return this.musicPlayerQueueArrayList;
    }
}