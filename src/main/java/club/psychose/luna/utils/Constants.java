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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimeZone;

/**
 * <p>The Constants class provides specific and important variables for the application.</p>
 * @author CrashedLife
 */

public final class Constants {
    /**
     * <p>We are restricting the access, because the class only contain static methods.</p>
     */
    private Constants () {}

    /**
     * <p>The VERSION string represents the official labeled version from the application.</p>
     * <p>The BUILD string represents the build number from the application.</p>
     */
    public static final String VERSION = "dev-2.0.0";
    public static final String BUILD = "12";

    /**
     * <p>The DEVELOPMENT_MODE boolean lets the program know if the code is run in production or not.</p>
     * <p>If the boolean is true, it'll disable some functions from the application.</p>
     */
    public static final boolean DEVELOPMENT_MODE = true;

    /**
     * <p>The TIMEZONE object gives the application the timezone that should be used.</p>
     * <p>It's not final because the user should be able to change the timezone.</p>
     */
    public static TimeZone TIMEZONE = TimeZone.getDefault();

    /**
     * <p>The method returns the folder in the home directory, where the application works.</p>
     * @param additionalPath When the additionalPath is not null, it'll include the path, when the path is returned.
     * @return Returns the folder path.
     */
    public static Path getLunaFolderPath (String additionalPath) {
        return additionalPath != null ? StringUtils.getOSPath(Paths.get(System.getProperty("user.home") + "\\psychose.club\\Luna\\" + additionalPath)) : StringUtils.getOSPath(Paths.get(System.getProperty("user.home") + "\\psychose.club\\Luna\\"));
    }
}