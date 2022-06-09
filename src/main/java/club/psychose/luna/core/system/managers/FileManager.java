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

import club.psychose.luna.Luna;
import club.psychose.luna.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * <p>The FileManager class manages files and creates automatically the needed directories for the application content.</p>
 * <p>Also it'll handle things like content deletion in directories.</p>
 * @author CrashedLife
 */
public final class FileManager {
    /**
     * <p>The public constructor creates the necessary directories if they didn't exist already.</p>
     */
    public FileManager () {
        try {
            if (!(Files.exists(Constants.getLunaFolderPath(null))))
                Files.createDirectories(Constants.getLunaFolderPath(null));

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\crashes\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\crashes\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\detections\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\detections\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\settings\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\settings\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\temp\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\temp\\"));
        } catch (IOException ioException) {
            Luna.LOG_MANAGER.getCrashLog().saveCrashLog(ioException);
        }
    }

    /**
     * <p>This method saves an ArrayList to a file.</p>
     * <p>The {@link Charset} parameter is optional, and let the developer the ability to change the charset.</p>
     * @param outputPath The location where the file should be saved.
     * @param arrayList The required ArrayList to save. (From the type String)
     * @param charset Optional: Encoding of the characters written to the file.
     */
    public void saveArrayListToAFile (Path outputPath, ArrayList<String> arrayList, Charset... charset) {
        // Collects the content from the ArrayList and converts it to a single string.
        String contentString = arrayList.stream().map(line -> line + "\n").collect(Collectors.joining());

        Charset encodingCharset = null;

        // Checks the charset parameter.
        if ((charset != null) && (charset.length != 0))
            encodingCharset = charset[0];

        try {
            // Creates the file and writes the string into the file.
            if (encodingCharset != null) {
                Files.writeString(outputPath, contentString, encodingCharset);
            } else {
                Files.writeString(outputPath, contentString);
            }
        } catch (IOException ioException) {
            Luna.LOG_MANAGER.getCrashLog().saveCrashLog(ioException);
        }
    }

    /**
     * <p>Saves the JsonObject to a file.</p>
     * @param outputPath The location where the file should be saved.
     * @param jsonObject The JsonObject that should be saved.
     */
    public void saveJsonObject (Path outputPath, JsonObject jsonObject) {
        // Setup Gson.
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        // Creates the file and writes the JsonObject to it.
        try (FileWriter fileWriter = new FileWriter(outputPath.toFile())) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException ioException) {
            Luna.LOG_MANAGER.getCrashLog().saveCrashLog(ioException);
        }
    }
}