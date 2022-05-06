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

package club.psychose.luna.core.bot.filter;

import club.psychose.luna.Luna;
import club.psychose.luna.utils.logging.CrashLog;

import java.util.Map;

/*
 * This classes filters the message.
 */

public final class MessageFilter {
    // Saves the last blacklisted word.
    private String lastBadWord = null;

    // This method checks the message.
    public boolean checkMessage (String message) {
        // Replace all spaces to prevent bypasses.
        message = message.replaceAll(" ", "").trim();

        // Throws an exception if the blacklist is empty.
        if (Luna.SETTINGS_MANAGER.getFilterSettings().getBlacklistedWords().isEmpty()) {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Blacklist is empty!"), null);
            return true;
        }

        // This method checks if a specific message has a special character and replace it with the right character to prevent word bypasses.
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

        // Checks if the word is whitelisted, if it's whitelisted it'll remove the word from the message.
        for (String whitelistedWord : Luna.SETTINGS_MANAGER.getFilterSettings().getWhitelistedWords())
            message = message.replaceAll(whitelistedWord, "").trim();

        // Wait why is this duplicated here. IDK it's work so yeah. Will check this later.
        // TODO: Check why this exist.
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

            // Checks if the word is blacklisted.
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