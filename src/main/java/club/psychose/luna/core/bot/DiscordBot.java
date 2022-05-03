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

package club.psychose.luna.core.bot;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.listeners.MessageListener;
import club.psychose.luna.core.bot.listeners.MessageReactionListener;
import club.psychose.luna.core.bot.listeners.ReadyListener;
import club.psychose.luna.core.system.managers.CaptchaManager;
import club.psychose.luna.core.system.managers.CommandManager;
import club.psychose.luna.utils.logging.CrashLog;
import net.dv8tion.jda.api.JDA;
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
            jdaBuilder.setActivity(Activity.watching("Sailor Moon | " + Luna.SETTINGS_MANAGER.getBotSettings().getPrefix() + "help for help!"));
            jdaBuilder.setStatus(OnlineStatus.ONLINE);

            jdaBuilder.addEventListeners(new ReadyListener());
            jdaBuilder.addEventListeners(new MessageListener());
            jdaBuilder.addEventListeners(new MessageReactionListener());

            COMMAND_MANAGER.initializeCommands();

            try {
                JDA jda = jdaBuilder.build();
                jdaBuilder.setAutoReconnect(true);
                jda.awaitReady();
            } catch (LoginException | InterruptedException exception) {
                CrashLog.saveLogAsCrashLog(exception, null);
            }
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Bot token is null!"), null);
        }
    }
}