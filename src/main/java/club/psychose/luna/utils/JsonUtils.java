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

package club.psychose.luna.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public final class JsonUtils {
    // Method to fetch the JsonObject.
    public static JsonObject fetchOnlineJsonObject (String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        int line;
        while ((line = bufferedReader.read()) != -1) {
            stringBuilder.append((char) line);
        }

        return new Gson().fromJson(stringBuilder.toString().trim(), JsonObject.class);
    }
}