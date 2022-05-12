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

package club.psychose.luna.core.bot.commands.categories.fun;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.commands.categories.fun.subcommands.musicplayer.*;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;

/*
 * This class provides the subcommands for the Discord bot Reload command.
 */

public final class MusicPlayerDiscordCommand extends DiscordCommand {
    // Public constructor.
    public MusicPlayerDiscordCommand () {
        super("music", "Music player for the server!", new String[] {"m", "mp", "player"}, CommandCategory.FUN, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    // This method registers the subcommands.
    @Override
    protected void registerSubCommands () {
        this.addSubCommand(new MusicPlayerPlayDiscordSubCommand());
        this.addSubCommand(new MusicPlayerPauseDiscordSubCommand());
        this.addSubCommand(new MusicPlayerResumeDiscordSubCommand());
        this.addSubCommand(new MusicPlayerQueueDiscordSubCommand());
        this.addSubCommand(new MusicPlayerRadioStreamsDiscordSubCommand());
        this.addSubCommand(new MusicPlayerSkipDiscordSubCommand());
        this.addSubCommand(new MusicPlayerStopDiscordSubCommand());
        this.addSubCommand(new MusicPlayerVolumeDiscordSubCommand());
    }
}