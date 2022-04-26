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

package club.psychose.luna.core.bot.musicplayer;

import club.psychose.luna.core.bot.musicplayer.handlers.MusicAudioSendHandler;
import club.psychose.luna.core.bot.musicplayer.scheduler.MusicPlayerTrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public final class MusicPlayer {
    private final AudioPlayer audioPlayer;
    private final AudioPlayerManager audioPlayerManager;
    private final MusicAudioSendHandler musicAudioSendHandler;
    private final MusicPlayerTrackScheduler musicPlayerTrackScheduler;

    public MusicPlayer (AudioPlayerManager audioPlayerManager, AudioManager audioManager, VoiceChannel voiceChannel) {
        // Initialize audio player manager.
        this.audioPlayerManager = audioPlayerManager;

        // Creates the audio player.
        this.audioPlayer = this.audioPlayerManager.createPlayer();

        // Creates the scheduler.
        this.musicPlayerTrackScheduler = new MusicPlayerTrackScheduler(this.audioPlayer, audioManager, voiceChannel);

        // Adds the scheduler to the audio player listener.
        this.audioPlayer.addListener(this.musicPlayerTrackScheduler);

        // Initialize the audio send handler.
        this.musicAudioSendHandler = new MusicAudioSendHandler(this.audioPlayer);
    }

    // Returns the audio player.
    public AudioPlayer getAudioPlayer () {
        return this.audioPlayer;
    }

    // Returns the audio player manager.
    public AudioPlayerManager getAudioPlayerManager () {
        return this.audioPlayerManager;
    }

    // Returns the scheduler.
    public MusicPlayerTrackScheduler getMusicPlayerTrackScheduler () {
        return this.musicPlayerTrackScheduler;
    }

    // Returns the audio send handler.
    public MusicAudioSendHandler getMusicAudioSendHandler () {
        return this.musicAudioSendHandler;
    }
}