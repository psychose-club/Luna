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
        // It'll check if the blacklist was enabled.
        if (Luna.SETTINGS_MANAGER.getMessageFilterSettings().isBlacklistEnabled()) {
            // It'll check if the blacklist is not empty.
            if (Luna.SETTINGS_MANAGER.getMessageFilterSettings().getBlacklistedWords().size() != 0) {
                // Initialize the filterReplacement boolean.
                boolean filterReplacement = Luna.SETTINGS_MANAGER.getMessageFilterSettings().getCharacterFilterHashMap().size() != 0;

                // We replace all spaces and all non-printable characters to prevent exploits.
                message = message.replaceAll(" ", "").replaceAll("\\p{C}", "").trim();

                // If the whitelist is not empty it'll remove the whitelisted words from the messages.
                if (Luna.SETTINGS_MANAGER.getMessageFilterSettings().getWhitelistedWords().size() != 0) {
                    for (String whitelistedWord : Luna.SETTINGS_MANAGER.getMessageFilterSettings().getWhitelistedWords()) {
                        message = message.replaceAll(whitelistedWord, "").trim();
                    }
                }

                // If the filter replacement is enabled, it'll replace the characters that can bypass the restriction with the normal characters.
                if (filterReplacement) {
                    for (Map.Entry<String, String> replaceCharacterFilterHashMap : Luna.SETTINGS_MANAGER.getMessageFilterSettings().getCharacterFilterHashMap().entrySet()) {
                        String character = replaceCharacterFilterHashMap.getKey();
                        String replaceWith = replaceCharacterFilterHashMap.getValue();

                        if (replaceWith.contains(" ")) {
                            String[] splitReplaceWith = replaceWith.split(" ");

                            for (String replacementCharacter : splitReplaceWith) {
                                message = message.replaceAll(replacementCharacter, character);
                            }
                        } else {
                            message = message.replaceAll(replaceWith, character);
                        }
                    }
                }

                // Here we'll check if the message matches with a blacklisted word.
                for (String blacklistedWord : Luna.SETTINGS_MANAGER.getMessageFilterSettings().getBlacklistedWords()) {
                    if (blacklistedWord.equalsIgnoreCase(message)) {
                        this.lastBadWord = blacklistedWord;
                        return false;
                    }
                }
            } else {
                CrashLog.saveLogAsCrashLog(new NullPointerException("Blacklist is empty!"));
            }
        }

        return true;
    }

    // This method returns the latest logged bad word.
    public String getLastBadWord () {
        return this.lastBadWord;
    }
}