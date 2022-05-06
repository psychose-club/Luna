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

package club.psychose.luna.core.bot.commands;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.utils.records.DiscordCommandReaction;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

/*
 * This class is the subclass for the discord commands.
 */

public abstract class DiscordCommand {
    // Command variables.
    private final String commandName;
    private final String commandDescription;
    private final String commandSyntax;
    private final String[] aliases;
    private final CommandCategory commandCategory;
    private final PermissionRoles[] permissions;
    private final DiscordChannels[] discordChannels;


    // Public constructor.
    @Deprecated (forRemoval = true)
    public DiscordCommand (String commandName, String commandDescription, String commandSyntax, String[] aliases, CommandCategory commandCategory, PermissionRoles[] permissions, DiscordChannels[] discordChannels) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandSyntax = commandSyntax;
        this.aliases = aliases;
        this.commandCategory = commandCategory;
        this.permissions = permissions;
        this.discordChannels = discordChannels;
    }

    // Command execution method.
    public abstract void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent);

    // Message reaction method.
    public boolean onMessageReaction (DiscordCommandReaction discordCommandReaction, MessageReactionAddEvent messageReactionAddEvent) {
        return false;
    }

    // Adds a user reaction. (Restricted access)
    // The buffer argument is intended to give the developer a possibility to save a "specific state".
    protected void addReaction (TextChannel textChannel, String reactionEmoji, String memberID, String messageID, String buffer) {
        Luna.DISCORD_MANAGER.getReactionScheduler().addDiscordCommandReaction(this, textChannel, reactionEmoji, memberID, messageID, buffer);
    }

    // Returns the command name.
    public String getCommandName () {
        return this.commandName;
    }

    // Returns the command description.
    public String getCommandDescription () {
        return this.commandDescription;
    }

    // Returns the command syntax.
    public String getSyntaxString() {
        return Luna.SETTINGS_MANAGER.getBotSettings().getPrefix() + this.commandName + " " + this.commandSyntax;
    }

    // Returns the command aliases.
    public String[] getAliases () {
        return this.aliases;
    }

    // Returns the command category.
    public CommandCategory getCommandCategory() {
        return this.commandCategory;
    }

    // Returns the command permissions.
    public PermissionRoles[] getPermissions () {
        return this.permissions;
    }

    // Returns the allowed executor channels.
    public DiscordChannels [] getDiscordChannels () {
        return this.discordChannels;
    }
}