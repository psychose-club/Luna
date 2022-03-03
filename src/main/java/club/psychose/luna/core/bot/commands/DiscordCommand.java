/*
 * Copyright © 2022 psychose.club
 * Contact: psychose.club@gmail.com
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

package club.psychose.luna.core.bot.commands;

import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class DiscordCommand {
    private final String commandName;
    private final String commandDescription;
    private final String commandSyntax;
    private final String[] aliases;
    private final PermissionRoles[] permissions;
    private final DiscordChannels[] discordChannels;

    private String slashCommandName = null;
    private String slashCommandDescription = null;

    @Deprecated
    public DiscordCommand (String commandName, String commandDescription, String commandSyntax, String[] aliases, PermissionRoles[] permissions, DiscordChannels[] discordChannels) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandSyntax = commandSyntax;
        this.aliases = aliases;
        this.permissions = permissions;
        this.discordChannels = discordChannels;
    }

    public DiscordCommand (String commandName, String commandDescription, String commandSyntax, String[] aliases, PermissionRoles[] permissions, DiscordChannels[] discordChannels, String slashCommandName, String slashCommandDescription) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandSyntax = commandSyntax;
        this.aliases = aliases;
        this.permissions = permissions;
        this.discordChannels = discordChannels;

        this.slashCommandName = slashCommandName;
        this.slashCommandDescription = slashCommandDescription;
    }

    public abstract void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent);
    public void onSlashCommandExecution (SlashCommandInteractionEvent slashCommandInteractionEvent) {}

    public String getCommandName () {
        return this.commandName;
    }

    public String getCommandDescription () {
        return this.commandDescription;
    }

    public String getCommandSyntax () {
        return this.commandSyntax;
    }

    public String[] getAliases () {
        return this.aliases;
    }

    public PermissionRoles[] getPermissions () {
        return this.permissions;
    }

    public DiscordChannels [] getDiscordChannels () {
        return this.discordChannels;
    }

    public String getSlashCommandName () {
        return this.slashCommandName;
    }

    public String getSlashCommandDescription () {
        return this.slashCommandDescription;
    }
}