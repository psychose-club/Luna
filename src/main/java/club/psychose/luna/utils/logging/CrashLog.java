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
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * This class handles the crash logging.
 */

public final class CrashLog {
    // This method saves the exception to a txt file.
    public static void saveLogAsCrashLog (Exception exception, List<Guild> guildList) {
        ArrayList<String> crashLogArrayList = new ArrayList<>();

        String timestamp = StringUtils.getDateAndTime("LOG");

        crashLogArrayList.add("==================================================================================================");
        crashLogArrayList.add("                                            Crash Log                                             ");
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("File: " + Constants.getLunaFolderPath("\\logs\\crashes\\" + timestamp + ".txt"));
        crashLogArrayList.add("Timestamp: " + timestamp);
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("Exception Cause: " + exception.getCause());
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("PrintStackTrace:");
        crashLogArrayList.add(ExceptionUtils.getStackTrace(exception));
        crashLogArrayList.add("==================================================================================================");

        if (guildList != null) {
            HashMap<String, String> fieldHashMap = new HashMap<>();
            fieldHashMap.put("Path in Luna folder", "\\logs\\crashes\\" + timestamp + ".txt");
            fieldHashMap.put("Timestamp: ", timestamp);

            guildList.forEach(guild -> Luna.DISCORD_MANAGER.getDiscordBotUtils().sendBotInformationMessage(guild.getId(), "Oh no!\nA crash occurred!\nPlease create on GitHub an issue!", fieldHashMap, Color.ORANGE, guild.getTextChannels()));
        }

        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\crashes\\" + timestamp + ".txt"), crashLogArrayList);
        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\crashes\\latest.txt"), crashLogArrayList);

        ConsoleLogger.debug("ERROR: An exception occurred! Crash Log under: " + Constants.getLunaFolderPath("\\logs\\crashes\\" + timestamp + ".txt"));
    }
}