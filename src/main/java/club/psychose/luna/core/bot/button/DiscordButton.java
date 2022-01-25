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

package club.psychose.luna.core.bot.button;

import club.psychose.luna.core.logging.CrashLog;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.util.concurrent.TimeUnit;

public final class DiscordButton {
    private final Button button;
    private final boolean requiresButtonOwner;

    private boolean timeoutEnabled = false;
    private boolean buttonActivated = false;
    private Member buttonOwner = null;
    private Thread buttonTimeOutThread = null;

    public DiscordButton (String buttonText, String buttonID, ButtonStyle buttonStyle, boolean requiresButtonOwner) {
        this.button = Button.of(buttonStyle, buttonID, buttonText);
        this.requiresButtonOwner = requiresButtonOwner;
    }

    public DiscordButton (String buttonText, String buttonID, Emoji emoji, ButtonStyle buttonStyle, boolean requiresButtonOwner) {
        this.button = Button.of(buttonStyle, buttonID, buttonText, emoji);
        this.requiresButtonOwner = requiresButtonOwner;
    }

    public void activateButtonTimeout () {
        if (!(this.timeoutEnabled)) {
            this.timeoutEnabled = true;
            this.activateButton();

            this.buttonTimeOutThread = new Thread(() -> {
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(2));
                } catch (InterruptedException interruptedException) {
                    CrashLog.saveLogAsCrashLog(interruptedException, null);
                }

                this.deactivateButtonTimeout();
            });

            this.buttonTimeOutThread.start();
        } else {
            this.deactivateButtonTimeout();
            this.activateButtonTimeout();
        }
    }

    public void deactivateButtonTimeout () {
        this.timeoutEnabled = false;
        this.setButtonOwner(null);
        this.deactivateButton();

        if (this.buttonTimeOutThread != null)
            this.buttonTimeOutThread = null;
    }

    public void setButtonOwner (Member member) {
        this.buttonOwner = member;
    }

    private void activateButton () {
        this.buttonActivated = true;
    }

    private void deactivateButton () {
        this.buttonActivated = false;
    }

    public boolean isButtonActivated () {
        return this.buttonActivated;
    }

    public boolean isMemberButtonOwner (Member member) {
        return ((this.requiresButtonOwner) && (this.buttonOwner != null) && (this.buttonOwner.getId().equals(member.getId())));
    }

    public Button getButton () {
        return this.button;
    }
}