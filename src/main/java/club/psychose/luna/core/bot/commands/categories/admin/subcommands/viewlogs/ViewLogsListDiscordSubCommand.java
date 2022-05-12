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

package club.psychose.luna.core.bot.commands.categories.admin.subcommands.viewlogs;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordSubCommand;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.Constants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * This is the subcommand class for the ViewLogs command.
 */

public final class ViewLogsListDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public ViewLogsListDiscordSubCommand () {
        super("list", new String[] {"Type"}, 1);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        String type = arguments[0].toLowerCase().trim();

        switch (type) {
            case "crash" -> this.sendFileDirectoryList("crashes", messageReceivedEvent);
            case "detection" -> this.sendFileDirectoryList("detections", messageReceivedEvent);
            case "nuke" -> this.sendFileDirectoryList("nukes", messageReceivedEvent);
            default -> Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "List mode not found!", "Valid modes:\ncrash\ndetection\nnuke", FooterType.ERROR, Color.RED);
        }
    }

    // This method collects the directory content and sends it to the text channel.
    private void sendFileDirectoryList (String directoryName, MessageReceivedEvent messageReceivedEvent) {
        // Checks if the directory exist.
        if (Files.exists(Constants.getLunaFolderPath("\\logs\\" + directoryName + "\\"))) {
            // Collects the content.
            File[] directoryFiles = Constants.getLunaFolderPath("\\logs\\" + directoryName + "\\").toFile().listFiles();

            // If content exist it'll send it to the text channel.
            if ((directoryFiles != null) && (directoryFiles.length != 0)) {
                String fileNames = Arrays.stream(directoryFiles).filter(File::isFile).filter(directoryFile -> directoryFile.getName().endsWith(".txt")).map(directoryFile -> directoryFile.getName().replace(".txt", "") + "\n").collect(Collectors.joining("", "```bash\n", "```"));
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendTextChannelMessage(messageReceivedEvent.getTextChannel(), fileNames, "\n", true);
            } else {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", FooterType.ERROR, Color.RED);
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", FooterType.ERROR, Color.RED);
        }
    }
}