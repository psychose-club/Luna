package club.psychose.luna.core.bot.musicplayer.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

/**
 ** This is the MusicAudioSendHandler.
 ** It's necessary since JDA 4+
 **/

public record MusicAudioSendHandler (AudioPlayer audioPlayer) implements AudioSendHandler {
    // Initialize last audio frame.
    private static AudioFrame lastAudioFrame = null;

    // Returns if the audio player provides something.
    @Override
    public boolean canProvide () {
        // Provides the last audio frame (Prevents frame loses).
        lastAudioFrame = this.audioPlayer.provide();

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