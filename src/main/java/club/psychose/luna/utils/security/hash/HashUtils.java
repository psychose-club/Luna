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

package club.psychose.luna.utils.security.hash;

import club.psychose.luna.utils.StringUtils;
import club.psychose.luna.utils.logging.CrashLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * This class handles hashing.
 */

public final class HashUtils {
    // This method calculates the SHA-256 hash from a file.
    public static String calculateSHA256Hash (Path filePath) {
        // Checks if the file exists.
        if (Files.exists(filePath)) {
            MessageDigest messageDigest;

            // Setups the message digest.
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                CrashLog.saveLogAsCrashLog(noSuchAlgorithmException);
                return "N/A";
            }

            // Creates a DigestInputStream and calculates the hash.
            try (DigestInputStream digestInputStream = new DigestInputStream(Files.newInputStream(filePath), messageDigest)) {
                while (digestInputStream.read() != -1)
                    messageDigest = digestInputStream.getMessageDigest();

                // Converts the bytes to HEX and return the string.
                return StringUtils.convertBytesToHEX(messageDigest.digest());
            } catch (IOException ioException) {
                CrashLog.saveLogAsCrashLog(ioException);
            }
        }

        return "N/A";
    }
}