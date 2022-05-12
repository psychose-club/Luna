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
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.logging.exceptions.InvalidPermissionUsageException;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * This class is the subclass for the discord commands.
 */

public abstract class DiscordCommand {
    // Command variables.
    private final ArrayList<DiscordSubCommand> discordSubCommandsArrayList = new ArrayList<>();
    private final String commandName;
    private final String commandDescription;
    private final String[] aliases;
    private final CommandCategory commandCategory;
    private final PermissionRoles[] permissions;
    private final DiscordChannels[] discordChannels;

    // Public constructor.
    public DiscordCommand (String commandName, String commandDescription, String[] aliases, CommandCategory commandCategory, PermissionRoles[] permissions, DiscordChannels[] discordChannels) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.aliases = aliases;
        this.commandCategory = commandCategory;
        this.permissions = permissions;
        this.discordChannels = discordChannels;

        // If more than one permission is added it'll check if bot owner is added and throws an exception then.
        // For more information see the InvalidPermissionUsageException class.
        if (permissions.length > 1) {
            try {
                for (PermissionRoles permissionRole : permissions)
                    if (permissionRole.equals(PermissionRoles.BOT_OWNER))
                        throw new InvalidPermissionUsageException("Invalid permission usage for " + this.commandName + "!");
            } catch (InvalidPermissionUsageException invalidPermissionUsageException) {
                CrashLog.saveLogAsCrashLog(invalidPermissionUsageException, null);
            }
        }

        // Register the subcommands.
        this.registerSubCommands();
    }

    // Command execution method.
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {}

    // Message reaction method.
    public boolean onMessageReaction (DiscordCommandReaction discordCommandReaction, MessageReactionAddEvent messageReactionAddEvent) {
        return false;
    }

    // Subcommand registration method.
    protected abstract void registerSubCommands ();

    // Adds a user reaction. (Restricted access)
    // The buffer argument is intended to give the developer a possibility to save a "specific state".
    protected void addReaction (TextChannel textChannel, String reactionEmoji, String memberID, String messageID, String buffer) {
        Luna.DISCORD_MANAGER.getReactionScheduler().addDiscordCommandReaction(this, textChannel, reactionEmoji, memberID, messageID, buffer);
    }

    // Registers a subcommand.
    protected void addSubCommand (DiscordSubCommand discordSubCommand) {
        if (!(this.discordSubCommandsArrayList.contains(discordSubCommand)))
            this.discordSubCommandsArrayList.add(discordSubCommand);
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
    // It'll parse automatically the required subcommand arguments in a syntax string.
    public String getSyntaxString () {
        HashMap<Integer, String> subCommandHashMap = new HashMap<>();
        ArrayList<Integer> splitSubCommandsSyntaxArrayList = new ArrayList<>();
        StringBuilder commandSyntax = new StringBuilder(Luna.SETTINGS_MANAGER.getBotSettings().getPrefix() + this.commandName);

        AtomicBoolean exceptionThrown = new AtomicBoolean(false);
        if (this.discordSubCommandsArrayList.size() != 0) {
            String subCommands = this.discordSubCommandsArrayList.stream().map(discordSubCommand -> discordSubCommand.getSubCommandName() + " | ").collect(Collectors.joining("", "<", ""));
            commandSyntax.append(" ").append(subCommands.substring(0, subCommands.length() - 3).trim()).append("> ");

            this.discordSubCommandsArrayList.stream().filter(discordSubCommand -> (discordSubCommand.getSubCommandSyntax() != null) && (discordSubCommand.getMinimumArgumentsRequired() != 0)).forEachOrdered(discordSubCommand -> {
                if (!(splitSubCommandsSyntaxArrayList.contains(discordSubCommand.getMinimumArgumentsRequired() - 1)))
                    splitSubCommandsSyntaxArrayList.add(discordSubCommand.getMinimumArgumentsRequired() - 1);

                IntStream.range(0, discordSubCommand.getSubCommandSyntax().length).forEachOrdered(subCommandSyntaxPosition -> {
                    String subCommandString = subCommandHashMap.getOrDefault(subCommandSyntaxPosition, null);
                    String subCommandSyntax = discordSubCommand.getSubCommandSyntax()[subCommandSyntaxPosition];

                    if (subCommandString != null) {
                        if (!(subCommandString.contains(subCommandSyntax))) {
                            subCommandString += " | " + subCommandSyntax;
                            subCommandHashMap.replace(subCommandSyntaxPosition, subCommandString);
                        }
                    } else {
                        subCommandString = subCommandSyntax;
                        subCommandHashMap.put(subCommandSyntaxPosition, subCommandString);
                    }
                });
            });

            if (exceptionThrown.get())
                return "AN EXCEPTION OCCURRED!";

            if (subCommandHashMap.size() != 0) {
                subCommandHashMap.forEach((subCommandPosition, value) -> {
                    String subCommandSyntax = "<" + value + ">";

                    if (splitSubCommandsSyntaxArrayList.contains(subCommandPosition))
                        subCommandSyntax += " |";

                    commandSyntax.append(subCommandSyntax).append(" ");
                });

                String syntax = commandSyntax.toString();

                if (syntax.endsWith(" | "))
                    syntax = syntax.substring(0, syntax.length() - 3);

                return syntax;
            }
        }

        return commandSyntax.toString();
    }

    // Returns the subcommands.
    public ArrayList<DiscordSubCommand> getDiscordSubCommandsArrayList () {
        return this.discordSubCommandsArrayList;
    }

    // Returns the command aliases.
    public String[] getAliases () {
        return this.aliases;
    }

    // Returns the command category.
    public CommandCategory getCommandCategory () {
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