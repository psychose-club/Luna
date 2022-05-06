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

package club.psychose.luna.core.bot.commands.categories.utility;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.logging.ConsoleLogger;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
 * This class provides the methods for a specific discord bot command.
 */

public final class VerificationDiscordCommand extends DiscordCommand {
    // Public constructor.
    public VerificationDiscordCommand () {
        super("verification", "Verify your self to gain access to the server!", "", new String[] {"verify", "v"}, CommandCategory.UTILITY, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.VERIFICATION});
    }

    // Command execution method.
    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // If the member is null it'll stop the method execution.
        assert messageReceivedEvent.getMember() == null;

        // Checks if the member has not already the verification role.
        if (!(Luna.DISCORD_MANAGER.getDiscordMemberUtils().checkUserPermission(messageReceivedEvent.getMember(), messageReceivedEvent.getGuild().getId(), new PermissionRoles[] {PermissionRoles.VERIFICATION}))) {
            // Gets the user from the member.
            User user = messageReceivedEvent.getMember().getUser();

            // Checks if the member has not generated a captcha.
            if (!(DiscordBot.CAPTCHA_MANAGER.hasMemberACaptcha(messageReceivedEvent.getMember()))) {
                // Generates the captcha.
                String captchaCode = DiscordBot.CAPTCHA_MANAGER.getCaptchaGenerator().generateCaptchaCode();
                BufferedImage captchaImage = DiscordBot.CAPTCHA_MANAGER.getCaptchaGenerator().getCaptchaImage(captchaCode);

                try {
                    // Sends the verification message to the user.
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(user, "Please verify yourself to access the server channels!", "You need to write the captcha code in this chat!\nIf something went wrong please contact the administration!", FooterType.SUCCESS, Color.MAGENTA);

                    // Creates the captcha.
                    File captchaFile = DiscordBot.CAPTCHA_MANAGER.getCaptchaGenerator().saveCaptchaFile(captchaImage);
                    Captcha captcha = new Captcha(messageReceivedEvent.getGuild().getId(), captchaFile, captchaCode, messageReceivedEvent.getMember());

                    // Adds the captcha.
                    DiscordBot.CAPTCHA_MANAGER.addCaptcha(captcha);

                    // Sends the captcha to the user.
                    messageReceivedEvent.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendFile(captchaFile).queue());
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException, messageReceivedEvent.getJDA().getGuilds());
                } catch (Exception exception) {
                    ConsoleLogger.debug("Failed to send captcha!");
                }
            } else {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(user, "You have already a captcha!", "You have already a captcha please solve it first!", FooterType.ERROR, Color.RED);
            }
        }
    }
}