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
import club.psychose.luna.utils.StringUtils;
import club.psychose.luna.utils.logging.ConsoleLogger;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidUpdateConfigurationException;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/*
 * This class downloads the new application update and launch the UniversalUpdater.
 */

public final class ApplicationDownloader {
    // This method downloads the update.
    public void downloadUpdate () {
        try {
            // Gets the latest version.
            JsonObject responseJsonObject = JsonUtils.fetchOnlineJsonObject(Constants.RELEASE_INFORMATION_URL);

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

                                // Application path and process id from this application..
                                Path applicationPath = StringUtils.getOSPath(Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getPath()));
                                long processID = ProcessHandle.current().pid();

                                // Creates the process.
                                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", Constants.getLunaFolderPath("\\temp\\UniversalUpdater.jar").toString(), "--pid", String.valueOf(processID), "--update-url", url, sha256, "--application-file", applicationPath.toString());

                                // Sets the running directory.
                                processBuilder.directory(Constants.getLunaFolderPath("\\temp\\").toFile());


                                // Starts the UniversalUpdater.
                                try {
                                    processBuilder.start();
                                } catch (Exception exception) {
                                    CrashLog.saveLogAsCrashLog(exception);
                                }

                                // Stops the bot.
                                System.exit(0);
                            } else {
                                CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version has not the valid properties!"));
                            }
                        } else {
                            CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version not fetched!"));
                        }
                    } else {
                        CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version to install not found!"));
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version not found!"));
                }
            }
        } catch (IOException exception) {
            CrashLog.saveLogAsCrashLog(exception);
        }
    }
}