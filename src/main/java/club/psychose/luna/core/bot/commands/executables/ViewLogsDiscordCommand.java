package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class ViewLogsDiscordCommand extends DiscordCommand {
    public ViewLogsDiscordCommand () {
        super("viewlogs", "Views logs!", "!viewlogs <crash | detection | nuke> <list | File Name>", new String[] {"vl", "logs"}, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getAuthor().getId().equals("321249545394847747")) {
            if (arguments.length == 2) {
                String mode = arguments[0].trim();
                String fileName = arguments[1].trim().equalsIgnoreCase("LIST") ? "LIST" : arguments[1].trim() + ".txt" ;

                switch (mode) {
                    case "crash" -> {
                        if (!(fileName.equalsIgnoreCase("LIST"))) {
                            if (Files.exists(Constants.getLunaFolderPath("\\logs\\crashes\\" + fileName))) {
                                messageReceivedEvent.getTextChannel().sendFile(Constants.getLunaFolderPath("\\logs\\crashes\\" + fileName).toFile()).queue();
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "File not found!", "We can't find the log file :(", null, "support human rights!", Color.RED);
                            }
                        } else {
                            if (Files.exists(Constants.getLunaFolderPath("\\logs\\crashes\\"))) {
                                File[] directoryFiles = Constants.getLunaFolderPath("\\logs\\crashes\\").toFile().listFiles();

                                if (directoryFiles != null) {
                                    String fileNames = Arrays.stream(directoryFiles).filter(File::isFile).filter(directoryFile -> directoryFile.getName().endsWith(".txt")).map(directoryFile -> directoryFile.getName().replace(".txt", "") + "\n").collect(Collectors.joining());
                                    messageReceivedEvent.getTextChannel().sendMessage(fileNames).queue();
                                } else {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", null, "support human rights!", Color.RED);
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", null, "support human rights!", Color.RED);
                            }
                        }
                    }

                    case "detection" -> {
                        if (!(fileName.equalsIgnoreCase("LIST"))) {
                            if (Files.exists(Constants.getLunaFolderPath("\\logs\\detections\\" + fileName))) {
                                messageReceivedEvent.getTextChannel().sendFile(Constants.getLunaFolderPath("\\logs\\detections\\" + fileName).toFile()).queue();
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "File not found!", "We can't find the log file :(", null, "support human rights!", Color.RED);
                            }
                        } else {
                            if (Files.exists(Constants.getLunaFolderPath("\\logs\\detections\\"))) {
                                File[] directoryFiles = Constants.getLunaFolderPath("\\logs\\detections\\").toFile().listFiles();

                                if (directoryFiles != null) {
                                    String fileNames = Arrays.stream(directoryFiles).filter(File::isFile).filter(directoryFile -> directoryFile.getName().endsWith(".txt")).map(directoryFile -> directoryFile.getName().replace(".txt", "") + "\n").collect(Collectors.joining());
                                    messageReceivedEvent.getTextChannel().sendMessage(fileNames).queue();
                                } else {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", null, "support human rights!", Color.RED);
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", null, "support human rights!", Color.RED);
                            }
                        }
                    }

                    case "nuke" -> {
                        if (!(fileName.equalsIgnoreCase("LIST"))) {
                            if (Files.exists(Constants.getLunaFolderPath("\\logs\\nukes\\" + fileName))) {
                                messageReceivedEvent.getTextChannel().sendFile(Constants.getLunaFolderPath("\\logs\\nukes\\" + fileName).toFile()).queue();
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "File not found!", "We can't find the log file :(", null, "support human rights!", Color.RED);
                            }
                        } else {
                            if (Files.exists(Constants.getLunaFolderPath("\\logs\\nukes\\"))) {
                                File[] directoryFiles = Constants.getLunaFolderPath("\\logs\\nukes\\").toFile().listFiles();

                                if (directoryFiles != null) {
                                    String fileNames = Arrays.stream(directoryFiles).filter(File::isFile).filter(directoryFile -> directoryFile.getName().endsWith(".txt")).map(directoryFile -> directoryFile.getName().replace(".txt", "") + "\n").collect(Collectors.joining());
                                    messageReceivedEvent.getTextChannel().sendMessage(fileNames).queue();
                                } else {
                                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", null, "support human rights!", Color.RED);
                                }
                            } else {
                                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Logs don't exist!", "Seems like no logs exists for this type!", null, "support human rights!", Color.RED);
                            }
                        }
                    }

                    default -> DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Please check the syntax!", null, "oh no qwq", Color.RED);
                }
            } else {
                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", null, "oh no qwq", Color.RED);
            }
        } else {
            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid permissions!", "You didn't have permissions to execute this command!", null, "oh no qwq", Color.RED);
        }
    }
}