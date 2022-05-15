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

package club.psychose.luna.utils;

import net.dv8tion.jda.api.entities.Emoji;

import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * This class contains all constants.
 */

public final class Constants {
    // Version constants.
    public static final String VERSION = "1.0.5";
    public static final String BUILD = "6";

    // Emoji constant.
    public static final Emoji GO_BACK_EMOJI = Emoji.fromUnicode("\uD83D\uDD19");

    // Filter constants.
    // If custom filters are enabled it'll use it only as fallback if not "Fallback to default" is disabled!
    public static final String FALLBACK_BLACKLIST_URL = "https://luna.psychose.club/assets/blacklist";
    public static final String FALLBACK_WHITELIST_URL = "https://luna.psychose.club/assets/whitelist";
    public static final String FALLBACK_CHARACTER_FILTER_URL = "https://luna.psychose.club/assets/character_filter.json";

    // Luna folder constant.
    public static Path getLunaFolderPath (String additionalPath) {
        return additionalPath != null ? StringUtils.getOSPath(Paths.get(System.getProperty("user.home") + "\\psychose.club\\Luna\\" + additionalPath)) : StringUtils.getOSPath(Paths.get(System.getProperty("user.home") + "\\psychose.club\\Luna\\"));
    }
}