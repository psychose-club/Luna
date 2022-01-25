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

package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.DiscordUtils;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class VerificationDiscordCommand extends DiscordCommand {
    public VerificationDiscordCommand () {
        super("verification", "Verify your self to gain access to the server!", "!verification", new String[] {"verify", "v"}, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.VERIFICATION});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (!(DiscordUtils.hasUserPermission(messageReceivedEvent.getMember(), messageReceivedEvent.getGuild().getId(), PermissionRoles.VERIFICATION))) {
            if (!(DiscordBot.CAPTCHA_MANAGER.hasMemberACaptcha(messageReceivedEvent.getMember()))) {
                String captchaCode = DiscordBot.CAPTCHA_MANAGER.getCaptchaGenerator().generateCaptchaCode();
                BufferedImage captchaImage = DiscordBot.CAPTCHA_MANAGER.getCaptchaGenerator().getCaptchaImage(captchaCode);

                try {
                    DiscordUtils.sendEmbedMessage(Objects.requireNonNull(messageReceivedEvent.getMember()).getUser(), "Please verify yourself!", "You need to write the captcha code in this chat!\nIf something went wrong please contact the administration!", null, "love you <3", Color.MAGENTA);

                    File captchaFile = DiscordBot.CAPTCHA_MANAGER.getCaptchaGenerator().saveCaptchaFile(captchaImage);
                    Captcha captcha = new Captcha(messageReceivedEvent.getGuild().getId(), captchaFile, captchaCode, messageReceivedEvent.getMember());

                    DiscordBot.CAPTCHA_MANAGER.addCaptcha(captcha);

                    messageReceivedEvent.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendFile(captchaFile).queue());
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException, messageReceivedEvent.getJDA().getGuilds());
                } catch (Exception exception) {
                    StringUtils.debug("Failed to send captcha!");
                }
            } else {
                DiscordUtils.sendEmbedMessage(Objects.requireNonNull(messageReceivedEvent.getMember()).getUser(), "You have already a captcha!", "You have already a captcha please solve it first!", null, "oh no qwq", Color.RED);
            }
        }
    }
}