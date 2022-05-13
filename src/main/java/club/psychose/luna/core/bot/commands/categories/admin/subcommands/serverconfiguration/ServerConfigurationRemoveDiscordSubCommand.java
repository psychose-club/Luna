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
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.logging.CrashLog;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/*
 * This is the subcommand class for the ServerConfiguration.
 */

public final class ServerConfigurationRemoveDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public ServerConfigurationRemoveDiscordSubCommand () {
        super("remove", new String[] {"Server ID"}, 1);
    }

    // Subcommand execution.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Checks if a server id is provided.
        String serverID = arguments[0].trim();

        // Checks if the server configuration is registered and removes it from the configuration.
        if (Luna.SETTINGS_MANAGER.getServerSettings().checkIfServerConfigurationExist(serverID)) {
            Luna.SETTINGS_MANAGER.getServerSettings().removeServerConfiguration(serverID);
            CrashLog.removeGuild(messageReceivedEvent.getGuild());
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server configuration removed!", "We have successfully removed the server configuration!", FooterType.SUCCESS, Color.GREEN);
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Server configuration didn't exist!", "Please select a valid server configuration!", FooterType.ERROR, Color.RED);
        }
    }
}