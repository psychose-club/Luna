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

package club.psychose.luna.core.system.settings;

public final class BotSettings {
    private String botToken = "NULL";
    private String messageFilterURL = "NULL";
    private String messageWhitelistFilterURL = "NULL";

    public void setBotToken (String value) {
        this.botToken = value;
    }

    public void setMessageFilterURL (String value) {
        this.messageFilterURL = value;
    }

    public void setMessageWhitelistFilterURL (String value) {
        this.messageWhitelistFilterURL = value;
    }

    public String getBotToken () {
        return this.botToken;
    }

    public String getMessageFilterURL () {
        return this.messageFilterURL;
    }

    public String getMessageWhitelistFilterURL () {
        return this.messageWhitelistFilterURL;
    }
}