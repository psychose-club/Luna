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

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.commands.categories.admin.subcommands.reload.ReloadFilterDiscordSubCommand;
import club.psychose.luna.core.bot.commands.categories.admin.subcommands.reload.ReloadSettingsDiscordSubCommand;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;

/*
 * This class provides the subcommands for the Discord bot Reload command.
 */

public final class ReloadDiscordCommand extends DiscordCommand {
    // Public constructor.
    public ReloadDiscordCommand () {
        super("reload", "Reload specific configurations", new String[] {"rl", "rel"}, CommandCategory.ADMIN, new PermissionRoles[] {PermissionRoles.BOT_OWNER}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    // This method registers the subcommands.
    @Override
    protected void registerSubCommands () {
        this.addSubCommand(new ReloadFilterDiscordSubCommand());
        this.addSubCommand(new ReloadSettingsDiscordSubCommand());
    }
}