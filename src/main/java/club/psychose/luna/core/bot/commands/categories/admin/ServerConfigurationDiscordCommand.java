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
import club.psychose.luna.core.system.settings.ServerSetting;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.stream.Collectors;

/*
 * This class provides the methods for a specific discord bot command.
 */

public final class ServerConfigurationDiscordCommand extends DiscordCommand {
    // Public constructor.
    public ServerConfigurationDiscordCommand () {
        super("serverconfiguration", "Add a server to the bot.", "<add | list | remove | update> | <Server ID> | <Owner Role ID> <Admin Role ID> <Moderator Role ID> <Verification Role ID> <Bot Information Channel ID> <Logging Channel ID> <Verification Channel ID>", new String[] {"serverconfig", "config", "sc"}, CommandCategory.ADMIN, new PermissionRoles[] {PermissionRoles.BOT_OWNER}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    // Command execution method.
    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if ((arguments != null) && (arguments.length >= 1)) {
            String mode = arguments[0].trim();

            // Checks which mode is selected.
            switch (mode) {
                // add or update mode.
                case "add", "update" -> {
                    if (arguments.length >= 9) {
                        String serverID = arguments[1].trim();
                        boolean found = messageReceivedEvent.getJDA().getGuilds().stream().anyMatch(guild -> guild.getId().equals(serverID));

                        // Checks if the provided server id is valid.
                        if (found) {
                            String ownerRoleID = arguments[2].trim();
                            String adminRoleID = arguments[3].trim();
                            String moderatorRoleID = arguments[4].trim();
                            String verificationRoleID = arguments[5].trim();
                            String botInformationChannelID = arguments[6].trim();
                            String loggingChannelID = arguments[7].trim();
                            String verificationChannelID = arguments[8].trim();

                            // Checks if the provided role ids exist on the server.
                            if (((Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, ownerRoleID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, adminRoleID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, moderatorRoleID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, verificationRoleID, messageReceivedEvent.getJDA().getGuilds())))) {
                                // Checks if the provided channel ids exist on the server.
                                if (((Luna.DISCORD_MANAGER.getDiscordChannelUtils().checkIfTextChannelExistOnServer(serverID, botInformationChannelID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordChannelUtils().checkIfTextChannelExistOnServer(serverID, loggingChannelID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordChannelUtils().checkIfTextChannelExistOnServer(serverID, verificationChannelID, messageReceivedEvent.getJDA().getGuilds())))) {
                                    // Creates a new configuration.
                                    ServerSetting serverSetting = new ServerSetting(ownerRoleID, adminRoleID, moderatorRoleID, verificationRoleID, botInformationChannelID,  loggingChannelID, verificationChannelID);
                                    boolean update = mode.equalsIgnoreCase("update");

                                    // If update is selected it'll remove the old configuration and adds the new configuration.
                                    // Otherwise, it'll just add the configuration if it's not already exist.
                                    if (update) {
                                        Luna.SETTINGS_MANAGER.getServerSettings().removeServerConfiguration(serverID);
                                        Luna.SETTINGS_MANAGER.getServerSettings().addServerConfiguration(serverID, serverSetting);
                                        Luna.SETTINGS_MANAGER.getServerSettings().reloadServerConfiguration(serverID);

                                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server entry updated in the database!", "Server entry successfully updated in the database!", FooterType.SUCCESS, Color.GREEN);
                                    } else {
                                        if (Luna.SETTINGS_MANAGER.getServerSettings().checkIfServerConfigurationExist(serverID)) {
                                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server configuration already exists!", "If you want to update an entry please use the update mode!", FooterType.ERROR, Color.RED);
                                            return;
                                        }

                                        Luna.SETTINGS_MANAGER.getServerSettings().addServerConfiguration(serverID, serverSetting);
                                        Luna.SETTINGS_MANAGER.getServerSettings().reloadServerConfiguration(serverID);

                                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server added to the database!", "Server successfully added to the database!", FooterType.SUCCESS, Color.GREEN);
                                    }
                                } else {
                                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid TextChannel IDs!", "We can't find the channels on the server!", FooterType.ERROR, Color.RED);
                                }
                            } else {
                                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid Role IDs!", "We can't find the roles on the server!", FooterType.ERROR, Color.RED);
                            }
                        } else {
                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "The bot isn't on that server!", "You can only add the server if the bot is joined on the server!", FooterType.ERROR, Color.RED);
                        }
                    } else {
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Syntax:\n" + this.getSyntaxString(), FooterType.ERROR, Color.RED);
                    }
                }

                // List mode.
                case "list" -> {
                    // Checks if any server configuration exist and collects them and sends the message in the text channel.
                    if (Luna.SETTINGS_MANAGER.getServerSettings().getServerConfigurationIDsArrayList().size() != 0) {
                        String serverIDList = Luna.SETTINGS_MANAGER.getServerSettings().getServerConfigurationIDsArrayList().stream().map(id -> id + "\n").collect(Collectors.joining("", "```bash\n", "```"));
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendTextChannelMessage(messageReceivedEvent.getTextChannel(), serverIDList, "\n", true);
                    } else {
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "No servers are added to the database!", "Add a server to the database to see the IDs here!", FooterType.ERROR, Color.RED);
                    }
                }

                // Remove mode.
                case "remove" -> {
                    // Checks if a server id is provided.
                    if (arguments.length >= 2) {
                        String serverID = arguments[1].trim();

                        // Checks if the server configuration is registered and removes it from the configuration.
                        if (Luna.SETTINGS_MANAGER.getServerSettings().checkIfServerConfigurationExist(serverID)) {
                            Luna.SETTINGS_MANAGER.getServerSettings().removeServerConfiguration(serverID);
                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server configuration removed!", "We have successfully removed the server configuration!", FooterType.SUCCESS, Color.GREEN);
                        } else {
                            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server configuration didn't exist!", "Please select a valid server configuration!", FooterType.ERROR, Color.RED);
                        }
                    } else {
                        Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Syntax:\n" + this.getSyntaxString(), FooterType.ERROR, Color.RED);
                    }
                }

                default -> Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Syntax:\n" + this.getSyntaxString(), FooterType.ERROR, Color.RED);
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Syntax:\n" + this.getSyntaxString(), FooterType.ERROR, Color.RED);
        }
    }
}