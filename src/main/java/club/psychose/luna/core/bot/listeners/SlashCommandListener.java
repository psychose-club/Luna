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

package club.psychose.luna.core.bot.listeners;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class SlashCommandListener extends ListenerAdapter  {
    @Override
    public void onSlashCommandInteraction (@Nonnull SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if ((slashCommandInteractionEvent.getMember() != null) && !(slashCommandInteractionEvent.getMember().getUser().isBot()) && (slashCommandInteractionEvent.getGuild() != null)) {
            slashCommandInteractionEvent.deferReply().queue();

            Member member = slashCommandInteractionEvent.getMember();

            DiscordBot.COMMAND_MANAGER.getDiscordCommandsArrayList().stream().filter(discordCommand -> (discordCommand.getSlashCommandName() != null) && (discordCommand.getSlashCommandDescription() != null)).filter(discordCommand -> slashCommandInteractionEvent.getName().equals(discordCommand.getSlashCommandName())).forEachOrdered(discordCommand -> {
                if (DiscordUtils.isChannelValidForTheDiscordCommand(slashCommandInteractionEvent.getTextChannel(), slashCommandInteractionEvent.getGuild().getId(), discordCommand.getDiscordChannels())) {
                    if (DiscordUtils.hasUserPermissions(member, slashCommandInteractionEvent.getGuild().getId(), discordCommand.getPermissions())) {
                        discordCommand.onSlashCommandExecution(slashCommandInteractionEvent);
                    } else {
                        slashCommandInteractionEvent.getHook().editOriginalFormat("Invalid permissions!").queue();
                    }
                } else {
                    slashCommandInteractionEvent.getHook().editOriginalFormat("Invalid channel to execute this command!").queue();
                }
            });
        }
    }
}