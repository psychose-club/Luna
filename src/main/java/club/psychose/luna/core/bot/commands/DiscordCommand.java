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

import java.util.ArrayList;

public abstract class DiscordCommand {
    private final String commandName;
    private final String commandDescription;
    private final String commandSyntax;
    private final String[] aliases;
    private final CommandCategory commandCategory;
    private final PermissionRoles[] permissions;
    private final DiscordChannels[] discordChannels;

    private final ArrayList<DiscordCommandReaction> discordCommandReactionArrayList = new ArrayList<>();

    @Deprecated
    public DiscordCommand (String commandName, String commandDescription, String commandSyntax, String[] aliases, CommandCategory commandCategory, PermissionRoles[] permissions, DiscordChannels[] discordChannels) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandSyntax = commandSyntax;
        this.aliases = aliases;
        this.commandCategory = commandCategory;
        this.permissions = permissions;
        this.discordChannels = discordChannels;
    }

    public abstract void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent);

    public void onMessageReaction (DiscordCommandReaction discordCommandReaction, MessageReactionAddEvent messageReactionAddEvent) {}

    protected void addReaction (TextChannel textChannel, String reactionEmoji, String memberID, String messageID, String buffer) {
        DiscordCommandReaction discordCommandReaction = new DiscordCommandReaction(reactionEmoji, memberID, messageID, buffer);

        if (!(this.discordCommandReactionArrayList.contains(discordCommandReaction))) {
            this.discordCommandReactionArrayList.add(discordCommandReaction);
            Luna.DISCORD_MANAGER.getDiscordMessageUtils().addReaction(textChannel, messageID, discordCommandReaction);
        }
    }

    protected void removeReaction (DiscordCommandReaction discordCommandReaction) {
        this.discordCommandReactionArrayList.remove(discordCommandReaction);
    }

    public ArrayList<DiscordCommandReaction> getDiscordCommandReactionArrayList () {
        return this.discordCommandReactionArrayList;
    }

    public String getCommandName () {
        return this.commandName;
    }

    public String getCommandDescription () {
        return this.commandDescription;
    }

    public String getCommandSyntax () {
        return this.commandSyntax;
    }

    protected String getSyntaxString () {
        return "L!" + this.commandName + " " + this.commandSyntax;
    }

    public String[] getAliases () {
        return this.aliases;
    }

    public CommandCategory getCommandCategory() {
        return this.commandCategory;
    }

    public PermissionRoles[] getPermissions () {
        return this.permissions;
    }

    public DiscordChannels [] getDiscordChannels () {
        return this.discordChannels;
    }
}