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

package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public final class ClearTempFolderDiscordCommand extends DiscordCommand {
    public ClearTempFolderDiscordCommand () {
        super("cleartempfolder", "Clears the temporary folder on the bot server!", "!cleartempfolder", new String[] {"cltmp"}, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getAuthor().getId().equals("321249545394847747")) {
            if (this.clearTempFolder(messageReceivedEvent.getJDA().getGuilds())) {
                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Temp folder cleared :)", "Everything is clean now :o", null, "stay safe! <3", Color.GREEN);
            } else {
                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Temp folder already cleared :)", "No need to let the bot do the dirty stuff.", null, "stay safe! <3", Color.GREEN);
            }
        } else {
            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid permissions!", "You didn't have permissions to execute this command!", null, "oh no qwq", Color.RED);
        }
    }

    private boolean clearTempFolder (List<Guild> guildList) {
        if (Files.exists(Constants.getLunaFolderPath("\\temp\\"))) {
            File[] tempFiles = Constants.getLunaFolderPath("\\temp\\").toFile().listFiles();

            if (tempFiles != null) {
                for (File file : tempFiles) {
                    boolean isCaptchaFile = false;
                    for (Captcha captcha : DiscordBot.CAPTCHA_MANAGER.getCaptchaArrayList()) {
                        if (captcha.getImageFile().equals(file)) {
                            isCaptchaFile = true;
                            break;
                        }
                    }

                    if (!(isCaptchaFile)) {
                        try {
                            Files.deleteIfExists(file.toPath());
                        } catch (IOException ioException) {
                            CrashLog.saveLogAsCrashLog(ioException, guildList);
                        }
                    }
                }

                return true;
            }
        }

        return false;
    }
}