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

public final class MessageFilter {
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