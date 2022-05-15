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

package club.psychose.luna.core.bot.commands.categories.admin.subcommands.viewlogs;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordSubCommand;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.Constants;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.nio.file.Files;

/*
 * This is the subcommand class for the ViewLogs command.
 */

public final class ViewLogsDetectionDiscordSubCommand extends DiscordSubCommand {
    // Public constructor.
    public ViewLogsDetectionDiscordSubCommand () {
        super("detection", new String[] {"File Name"}, 1);
    }

    // Subcommand execution method.
    @Override
    public void onSubCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        String fileName = arguments[0].trim();

        if (!(fileName.endsWith(".txt")))
            fileName += ".txt";

        if (Files.exists(Constants.getLunaFolderPath("\\logs\\detections\\" + fileName))) {
            messageReceivedEvent.getTextChannel().sendFile(Constants.getLunaFolderPath("\\logs\\detections\\" + fileName).toFile()).queue();
        } else {
            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(messageReceivedEvent.getTextChannel(), "File not found!", "We can't find the log file :(", FooterType.ERROR, Color.RED);
        }
    }
}