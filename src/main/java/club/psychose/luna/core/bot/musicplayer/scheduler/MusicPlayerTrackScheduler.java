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

/*
 * This class is the MusicPlayerTrackScheduler and handles the AudioPlayer.
 */

public final class MusicPlayerTrackScheduler extends AudioEventAdapter {
    private final ArrayList<AudioTrack> musicPlayerQueueArrayList = new ArrayList<>();
    private final AudioPlayer audioPlayer;
    private final AudioManager audioManager;
    private final VoiceChannel voiceChannel;
    private TextChannel textChannel = null;

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
                this.playNextTrack();
            }
        }
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

    // Checks if the member is in the same channel as the bot.
    public boolean isInSameVoiceChannel (Member member) {
        // Returns if the voice channel is not null and if the bot is connected to a voice channel and if the member is in the same voice channel.
        return ((this.voiceChannel != null) && (this.isConnectedWithAnyVoiceChannel())) && (this.voiceChannel.getMembers().contains(member));
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