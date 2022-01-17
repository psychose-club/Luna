package club.psychose.luna.core.logging;

import club.psychose.luna.Luna;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.DiscordUtils;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrashLog {
    public static void saveLogAsCrashLog (Exception exception, List<Guild> guildList) {
        String timestamp = StringUtils.getDateAndTime("LOG");

        ArrayList<String> crashLogArrayList = new ArrayList<>();
        crashLogArrayList.add("==================================================================================================");
        crashLogArrayList.add("                                            Crash Log                                             ");
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("File: " + Constants.getLunaFolderPath("\\logs\\crash\\" + timestamp + ".log"));
        crashLogArrayList.add("Timestamp: " + timestamp);
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("Exception Cause: " + exception.getCause());
        crashLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        crashLogArrayList.add("PrintStackTrace:");
        crashLogArrayList.add(ExceptionUtils.getStackTrace(exception));
        crashLogArrayList.add("==================================================================================================");

        if (guildList != null) {
            HashMap<String, String> fieldHashMap = new HashMap<>();
            fieldHashMap.put("Path in Luna folder", "\\logs\\crash\\" + timestamp + ".log");
            fieldHashMap.put("Timestamp: ", timestamp);

            guildList.forEach(guild -> DiscordUtils.sendBotInformationMessage(guild.getId(), "Oh no!\nA crash occurred!\nPlease contact CrashedLife!", fieldHashMap, "SYSTEM", guild.getTextChannels()));
        }

        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\crash\\" + timestamp + ".log"), crashLogArrayList);
        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\crash\\latest.log"), crashLogArrayList);

        StringUtils.debug("ERROR: An exception occurred! Crash Log under: " + Constants.getLunaFolderPath("\\logs\\crash\\" + timestamp + ".log"));
    }
}