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

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.enums.CommandCategory;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/*
 * This class provides the methods for the Discord bot ClearTempFolder command.
 */

public final class ClearTempFolderDiscordCommand extends DiscordCommand {
    // Public constructor.
    public ClearTempFolderDiscordCommand () {
        super("cleartempfolder", "Clears the temporary folder on the bot server!", new String[] {"clear", "cleartemp", "ctf"}, CommandCategory.ADMIN, new PermissionRoles[] {PermissionRoles.BOT_OWNER}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    // Command execution method.
    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Clears the temp folder.
        if (Luna.FILE_MANAGER.clearTempFolder()) {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Temp folder cleared :)", "Everything is clean now :o", FooterType.SUCCESS, Color.GREEN);
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Temp folder already cleared :)", "No need to let the bot do the dirty stuff.", FooterType.SUCCESS, Color.GREEN);
        }
    }

    // This method would be register the subcommands, but we don't have any in this command.
    @Override
    protected void registerSubCommands() {}
}