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

package club.psychose.luna.core.bot.commands.categories.admin.subcommands.serverconfiguration;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordSubCommand;
import club.psychose.luna.core.system.settings.ServerSetting;
import club.psychose.luna.enums.FooterType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/*
 * This is the subcommand class for the ServerConfiguration.
 */

public final class ServerConfigurationUpdateDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public ServerConfigurationUpdateDiscordSubCommand () {
        super("update", new String[] {"Server ID", "Owner Role ID", "Admin Role ID", "Moderator Role ID", "Verification Role ID", "Bot Information Channel ID", "Logging Channel ID", "Verification Channel ID"}, 8);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        String serverID = arguments[0].trim();
        boolean found = messageReceivedEvent.getJDA().getGuilds().stream().anyMatch(guild -> guild.getId().equals(serverID));

        // Checks if the provided server id is valid.
        if (found) {
            String ownerRoleID = arguments[1].trim();
            String adminRoleID = arguments[2].trim();
            String moderatorRoleID = arguments[3].trim();
            String verificationRoleID = arguments[4].trim();
            String botInformationChannelID = arguments[5].trim();
            String loggingChannelID = arguments[6].trim();
            String verificationChannelID = arguments[7].trim();

            // Checks if the provided role ids exist on the server.
            if (((Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, ownerRoleID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, adminRoleID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, moderatorRoleID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordRoleUtils().checkIfRoleExistOnServer(serverID, verificationRoleID, messageReceivedEvent.getJDA().getGuilds())))) {
                // Checks if the provided channel ids exist on the server.
                if (((Luna.DISCORD_MANAGER.getDiscordChannelUtils().checkIfTextChannelExistOnServer(serverID, botInformationChannelID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordChannelUtils().checkIfTextChannelExistOnServer(serverID, loggingChannelID, messageReceivedEvent.getJDA().getGuilds())) && (Luna.DISCORD_MANAGER.getDiscordChannelUtils().checkIfTextChannelExistOnServer(serverID, verificationChannelID, messageReceivedEvent.getJDA().getGuilds())))) {
                    // Creates a new configuration.
                    ServerSetting serverSetting = new ServerSetting(ownerRoleID, adminRoleID, moderatorRoleID, verificationRoleID, botInformationChannelID,  loggingChannelID, verificationChannelID);

                    // It'll remove the old configuration and adds the new configuration.
                    Luna.SETTINGS_MANAGER.getServerSettings().removeServerConfiguration(serverID);
                    Luna.SETTINGS_MANAGER.getServerSettings().addServerConfiguration(serverID, serverSetting);
                    Luna.SETTINGS_MANAGER.getServerSettings().reloadServerConfiguration(serverID);

                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server entry updated in the database!", "Server entry successfully updated in the database!", FooterType.SUCCESS, Color.GREEN);
                } else {
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid TextChannel IDs!", "We can't find the channels on the server!", FooterType.ERROR, Color.RED);
                }
            } else {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid Role IDs!", "We can't find the roles on the server!", FooterType.ERROR, Color.RED);
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "The bot isn't on that server!", "You can only add the server if the bot is joined on the server!", FooterType.ERROR, Color.RED);
        }
    }
}