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

public final class ViewLogsDiscordCommand extends DiscordCommand {
    public ViewLogsDiscordCommand () {
        super("viewlogs", "Views logs!", "!viewlogs <crash | detection | nuke> <list | File Name>", new String[] {"vl", "logs"}, CommandCategory.ADMIN, new PermissionRoles[] {PermissionRoles.BOT_OWNER}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (arguments != null) {
            if (arguments.length == 2) {
                String mode = arguments[0].trim();
                String fileName = arguments[1].trim().equalsIgnoreCase("LIST") ? "LIST" : arguments[1].trim() + ".txt" ;

                switch (mode) {
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
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", FooterType.ERROR, Color.RED);
        }
    }

    private void sendFileDirectoryList (String directoryName, MessageReceivedEvent messageReceivedEvent) {
        if (Files.exists(Constants.getLunaFolderPath("\\logs\\" + directoryName + "\\"))) {
            File[] directoryFiles = Constants.getLunaFolderPath("\\logs\\" + directoryName + "\\").toFile().listFiles();

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