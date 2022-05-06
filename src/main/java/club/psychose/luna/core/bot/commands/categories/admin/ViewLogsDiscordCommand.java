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

package club.psychose.luna.core.bot.commands.categories.admin;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.Constants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * This class provides the methods for a specific discord bot command.
 */

public final class ViewLogsDiscordCommand extends DiscordCommand {
    // Public constructor.
    public ViewLogsDiscordCommand () {
        super("viewlogs", "Views logs!", "<crash | detection | nuke> <list | file>", new String[] {"vl", "logs"}, CommandCategory.ADMIN, new PermissionRoles[] {PermissionRoles.BOT_OWNER}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    // Command execution method.
    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if ((arguments != null) && (arguments.length == 2)) {
            String mode = arguments[0].trim();
            String fileName = arguments[1].trim().equalsIgnoreCase("LIST") ? "LIST" : arguments[1].trim() + ".txt" ;

            // Checks which mode is selected.
            // It'll send the file to the text channel or collect the directory content if LIST is provided as argument.
            switch (mode) {
                // Crash mode.
                case "crash" -> {
                    if (!(fileName.equalsIgnoreCase("LIST"))) {
                        if (Files.exists(Constants.getLunaFolderPath("\\logs\\crashes\\" + fileName))) {
                            messageReceivedEvent.getTextChannel().sendFile(Constants.getLunaFolderPath("\\logs\\crashes\\" + fileName).toFile()).queue();
                        } else {
                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "File not found!", "We can't find the log file :(", FooterType.ERROR, Color.RED);
                        }
                    } else {
                        this.sendFileDirectoryList("crashes", messageReceivedEvent);
                    }
                }

                // Detection mode.
                case "detection" -> {
                    if (!(fileName.equalsIgnoreCase("LIST"))) {
                        if (Files.exists(Constants.getLunaFolderPath("\\logs\\detections\\" + fileName))) {
                            messageReceivedEvent.getTextChannel().sendFile(Constants.getLunaFolderPath("\\logs\\detections\\" + fileName).toFile()).queue();
                        } else {
                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "File not found!", "We can't find the log file :(", FooterType.ERROR, Color.RED);
                        }
                    } else {
                        this.sendFileDirectoryList("detections", messageReceivedEvent);
                    }
                }

                // Nuke mode.
                case "nuke" -> {
                    if (!(fileName.equalsIgnoreCase("LIST"))) {
                        if (Files.exists(Constants.getLunaFolderPath("\\logs\\nukes\\" + fileName))) {
                            messageReceivedEvent.getTextChannel().sendFile(Constants.getLunaFolderPath("\\logs\\nukes\\" + fileName).toFile()).queue();
                        } else {
                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "File not found!", "We can't find the log file :(", FooterType.ERROR, Color.RED);
                        }
                    } else {
                        this.sendFileDirectoryList("nukes", messageReceivedEvent);
                    }
                }

                default -> Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Please check the syntax!", FooterType.ERROR, Color.RED);
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", FooterType.ERROR, Color.RED);
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