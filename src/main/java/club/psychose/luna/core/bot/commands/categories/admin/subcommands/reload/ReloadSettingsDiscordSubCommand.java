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

package club.psychose.luna.core.bot.commands.categories.admin.subcommands.reload;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordSubCommand;
import club.psychose.luna.enums.FooterType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/*
 * This is the subcommand class for the Reloading command.
 */

public final class ReloadSettingsDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public ReloadSettingsDiscordSubCommand () {
        super("settings", new String[] {"all | bot | mute | mysql"}, 1);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        String settingMode = arguments[0].toLowerCase().trim();

        switch (settingMode) {
            case "all" -> {
                // Reloads the settings.
                Luna.SETTINGS_MANAGER.loadSettings();
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings reloaded!", "Settings successfully reloaded!", FooterType.SUCCESS, Color.GREEN);
            }

            case "bot" -> {
                // Reloads the bot settings.
                Luna.SETTINGS_MANAGER.loadBotSettings();
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings reloaded!", "Bot settings successfully reloaded!", FooterType.SUCCESS, Color.GREEN);
            }

            case "mute" -> {
                // Reloads the mute settings.
                Luna.SETTINGS_MANAGER.loadMuteSettings();
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings reloaded!", "Mute settings successfully reloaded!", FooterType.SUCCESS, Color.GREEN);
            }

            case "mysql" -> {
                // Reloads the MySQL settings.
                Luna.SETTINGS_MANAGER.loadMySQLSettings();
                Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings reloaded!", "MySQL settings successfully reloaded!", FooterType.SUCCESS, Color.GREEN);
            }

            default -> Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings mode not found!", "Valid modes:\nall\nbot\nmysql", FooterType.ERROR, Color.RED);
        }
    }
}