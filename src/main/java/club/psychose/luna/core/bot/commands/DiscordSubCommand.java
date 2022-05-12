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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/*
 * This class is the subclass for the discord subcommands.
 */

public abstract class DiscordSubCommand {
    // Command variables.
    private final String subCommandName;
    private final String[] subCommandSyntax;
    private final int minimumArgumentsRequired;

    // Public constructor.
    public DiscordSubCommand (String subCommandName, String[] subCommandSyntax, int minimumArgumentsRequired) {
        this.subCommandName = subCommandName;
        this.subCommandSyntax = subCommandSyntax;
        this.minimumArgumentsRequired = minimumArgumentsRequired;
    }

    // Command execution method.
    public abstract void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent);

    // Returns the subcommand name.
    public String getSubCommandName () {
        return this.subCommandName;
    }

    // Returns the available subcommand syntax's.
    public String[] getSubCommandSyntax () {
        return this.subCommandSyntax;
    }

    // Returns the minimum required arguments.
    public int getMinimumArgumentsRequired () {
        return this.minimumArgumentsRequired;
    }
}