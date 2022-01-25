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

package club.psychose.luna.core.bot;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.listeners.MessageListener;
import club.psychose.luna.core.bot.listeners.ReadyListener;
import club.psychose.luna.core.system.managers.CaptchaManager;
import club.psychose.luna.core.system.managers.CommandManager;
import club.psychose.luna.core.logging.CrashLog;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public final class DiscordBot {
    public static final CaptchaManager CAPTCHA_MANAGER = new CaptchaManager();
    public static final CommandManager COMMAND_MANAGER = new CommandManager();

    public void startDiscordBot () {
        if (!(Luna.SETTINGS_MANAGER.getBotSettings().getBotToken().equals("null"))) {
            JDABuilder jdaBuilder = JDABuilder.createDefault(Luna.SETTINGS_MANAGER.getBotSettings().getBotToken());
            jdaBuilder.setActivity(Activity.playing("with \uD83D\uDC08"));
            jdaBuilder.setStatus(OnlineStatus.ONLINE);

            jdaBuilder.addEventListeners(new ReadyListener());
            jdaBuilder.addEventListeners(new MessageListener());

            COMMAND_MANAGER.initializeCommands();

            try {
                jdaBuilder.build();
                jdaBuilder.setAutoReconnect(true);
            } catch (LoginException loginException) {
                CrashLog.saveLogAsCrashLog(loginException, null);
            }
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Bot token is null!"), null);
        }
    }
}