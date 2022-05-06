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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/*
 * This class provides the methods for a specific discord bot command.
 */

public final class ReloadDiscordCommand extends DiscordCommand {
    // Public constructor.
    public ReloadDiscordCommand () {
        super("reload", "Reload specific configurations", "<filter | settings>", new String[] {"rl", "rel"}, CommandCategory.ADMIN, new PermissionRoles[] {PermissionRoles.BOT_OWNER}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    // Command execution method.
    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Checks if arguments are provided.
        if ((arguments != null) && (arguments.length == 1)) {
            String mode = arguments[0].trim();

            // Checks which mode is selected.
            switch (mode) {
                case "filter" -> {
                    // Reloads the filter settings.
                    Luna.SETTINGS_MANAGER.loadFilterSettings();
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Filter reloaded!", "Filter successfully reloaded!", FooterType.SUCCESS, Color.GREEN);
                }

                case "settings" -> {
                    // Reloads the normal settings.
                    Luna.SETTINGS_MANAGER.loadSettings();
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings reloaded!", "Settings successfully reloaded!", FooterType.SUCCESS, Color.GREEN);
                }

                default -> Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Please check the syntax!", FooterType.ERROR, Color.RED);
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", FooterType.ERROR, Color.RED);
        }
    }
}