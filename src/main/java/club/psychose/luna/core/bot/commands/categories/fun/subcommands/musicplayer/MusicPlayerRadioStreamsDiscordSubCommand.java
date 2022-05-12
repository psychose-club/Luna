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
import java.util.stream.Collectors;

/*
 * This is the subcommand class for the MusicPlayer.
 */

public final class MusicPlayerRadioStreamsDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public MusicPlayerRadioStreamsDiscordSubCommand () {
        super("radio", null, 0);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Fetches the streams and sends it as a message.
        if (Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getILoveRadioFetcher().fetchStreams()) {
            String radioStreamsList = Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getILoveRadioFetcher().getILoveRadioStreamsArrayList().stream().map(radioStream -> radioStream.getStreamName() + "\n").collect(Collectors.joining("", "```bash\n", "```"));
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendTextChannelMessage(messageReceivedEvent.getTextChannel(), radioStreamsList, "\n", true);
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Streams not fetched!", "Something went wrong while fetching the streams!", FooterType.ERROR, Color.RED);
        }
    }
}