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

package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.commands.categories.admin.*;
import club.psychose.luna.core.bot.commands.categories.fun.MusicPlayerDiscordCommand;
import club.psychose.luna.core.bot.commands.categories.utility.HelpDiscordCommand;
import club.psychose.luna.core.bot.commands.categories.utility.NukeDiscordCommand;
import club.psychose.luna.core.bot.commands.categories.utility.VerificationDiscordCommand;

import java.util.ArrayList;

/*
 * This class manages the commands.
 */

public final class CommandManager {
    private final ArrayList<DiscordCommand> discordCommandsArrayList = new ArrayList<>();

    // This method initialize the commands.
    public void initializeCommands () {
        // Admin category.
        this.discordCommandsArrayList.add(new ClearTempFolderDiscordCommand());
        this.discordCommandsArrayList.add(new ReloadDiscordCommand());
        this.discordCommandsArrayList.add(new ServerConfigurationDiscordCommand());
        this.discordCommandsArrayList.add(new ViewLogsDiscordCommand());
        this.discordCommandsArrayList.add(new UpdateDiscordCommand());

        // Fun category.
        this.discordCommandsArrayList.add(new MusicPlayerDiscordCommand());

        // Utility category.
        this.discordCommandsArrayList.add(new HelpDiscordCommand());
        this.discordCommandsArrayList.add(new NukeDiscordCommand());
        this.discordCommandsArrayList.add(new VerificationDiscordCommand());
    }

    // This method returns the captchas.
    public ArrayList<DiscordCommand> getDiscordCommandsArrayList () {
        return this.discordCommandsArrayList;
    }
}