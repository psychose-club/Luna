package club.psychose.luna.core.logging;

import club.psychose.luna.Luna;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.HashMap;

public class BadWordLog {
    public static String createBadWordLog (Member member, String serverID, String badWord, String message, String timestamp) {

        ArrayList<String> badWordLogArrayList = new ArrayList<>();
        badWordLogArrayList.add("==================================================================================================");
        badWordLogArrayList.add("                                         Bad Word Log                                             ");
        badWordLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        badWordLogArrayList.add("File: " + Constants.getLunaFolderPath("\\logs\\detections\\" + timestamp + ".log"));
        badWordLogArrayList.add("Timestamp: " + timestamp);
        badWordLogArrayList.add("Server ID: " + serverID);
        badWordLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        badWordLogArrayList.add("Member: " + member.getAsMention());
        badWordLogArrayList.add("Bad Word: " + badWord);
        badWordLogArrayList.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        badWordLogArrayList.add("Full Message:");
        badWordLogArrayList.add(message);
        badWordLogArrayList.add("==================================================================================================");

        HashMap<String, String> fieldHashMap = new HashMap<>();
        fieldHashMap.put("Path in Luna folder", "\\logs\\detections\\" + timestamp + ".log");
        fieldHashMap.put("Timestamp: ", timestamp);
        DiscordUtils.sendBotInformationMessage(serverID, "Bad Word Log saved!", fieldHashMap, "SYSTEM", member.getGuild().getTextChannels());

        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\detections\\" + timestamp + ".log"), badWordLogArrayList);
        Luna.FILE_MANAGER.saveArrayListToAFile(Constants.getLunaFolderPath("\\logs\\detections\\latest.log"), badWordLogArrayList);

        return "\\logs\\detections\\" + timestamp + ".log";
    }
}
