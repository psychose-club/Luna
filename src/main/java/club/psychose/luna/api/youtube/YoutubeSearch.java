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

package club.psychose.luna.api.youtube;

import club.psychose.luna.Luna;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidConfigurationDataException;
import club.psychose.luna.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;

/*
 * This class handles the YouTube search requests.
 */

public final class YoutubeSearch {
    // This method searches a YouTube video via keywords.
    public YouTubeVideo searchYouTubeVideo (String[] keywords) throws IOException {
        // Checks if the YouTube API key is initialized.
        if (!(Luna.SETTINGS_MANAGER.getBotSettings().getYoutubeAPIKey().equals("NULL"))) {
            // Checks if the keywords are not null.
            if (keywords != null) {
                // Parses the keywords for the request.
                String keyword = Arrays.toString(keywords).replaceAll(" ", "+");

                // Send the request and receive the JsonObject.
                JsonObject youtubeSearchJsonObject = JsonUtils.fetchOnlineJsonObject("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=relevance&q=" + keyword + "&key=" + Luna.SETTINGS_MANAGER.getBotSettings().getYoutubeAPIKey());

                // Checks if the received JsonObject is not null.
                if (youtubeSearchJsonObject != null) {
                    // Checks if the JsonObject has the "items" key.
                    if (youtubeSearchJsonObject.has("items")) {
                        JsonArray jsonArray = youtubeSearchJsonObject.get("items").getAsJsonArray();

                        String videoURL = null;
                        String title = null;

                        // Fetches the required video data.
                        for (JsonElement jsonElement : jsonArray) {
                            if (jsonElement.isJsonObject()) {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();

                                if (jsonObject.has("id")) {
                                    JsonObject idJsonObject = jsonObject.get("id").getAsJsonObject();

                                    if (idJsonObject.has("videoId"))
                                        videoURL = idJsonObject.get("videoId").getAsString();
                                }

                                if (jsonObject.has("snippet")) {
                                    JsonObject snippetJsonObject = jsonObject.get("snippet").getAsJsonObject();

                                    if (snippetJsonObject.has("title"))
                                        title = snippetJsonObject.get("title").getAsString();

                                    if (snippetJsonObject.has("liveBroadcastContent")) {
                                        String liveBroadcastContent = snippetJsonObject.get("liveBroadcastContent").getAsString();

                                        // Returns null when the video is a broadcast.
                                        if (liveBroadcastContent.equalsIgnoreCase("upcoming"))
                                            return new YouTubeVideo(null,null, true);
                                    }
                                }
                            }
                        }

                        // Returns the YouTube video.
                        return (videoURL != null) && (title != null) ? new YouTubeVideo(title, videoURL, false) : new YouTubeVideo(null, null, false);
                    }
                }
            }
        } else {
            CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("API Key not initialized!"), null);
        }

        return null;
    }
}