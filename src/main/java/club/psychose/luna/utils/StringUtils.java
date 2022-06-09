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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * <p>The StringUtils class provides various utilities to handle with strings.</p>
 * @author CrashedLife
 */

public final class StringUtils {
    /**
     * <p>We are restricting the access, because the class only contain static methods.</p>
     */
    private StringUtils () {}

    /**
     * <p>The getDateAndTime method creates a readable string which includes the current date and time.</p>
     * <p>The used timezone is the selected timezone in {@link Constants}.</p>
     * @param formatMode Selects the mode how the string should be formatted.
     * @return The date and time string from {@link DateTimeFormatter}.
     */
    public static String getDateAndTime (String formatMode) {
        // Setups the format.
        switch (formatMode) {
            case "CONSOLE" -> formatMode = "dd/MM/yyyy HH:mm:ss";
            case "LOG" -> formatMode = "dd-MM-yyyy HH-mm-ss-SSS";
            default -> formatMode = "dd-MM-yyyy HH-mm-ss";
        };

        // Parses and returns the formatted date.
        return DateTimeFormatter.ofPattern(formatMode).withZone(Constants.TIMEZONE.toZoneId()).format(Instant.now());
    }

    /**
     * <p>This method returns a valid path for the used operating system.</p>
     * @param path The path that should be converted to the valid path.
     * @return Replaces the \\ from the path with the separator from {@link File}.
     */
    public static Path getOSPath (Path path) {
        return Paths.get(path.toString().trim().replace("\\", File.separator));
    }
}