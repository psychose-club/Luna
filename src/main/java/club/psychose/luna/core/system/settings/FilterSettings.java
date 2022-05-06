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

import java.util.ArrayList;
import java.util.HashMap;

/*
 * This class contains the settings for the filter usage.
 */

public final class FilterSettings {
    // These are the default values for the settings.
    private final ArrayList<String> blacklistedWords = new ArrayList<>();
    private final ArrayList<String> whitelistedWords = new ArrayList<>();
    private final HashMap<String, String> bypassDetectionHashMap = new HashMap<>();

    public ArrayList<String> getBlacklistedWords () {
        return this.blacklistedWords;
    }

    public ArrayList<String> getWhitelistedWords () {
        return this.whitelistedWords;
    }

    public HashMap<String, String> getBypassDetectionHashMap () {
        return this.bypassDetectionHashMap;
    }
}