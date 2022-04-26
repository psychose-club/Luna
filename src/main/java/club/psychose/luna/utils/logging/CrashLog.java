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
import club.psychose.luna.utils.DiscordUtils;
import club.psychose.luna.utils.StringUtils;
import club.psychose.luna.utils.logging.ConsoleLogger;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class CrashLog {
    public static void saveLogAsCrashLog (Exception exception, List<Guild> guildList) {
        ArrayList<String> crashLogArrayList = new ArrayList<>();

        String timestamp = StringUtils.getDateAndTime("LOG");
        String cutTimeStamp = timestamp.replaceAll(" ", "").trim();

        crashLogArrayList.add("==================================================================================================");
        crashLogArrayList.add("                                            Crash Log                                             ");
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("File: " + Constants.getLunaFolderPath("\\logs\\crashes\\" + cutTimeStamp + ".txt"));
        crashLogArrayList.add("Timestamp: " + timestamp);
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("Exception Cause: " + exception.getCause());
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("PrintStackTrace:");
        crashLogArrayList.add(ExceptionUtils.getStackTrace(exception));
        crashLogArrayList.add("==================================================================================================");

        if (guildList != null) {
            HashMap<String, String> fieldHashMap = new HashMap<>();
            fieldHashMap.put("Path in Luna folder", "\\logs\\crashes\\" + cutTimeStamp + ".txt");
            fieldHashMap.put("Timestamp: ", timestamp);

            guildList.forEach(guild -> DiscordUtils.sendBotInformationMessage(guild.getId(), "Oh no!\nA crash occurred!\nPlease contact CrashedLife!", fieldHashMap, "SYSTEM", guild.getTextChannels()));
        }

        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\crashes\\" + cutTimeStamp + ".txt"), crashLogArrayList);
        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\crashes\\latest.txt"), crashLogArrayList);

        ConsoleLogger.debug("ERROR: An exception occurred! Crash Log under: " + Constants.getLunaFolderPath("\\logs\\crashes\\" + cutTimeStamp + ".txt"));
    }
}