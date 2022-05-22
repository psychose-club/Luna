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

package club.psychose.luna.core.system.updater;

import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.JsonUtils;
import club.psychose.luna.utils.logging.ConsoleLogger;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidUpdateConfigurationException;
import club.psychose.luna.utils.security.hash.HashUtils;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * This class downloads the latest UniversalUpdater.
 */

public final class UniversalUpdaterDownloader {
    // This method downloads the UniversalUpdater.
    public boolean downloadUniversalUpdater () {
        try {
            // Gets the latest version.
            JsonObject responseJsonObject = JsonUtils.fetchOnlineJsonObject(Constants.UNIVERSAL_UPDATER_RELEASE_INFORMATION_URL);

            if (responseJsonObject != null) {
                if (responseJsonObject.has("Latest")) {
                    String latestVersion = responseJsonObject.get("Latest").getAsString();

                    if (responseJsonObject.has(latestVersion)) {
                        JsonObject latestVersionJsonObject = responseJsonObject.get(latestVersion).getAsJsonObject();

                        // Checks if the latest version is valid.
                        if (latestVersionJsonObject != null) {
                            if ((latestVersionJsonObject.has("URL")) && (latestVersionJsonObject.has("SHA-256"))) {
                                String url = latestVersionJsonObject.get("URL").getAsString();
                                String sha256 = latestVersionJsonObject.get("SHA-256").getAsString();

                                // Deletes the UniversalUpdater if it's already exist.
                                try {
                                    Files.deleteIfExists(Constants.getLunaFolderPath("\\temp\\UniversalUpdater.jar"));
                                } catch (IOException ioException) {
                                    CrashLog.saveLogAsCrashLog(ioException);
                                    return false;
                                }

                                // Downloads the UniversalUpdater.
                                boolean success = this.downloadFile(url, Constants.getLunaFolderPath("\\temp\\UniversalUpdater.jar"));

                                // Checks if the download was successful and checks the SHA-256 checksum.
                                if (success) {
                                    ConsoleLogger.debug("Updater downloaded successfully!");
                                    ConsoleLogger.debug("Verifying updater...");
                                    String updateSHA256Hash = HashUtils.calculateSHA256Hash(Constants.getLunaFolderPath("\\temp\\UniversalUpdater.jar"));

                                    if (!(updateSHA256Hash.equals("N/A")) && ((updateSHA256Hash.equals(sha256)))) {
                                        ConsoleLogger.debug("Updater verification was successful!");
                                        return true;
                                    } else {
                                        ConsoleLogger.debug("Verification failed! Updater checksum didn't match!");
                                    }
                                }
                            } else {
                                CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version has not the valid properties!"));
                            }
                        } else {
                            CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version not fetched!"));
                        }
                    } else {
                        CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version to download not found!"));
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version not found!"));
                }
            }
        } catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException);
        }

        return false;
    }

    // This method downlaods the file.
    private boolean downloadFile (String downloadURL, Path outputPath) throws IOException {
        // Setup URL connection.
        URL url = new URL(downloadURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // Check if the response is 200.
        if (httpURLConnection.getResponseCode() == 200) {
            boolean exceptionThrown = false;

            // The BufferedInputStream tries to read the content.
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream())) {
                // Sets the file output to the client.jar.
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath.toFile())) {
                    // Writes the bytes to the client.jar
                    try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
                        int bufferLength = 1024;

                        byte[] data = new byte[bufferLength];

                        int readBytes;
                        while ((readBytes = bufferedInputStream.read(data, 0, bufferLength)) != -1) {
                            bufferedOutputStream.write(data, 0, readBytes);
                            bufferedOutputStream.flush();
                        }
                    } catch (Exception exception) {
                        CrashLog.saveLogAsCrashLog(exception);
                        exceptionThrown = true;
                    }
                } catch (Exception exception) {
                    CrashLog.saveLogAsCrashLog(exception);
                    exceptionThrown = true;
                }
            } catch (Exception exception) {
                CrashLog.saveLogAsCrashLog(exception);
                exceptionThrown = true;
            }

            return !(exceptionThrown);
        }

        return false;
    }
}