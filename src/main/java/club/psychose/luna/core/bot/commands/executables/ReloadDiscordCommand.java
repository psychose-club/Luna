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

package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public final class ReloadDiscordCommand extends DiscordCommand {
    public ReloadDiscordCommand () {
        super("reload", "Reload specific configurations", "!reload <filter | settings>", new String[] {"rl", "rel"}, CommandCategory.ADMIN, new PermissionRoles[] {PermissionRoles.BOT_OWNER}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if ((arguments != null) && (arguments.length == 1)) {
            String mode = arguments[0].trim();

            switch (mode) {
                case "filter" -> {
                    Luna.SETTINGS_MANAGER.loadFilterSettings();
                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Filter reloaded!", "Filter successfully reloaded!", null, "uwu", Color.GREEN);
                }

                case "settings" -> {
                    Luna.SETTINGS_MANAGER.loadSettings();
                    DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings reloaded!", "Settings successfully reloaded!", null, "uwu", Color.GREEN);
                }

                default -> DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Please check the syntax!", null, "oh no qwq", Color.RED);
            }
        } else {
            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", null, "oh no qwq", Color.RED);
        }
    }
}