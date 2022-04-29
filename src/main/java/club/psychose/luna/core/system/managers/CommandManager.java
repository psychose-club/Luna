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
import club.psychose.luna.core.bot.commands.executables.*;

import java.util.ArrayList;

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

    public ArrayList<DiscordCommand> getDiscordCommandsArrayList () {
        return this.discordCommandsArrayList;
    }
}