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
import club.psychose.luna.enums.DiscordChannels;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Arrays;
import java.util.List;

public final class DiscordChannelUtils {
    // Method to delete a complete channel History.
    public void deleteChannelHistory (String serverID, TextChannel textChannel) {
        // Creates a copy of the text channel
        TextChannel copyOfTheOriginalChannel = textChannel.createCopy().complete();

        // Saves the text channel position.
        int textChannelPosition = textChannel.getPosition();

        // Checks if the text channel is a configured channel.
        // Checks if the channel id equals the text channel id.
        Arrays.stream(DiscordChannels.values()).forEachOrdered(discordChannel -> {
            String channelID = Luna.SETTINGS_MANAGER.getServerSettings().getDiscordChannelID(serverID, discordChannel);

            if (channelID.equals(textChannel.getId())) {
                // Sets the channel id to the copy of the original text channel.
                Luna.SETTINGS_MANAGER.getServerSettings().replaceChannelConfiguration(serverID, discordChannel, copyOfTheOriginalChannel.getId());
            }
        });

        // Deletes the text channel.
        textChannel.delete().queue();

        // Sets the position.
        copyOfTheOriginalChannel.getManager().setPosition(textChannelPosition).queue();
    }

    public boolean checkIfTextChannelExistOnServer (String serverID, String textChannelID, List<Guild> guildList) {
        return guildList.stream().filter(guild -> guild.getId().equals(serverID)).flatMap(guild -> guild.getTextChannels().stream()).anyMatch(textChannel -> textChannel.getId().equals(textChannelID));
    }

    public boolean isChannelValidForTheDiscordCommand (TextChannel textChannel, String serverID, DiscordChannels[] discordChannels) {
        return ((textChannel != null) && (Luna.SETTINGS_MANAGER.getServerSettings().isValidChannel(serverID, textChannel.getId(), discordChannels)));
    }

    public TextChannel getTextChannel (String channelID, List<TextChannel> textChannelList) {
        return textChannelList.stream().filter(textChannel -> textChannel.getId().equals(channelID)).findFirst().orElse(null);
    }

    public VoiceChannel getVoiceChannel (Member member, Guild guild) {
        return guild.getVoiceChannels().stream().filter(voiceChannel -> voiceChannel.getMembers().stream().anyMatch(joinedMember -> joinedMember.getId().equals(member.getId()))).findFirst().orElse(null);
    }
}