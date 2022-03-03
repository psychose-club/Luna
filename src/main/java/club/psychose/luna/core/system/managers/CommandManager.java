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

package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.commands.executables.*;
import club.psychose.luna.core.logging.CrashLog;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.MissingAccessException;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandManager {
    private final ArrayList<DiscordCommand> discordCommandsArrayList = new ArrayList<>();

    public void initializeCommands () {
        this.discordCommandsArrayList.add(new ClearTempFolderDiscordCommand());
        this.discordCommandsArrayList.add(new HelpDiscordCommand());
        this.discordCommandsArrayList.add(new MusicPlayerDiscordCommand());
        this.discordCommandsArrayList.add(new NukeDiscordCommand());
        this.discordCommandsArrayList.add(new ReloadDiscordCommand());
        this.discordCommandsArrayList.add(new ViewLogsDiscordCommand());
        this.discordCommandsArrayList.add(new VerificationDiscordCommand());
    }

    // Usage for development. | Update commands instantly.
    public void initializeSlashCommands (Guild guild, String botID) {
        HashMap<String, String> commandHashMap = this.discordCommandsArrayList.stream().filter(discordCommand -> (discordCommand.getSlashCommandName() != null) && (discordCommand.getSlashCommandDescription() != null)).collect(Collectors.toMap(DiscordCommand::getSlashCommandName, DiscordCommand::getSlashCommandDescription, (a, b) -> b, HashMap::new));

        try {
            List<Command> commandList = guild.retrieveCommands().complete();

            commandList.stream().filter(command -> command.getApplicationId().equals(botID)).forEachOrdered(command -> {
                boolean found = commandHashMap.keySet().stream().anyMatch(commandName -> command.getName().equals(commandName));

                if (!(found)) {
                    guild.deleteCommandById(command.getId()).queue();
                } else {
                    commandHashMap.remove(command.getName());
                }
            });

            commandHashMap.forEach((commandName, commandDescription) -> guild.upsertCommand(commandName, commandDescription).queue());
        } catch (MissingAccessException missingAccessException) {
            CrashLog.saveLogAsCrashLog(missingAccessException, guild.getJDA().getGuilds());
        }
    }

    public ArrayList<DiscordCommand> getDiscordCommandsArrayList () {
        return this.discordCommandsArrayList;
    }
}