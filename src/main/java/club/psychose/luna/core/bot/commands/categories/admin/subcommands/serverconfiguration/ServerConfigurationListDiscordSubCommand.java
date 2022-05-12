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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.stream.Collectors;

/*
 * This is the subcommand class for the ServerConfiguration.
 */

public final class ServerConfigurationListDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public ServerConfigurationListDiscordSubCommand () {
        super("list", null, 0);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Checks if any server configuration exist and collects them and sends the message in the text channel.
        if (Luna.SETTINGS_MANAGER.getServerSettings().getServerConfigurationIDsArrayList().size() != 0) {
            String serverIDList = Luna.SETTINGS_MANAGER.getServerSettings().getServerConfigurationIDsArrayList().stream().map(id -> id + "\n").collect(Collectors.joining("", "```bash\n", "```"));
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendTextChannelMessage(messageReceivedEvent.getTextChannel(), serverIDList, "\n", true);
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "No servers are added to the database!", "Add a server to the database to see the IDs here!", FooterType.ERROR, Color.RED);
        }
    }
}