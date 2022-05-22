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

package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.logging.CrashLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

/*
 * This class manage files and creates automatically the needed directories for the application content.
 */

public final class FileManager {
    // Public constructor.
    public FileManager () {
        // Creates the directories.
        try {
            if (!(Files.exists(Constants.getLunaFolderPath(null))))
                Files.createDirectories(Constants.getLunaFolderPath(null));

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\crashes\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\crashes\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\detections\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\detections\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\nukes\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\nukes\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\servers\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\servers\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\settings\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\settings\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\temp\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\temp\\"));
        } catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException);
        }
    }

    //This method clears the temp folder.
    public boolean clearTempFolder () {
        // Checks if the temp folder exist and fetches the folder content.
        if (Files.exists(Constants.getLunaFolderPath("\\temp\\"))) {
            File[] tempFiles = Constants.getLunaFolderPath("\\temp\\").toFile().listFiles();

            // Checks if the folder has contents available.
            if (tempFiles != null) {
                // Deletes the file if it's not a bot used captcha file.
                for (File file : tempFiles) {
                    boolean isCaptchaFile = false;
                    for (Captcha captcha : DiscordBot.CAPTCHA_MANAGER.getCaptchaArrayList()) {
                        if (captcha.getImageFile().equals(file)) {
                            isCaptchaFile = true;
                            break;
                        }
                    }

                    if (!(isCaptchaFile)) {
                        try {
                            Files.deleteIfExists(file.toPath());
                        } catch (IOException ioException) {
                            CrashLog.saveLogAsCrashLog(ioException);
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    // This method reads json files and convert them to a JsonObject.
    public JsonObject readJsonObject (Path path) {
        // Checks if the file exists.
        if (Files.exists(path)) {
            // Setup Gson.
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();

            // Reads the file content and convert it to a JsonObject.
            try (FileReader fileReader = new FileReader(path.toFile())) {
                return gson.fromJson(fileReader, JsonObject.class);
            } catch (IOException ioException) {
                CrashLog.saveLogAsCrashLog(ioException);
            }
        }

        return null;
    }


    // This method saves an ArrayList to a file.
    public void saveArrayListToAFile (Path outputPath, ArrayList<String> arrayListToSave) {
        // Collects the content from the ArrayList and converts it to a string.
        String contentString = arrayListToSave.stream().map(line -> line + "\n").collect(Collectors.joining());

        try {
            // Creates the file and write the string.
            Files.write(outputPath, contentString.getBytes());
        } catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException);
        }
    }

    // This method saves a JsonObject to a file.
    public void saveJsonObject (Path path, JsonObject jsonObject) {
        // Setup Gson.
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        // Creates the file and writes the JsonObject to it.
        try (FileWriter fileWriter = new FileWriter(path.toFile())) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException);
        }
    }
}