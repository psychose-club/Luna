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

public final class MuteSettings {
    private boolean enableMuting = true;
    private int warningsNeededForMute = 3;
    private int muteTime = 12;
    private TimeUnit muteTimeUnit = TimeUnit.HOURS;
    private int timeToResetMutes = 24;
    private TimeUnit timeToResetMutesUnit = TimeUnit.HOURS;

    public void setEnableMuting (boolean value) {
        this.enableMuting = value;
    }

    public void setWarningsNeededForMute (int value) {
        this.warningsNeededForMute = value;
    }

    public void setMuteTime (int value) {
        this.muteTime = value;
    }

    public void setMuteTimeUnit (TimeUnit value) {
        this.muteTimeUnit = value;
    }

    public void setTimeToResetMutes (int value) {
        this.timeToResetMutes = value;
    }

    public void setTimeToResetMutesUnit (TimeUnit value) {
        this.timeToResetMutesUnit = value;
    }

    public boolean isMutingEnabled () {
        return this.enableMuting;
    }

    public int getWarningsNeededForMute () {
        return this.warningsNeededForMute;
    }

    public int getMuteTime () {
        return this.muteTime;
    }

    public TimeUnit getMuteTimeUnit () {
        return this.muteTimeUnit;
    }

    public int getTimeToResetMutes () {
        return this.timeToResetMutes;
    }

    public TimeUnit getTimeToResetMutesUnit () {
        return this.timeToResetMutesUnit;
    }
}