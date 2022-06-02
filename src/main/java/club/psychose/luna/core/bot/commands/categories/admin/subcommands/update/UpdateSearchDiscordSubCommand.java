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

package club.psychose.luna.core.bot.commands.categories.admin.subcommands.update;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordSubCommand;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.Constants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/*
 * This is the subcommand class for the update.
 */

public final class UpdateSearchDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public UpdateSearchDiscordSubCommand () {
        super("search", null, 0);
    }

    // Subcommand execution.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Checks if the development is not enabled.
        if (!(Constants.DEVELOPMENT_MODE)) {
            // Checks if an update is available.
            if (Luna.APPLICATION_CHECKER.checkIfUpdateIsAvailable()) {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "New updates are available!", "To install the update use the install argument!", FooterType.SUCCESS, Color.ORANGE);
            } else {
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "No new updates are available!", "You are on the latest version :)", FooterType.SUCCESS, Color.GREEN);
            }
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Development mode is enabled!", "The development mode is enabled, you can't search for new updates!", FooterType.ERROR, Color.RED);
        }
    }
}
