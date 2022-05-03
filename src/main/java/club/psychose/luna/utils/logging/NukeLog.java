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

package club.psychose.luna.utils.logging;

import club.psychose.luna.Luna;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class NukeLog {
    public static void saveNukeLog (TextChannel textChannel, Member member) {
        ArrayList<String> messageHistoryArrayList = new ArrayList<>();
        List<Message> messageHistoryList = Luna.DISCORD_MANAGER.getDiscordMessageUtils().getMessageHistory(textChannel, 100);

        String channelName = textChannel.getName();
        String channelTopic = textChannel.getTopic();
        boolean channelNSFW = textChannel.isNSFW();
        int channelPosition = textChannel.getPosition();
        int channelSlowMode = textChannel.getSlowmode();

        String timestamp = StringUtils.getDateAndTime("LOG");

        messageHistoryArrayList.add("==================================================================================================");
        messageHistoryArrayList.add("                                             Nuke Log                                             ");
        messageHistoryArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        messageHistoryArrayList.add("File: " + Constants.getLunaFolderPath("\\logs\\nukes\\" + timestamp + ".txt"));
        messageHistoryArrayList.add("Channel name: " + channelName);
        messageHistoryArrayList.add("Channel topic: " + channelTopic);
        messageHistoryArrayList.add("Channel position: " + channelPosition);
        messageHistoryArrayList.add(channelNSFW ? "Channel NSFW: true" : "Channel NSFW: false");
        messageHistoryArrayList.add((channelSlowMode != 0) ? ("Channel Slowmode: true (" + channelSlowMode + ")") : "Channel Slowmode: false");
        messageHistoryArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        messageHistoryArrayList.add("Timestamp: " + timestamp);
        messageHistoryArrayList.add("Channel nuked by " + member.getEffectiveName() + "!");
        messageHistoryArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        messageHistoryArrayList.add("Channel history (latest 100 messages): \n\n");
        messageHistoryList.stream().map(message -> "[" + message.getTimeCreated() + "] " + message.getAuthor().getName() + " : " + message.getAuthor().getId() + " : " + message.getAuthor().getAsMention() + " > " + message.getContentRaw()).forEach(messageHistoryArrayList::add);
        messageHistoryArrayList.add("==================================================================================================");

        ConsoleLogger.debug(member.getEffectiveName() + " nuked -> " + channelName);

        HashMap<String, String> fieldHashMap = new HashMap<>();
        fieldHashMap.put("Path in Luna folder", "\\logs\\nukes\\" + timestamp + ".txt");
        fieldHashMap.put("Timestamp: ", timestamp);

        Luna.DISCORD_MANAGER.getDiscordBotUtils().sendBotInformationMessage(textChannel.getGuild().getId(), channelName + " nuked by " + member.getAsMention() + "!", fieldHashMap, Color.RED, textChannel.getGuild().getTextChannels());

        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\nukes\\" + timestamp + ".txt"), messageHistoryArrayList);
        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\nukes\\latest.txt"), messageHistoryArrayList);
    }
}