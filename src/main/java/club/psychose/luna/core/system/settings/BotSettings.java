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

package club.psychose.luna.core.system.settings;

import java.util.concurrent.TimeUnit;

public final class BotSettings {
    private String botToken = "NULL";
    private String youtubeAPIKey = "NULL";
    private String botOwnerID = "NULL";
    private String messageFilterURL = "NULL";
    private String messageWhitelistFilterURL = "NULL";
    private String prefix = "L!";
    private int timePeriod = 10;
    private TimeUnit timeUnit = TimeUnit.MINUTES;

    public void setBotToken (String value) {
        this.botToken = value;
    }

    public void setYoutubeAPIKey (String value) {
        this.youtubeAPIKey = value;
    }

    public void setBotOwnerID(String value) {
        this.botOwnerID = value;
    }

    public void setMessageFilterURL (String value) {
        this.messageFilterURL = value;
    }

    public void setMessageWhitelistFilterURL (String value) {
        this.messageWhitelistFilterURL = value;
    }

    public void setPrefix (String value) {
        this.prefix = value;
    }

    public void setTimePeriod (int value) {
        this.timePeriod = value;
    }

    public void setTimeUnit (TimeUnit value) {
        this.timeUnit = value;
    }

    public String getBotToken () {
        return this.botToken;
    }

    public String getYoutubeAPIKey () {
        return this.youtubeAPIKey;
    }

    public String getBotOwnerID() {
        return this.botOwnerID;
    }

    public String getMessageFilterURL () {
        return this.messageFilterURL;
    }

    public String getMessageWhitelistFilterURL () {
        return this.messageWhitelistFilterURL;
    }

    public String getPrefix () {
        return this.prefix;
    }

    public int getTimePeriod () {
        return this.timePeriod;
    }

    public TimeUnit getTimeUnit () {
        return this.timeUnit;
    }
}