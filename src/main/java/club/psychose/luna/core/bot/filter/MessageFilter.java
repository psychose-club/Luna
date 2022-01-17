package club.psychose.luna.core.bot.filter;

import club.psychose.luna.Luna;
import club.psychose.luna.core.logging.CrashLog;

import java.util.Map;

public class MessageFilter {
    private String lastBadWord = null;

    public boolean checkMessage (String message) {
        message = message.replaceAll(" ", "").trim();

        if (Luna.SETTINGS_MANAGER.getFilterSettings().getBlacklistedWords().isEmpty()) {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Blacklist is empty!"), null);
            return true;
        }

        for (Map.Entry<String, String> replaceFilterMapEntry : Luna.SETTINGS_MANAGER.getFilterSettings().getBypassDetectionHashMap().entrySet()) {
            String character = replaceFilterMapEntry.getKey();
            String replaceWith = replaceFilterMapEntry.getValue();

            if (character.equals("REPLACE")) {
                if (replaceWith.contains(" ")) {
                    String[] splitReplaceWith = replaceWith.split(" ");

                    for (String replaceCharacter : splitReplaceWith)
                        message = message.replaceAll(replaceCharacter, "");

                    continue;
                }

                message = message.replaceAll(replaceWith, "").trim();
            }
        }

        for (String whitelistedWord : Luna.SETTINGS_MANAGER.getFilterSettings().getWhitelistedWords())
            message = message.replaceAll(whitelistedWord, "").trim();

        for (Map.Entry<String, String> replaceFilterMapEntry : Luna.SETTINGS_MANAGER.getFilterSettings().getBypassDetectionHashMap().entrySet()) {
            String character = replaceFilterMapEntry.getKey();
            String replaceWith = replaceFilterMapEntry.getValue();
            String temporaryMessage = message;

            if (character.equals("REPLACE"))
                continue;

            if (replaceWith.contains(" ")) {
                String[] splitReplaceWith = replaceWith.split(" ");

                for (String replaceCharacter : splitReplaceWith)
                    temporaryMessage = message.replaceAll(replaceCharacter, "");
            } else {
                temporaryMessage = message.replaceAll(replaceWith, "").trim();
            }

            for (String blacklistedWord : Luna.SETTINGS_MANAGER.getFilterSettings().getBlacklistedWords()) {
                if (temporaryMessage.equalsIgnoreCase(blacklistedWord)) {
                    this.lastBadWord = blacklistedWord;
                    return false;
                }
            }
        }

        return true;
    }

    public String getLastBadWord () {
        return this.lastBadWord;
    }
}