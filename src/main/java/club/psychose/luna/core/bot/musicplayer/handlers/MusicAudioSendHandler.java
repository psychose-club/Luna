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

package club.psychose.luna.core.bot.musicplayer.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

/*
 * This is the MusicAudioSendHandler.
 * It's necessary since JDA 4+
 */

public record MusicAudioSendHandler (AudioPlayer getAudioPlayer) implements AudioSendHandler {
    // Initialize last audio frame.
    private static AudioFrame lastAudioFrame = null;

    // Returns if the audio player provides something.
    @Override
    public boolean canProvide () {
        // Provides the last audio frame (Prevents frame loses).
        lastAudioFrame = this.getAudioPlayer.provide();

        // Returns the state.
        return lastAudioFrame != null;
    }

    // Returns if the audio player provides 20ms audio.
    @Override
    public ByteBuffer provide20MsAudio () {
        return ByteBuffer.wrap(lastAudioFrame.getData());
    }

    // Returns that Opus is used.
    @Override
    public boolean isOpus () {
        return true;
    }
}