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
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidUpdateConfigurationException;
import com.google.gson.JsonObject;

import java.io.IOException;

/*
 * This class checks if an application update is available.
 */

public final class ApplicationChecker {
    // This method checks if an update is available from the update configuration in the RELEASE_INFORMATION_URL constant.
    public boolean checkIfUpdateIsAvailable () {
        try {
            JsonObject responseJsonObject = JsonUtils.fetchOnlineJsonObject(Constants.RELEASE_INFORMATION_URL);

            if (responseJsonObject != null) {
                if (responseJsonObject.has("Latest")) {
                    String latestVersion = responseJsonObject.get("Latest").getAsString();

                    return !(Constants.VERSION.equals(latestVersion));
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidUpdateConfigurationException("Latest version not found!"));
                }
            }
        } catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException);
        }

        return false;
    }
}