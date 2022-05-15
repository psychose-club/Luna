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

package club.psychose.luna.core.system.settings;

/*
 * This class contains the settings for the filter usage.
 */

import java.util.ArrayList;
import java.util.HashMap;

public final class MessageFilterSettings {
    private final ArrayList<String> blacklistedWords = new ArrayList<>();
    private final ArrayList<String> whitelistedWords = new ArrayList<>();
    private final HashMap<String, String> characterFilterHashMap = new HashMap<>();

    // These are the default values for the settings.
    private boolean enableBlacklist = true;
    private boolean customFilters = false;
    private boolean fallbackToDefault = true;
    private String customBlackListURL = "NULL";
    private String customWhitelistURL = "NULL";
    private String customCharacterFilterURL = "NULL";

    public void setEnableBlacklist (boolean value) {
        this.enableBlacklist = value;
    }

    public void setCustomFiltersEnabled (boolean value) {
        this.customFilters = value;
    }

    public void setFallbackToDefault (boolean value) {
        this.fallbackToDefault = value;
    }

    public void setCustomBlackListURL (String value) {
        this.customBlackListURL = value;
    }

    public void setCustomWhitelistURL (String value) {
        this.customWhitelistURL = value;
    }

    public void setCustomCharacterFilterURL (String value) {
        this.customCharacterFilterURL = value;
    }

    public boolean isBlacklistEnabled () {
        return this.enableBlacklist;
    }

    public boolean isCustomFiltersEnabled () {
        return this.customFilters;
    }

    public boolean isFallbackToDefault () {
        return this.fallbackToDefault;
    }

    public String getCustomBlackListURL () {
        return this.customBlackListURL;
    }

    public String getCustomWhitelistURL () {
        return this.customWhitelistURL;
    }

    public String getCustomCharacterFilterURL () {
        return this.customCharacterFilterURL;
    }

    public ArrayList<String> getBlacklistedWords () {
        return this.blacklistedWords;
    }

    public ArrayList<String> getWhitelistedWords () {
        return this.whitelistedWords;
    }

    public HashMap<String, String> getCharacterFilterHashMap () {
        return this.characterFilterHashMap;
    }
}