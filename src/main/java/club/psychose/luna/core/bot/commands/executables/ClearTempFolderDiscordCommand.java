package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class ClearTempFolderDiscordCommand extends DiscordCommand {
    public ClearTempFolderDiscordCommand () {
        super("cleartempfolder", "Clears the temporary folder on the server!", "!cleartempfolder", new String[] {"cltmp"}, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getAuthor().getId().equals("321249545394847747")) {
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
                                CrashLog.saveLogAsCrashLog(ioException, messageReceivedEvent.getJDA().getGuilds());
                            }
                        }
                    }

                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Temp folder cleared :)", "Everything is clean now :o", null, "stay safe! <3", Color.GREEN);
                } else {
                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Temp folder already cleared :)", "No need to let the bot do the dirty stuff.", null, "stay safe! <3", Color.GREEN);
                }
            } else {
                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Temp folder already cleared :)", "No need to let the bot do the dirty stuff.", null, "stay safe! <3", Color.GREEN);
            }
        } else {
            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid permissions!", "You didn't have permissions to execute this command!", null, "oh no qwq", Color.RED);
        }
    }
}