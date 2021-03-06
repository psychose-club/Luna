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
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * This class handles the bad word logging.
 */

public final class BadWordLog {
    // This method saves the log to a txt file.
    public static String createBadWordLog (Member member, String serverID, String badWord, String message, String timestamp) {
        ArrayList<String> badWordLogArrayList = new ArrayList<>();

        badWordLogArrayList.add("==================================================================================================");
        badWordLogArrayList.add("                                         Bad Word Log                                             ");
        badWordLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        badWordLogArrayList.add("File: " + Constants.getLunaFolderPath("\\logs\\detections\\" + timestamp + ".txt"));
        badWordLogArrayList.add("Timestamp: " + timestamp);
        badWordLogArrayList.add("Server ID: " + serverID);
        badWordLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        badWordLogArrayList.add("Member Name: " + member.getEffectiveName());
        badWordLogArrayList.add("Member ID: " + member.getId());
        badWordLogArrayList.add("Member (As mention): " + member.getAsMention());
        badWordLogArrayList.add("Bad Word: " + badWord);
        badWordLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        badWordLogArrayList.add("Full Message:");
        badWordLogArrayList.add(message);
        badWordLogArrayList.add("==================================================================================================");

        HashMap<String, String> fieldHashMap = new HashMap<>();
        fieldHashMap.put("Path in Luna folder", "\\logs\\detections\\" + timestamp + ".txt");
        fieldHashMap.put("Timestamp: ", timestamp);
        Luna.DISCORD_MANAGER.getDiscordBotUtils().sendBotInformationMessage(serverID, "Bad Word Log saved!", fieldHashMap, Color.RED, member.getGuild().getTextChannels());

        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\detections\\" + timestamp + ".txt"), badWordLogArrayList);
        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\detections\\latest.txt"), badWordLogArrayList);

        return "\\logs\\detections\\" + timestamp + ".txt";
    }
}
