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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * This class contain methods for strings.
 */

public final class StringUtils {
    // This method converts bytes to HEX.
    public static String convertBytesToHEX (byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();

        // Converts every byte to HEX.
        for (byte convertByte : bytes)
            stringBuilder.append(String.format("%02x", convertByte));

        // Returns the string.
        return stringBuilder.toString().trim();
    }

    // This method returns a date and time string.
    public static String getDateAndTime (String formatMode) {
        // Setup date.
        Date date = new Date();

        // Setups the format.
        DateFormat dateFormat = switch (formatMode) {
            case "CONSOLE" -> new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            case "LOG" -> new SimpleDateFormat("dd-MM-yyyy HH-mm-ss-SSS");
            default -> new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        };

        // Returns the formatted date.
        return dateFormat.format(date);
    }

    // This returns a path with the valid file separators for the OS.
    public static Path getOSPath (Path path) {
        return Paths.get(path.toString().trim().replace("\\", File.separator));
    }
}