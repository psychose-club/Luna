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

import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public final class FileManager {
    // This is the public constructor. If the application calls the FileManager it'll try to create automatically the folders.
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

            if (!(Files.exists(Constants.getLunaFolderPath("\\logs\\nukes\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\logs\\nukes\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\servers\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\servers\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\settings\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\settings\\"));

            if (!(Files.exists(Constants.getLunaFolderPath("\\temp\\"))))
                Files.createDirectories(Constants.getLunaFolderPath("\\temp\\"));
        } catch (IOException ioException) {
            StringUtils.debug("IOException while creating the directories!");
            ioException.printStackTrace();
        }
    }

    // This method reads json files and convert them to a JsonObject.
    public JsonObject readJsonObject (Path path) {
        if (Files.exists(path)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();

            try (FileReader fileReader = new FileReader(path.toFile())) {
                return gson.fromJson(fileReader, JsonObject.class);
            } catch (IOException ioException) {
                StringUtils.debug("IOException while reading the JsonObject!");
                ioException.printStackTrace();
            }
        }

        return null;
    }

    // This method saves an ArrayList to a file. Optional it provides encryption for the content from the ArrayList.
    public void saveArrayListToAFile (Path outputPath, ArrayList<String> arrayListToSave) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String line : arrayListToSave) {
            stringBuilder.append(line).append("\n");
        }

        try {
            Files.write(outputPath, stringBuilder.toString().getBytes());
        } catch (IOException ioException) {
            StringUtils.debug("IOException while saving an ArrayList to a file!");
            ioException.printStackTrace();
        }
    }

    // This method saves a JsonObject to a file.
    public void saveJsonObject (Path path, JsonObject jsonObject) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(path.toFile())) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException ioException) {
            StringUtils.debug("IOException while saving the JsonObject!");
            ioException.printStackTrace();
        }
    }
}