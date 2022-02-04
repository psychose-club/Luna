/*
 * Copyright © 2022 psychose.club
 * Contact: psychose.club@gmail.com
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

package club.psychose.luna.core.bot.musicplayer.youtube;

import club.psychose.luna.Luna;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.core.logging.exceptions.InvalidConfigurationDataException;
import club.psychose.luna.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Arrays;

public final class YoutubeSearch {
    public YouTubeVideo searchYouTubeVideo (String[] keywords) throws IOException {
        if (!(Luna.SETTINGS_MANAGER.getBotSettings().getYoutubeAPIKey().equals("NULL"))) {
            if (keywords != null) {
                String keyword = Arrays.toString(keywords).replaceAll(" ", "+");

                JsonObject youtubeSearchJsonObject = JsonUtils.fetchOnlineJsonObject("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=rating&q=" + keyword + "&key=" + Luna.SETTINGS_MANAGER.getBotSettings().getYoutubeAPIKey());

                if (youtubeSearchJsonObject != null) {
                    if (youtubeSearchJsonObject.has("items")) {
                        JsonArray jsonArray = youtubeSearchJsonObject.get("items").getAsJsonArray();

                        String videoURL = null;
                        String title = null;

                        for (JsonElement jsonElement : jsonArray) {
                            if (jsonElement.isJsonObject()) {
                                JsonObject jsonObject = jsonElement.getAsJsonObject();

                                if (jsonObject.has("id")) {
                                    JsonObject idJsonObject = jsonObject.get("id").getAsJsonObject();

                                    if (idJsonObject.has("videoId")) {
                                        videoURL = idJsonObject.get("videoId").getAsString();
                                    }
                                }

                                if (jsonObject.has("snippet")) {
                                    JsonObject snippetJsonObject = jsonObject.get("snippet").getAsJsonObject();

                                    if (snippetJsonObject.has("title")) {
                                        title = snippetJsonObject.get("title").getAsString();
                                    }

                                    if (snippetJsonObject.has("liveBroadcastContent")) {
                                        String liveBroadcastContent = snippetJsonObject.get("liveBroadcastContent").getAsString();

                                        if (liveBroadcastContent.equalsIgnoreCase("upcoming")) {
                                            return new YouTubeVideo(null,null, true);
                                        }
                                    }
                                }
                            }
                        }

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