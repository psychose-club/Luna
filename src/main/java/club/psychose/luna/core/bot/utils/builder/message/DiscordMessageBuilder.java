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

package club.psychose.luna.core.bot.utils.builder.message;

import club.psychose.luna.core.bot.utils.builder.embed.DiscordEmbedBuilder;
import club.psychose.luna.enums.FooterType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public final class DiscordMessageBuilder {
    public void sendTextChannelMessage (TextChannel textChannel, String message, String splitCharacter, boolean markdown) {
        int discordMessageCharacterLimit = 2000;

        if (message.length() > discordMessageCharacterLimit) {
            String markdownType = null;
            String[] messageSplit = message.split(splitCharacter);

            int markdownLength = 0;

            if (markdown) {
                markdownType = messageSplit[0].trim();
                markdownType = markdownType.substring(2);

                // 6 -> ``` | 2 -> \n
                markdownLength = 8 + markdownType.length();

                messageSplit = Arrays.copyOfRange(messageSplit, 1, messageSplit.length - 1);
            }

            while (true) {
                String newMessage = "";
                int removeRanges = 0;

                for (String content : messageSplit) {
                    int newLength = newMessage.length() + content.length() + markdownLength;

                    if (newLength <= discordMessageCharacterLimit) {
                        newMessage += content;
                        removeRanges ++;
                    } else {
                        break;
                    }
                }

                if (markdown) {
                    textChannel.sendMessage("```" + markdownType + "\n" + newMessage + "```").queue();
                } else {
                    textChannel.sendMessage(newMessage).queue();
                }


                if (removeRanges >= messageSplit.length) {
                    messageSplit = Arrays.copyOfRange(messageSplit, removeRanges, messageSplit.length);
                } else {
                    break;
                }
            }
        } else {
            textChannel.sendMessage(message).queue();
        }
    }

    public void editEmbedMessage (Message message, String title, String description, FooterType footerType, Color color) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, footerType, color).build().build()).queue();
    }

    public void editEmbedMessage (Message message, String title, String description, FooterType footerType, Color color, Consumer<? super Message> messageConsumer) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, footerType, color).build().build()).queue(messageConsumer);
    }

    public void editEmbedMessage (Message message, String title, String description, HashMap<String, String> fieldHashMap, FooterType footerType, Color color) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerType, color).build().build()).queue();
    }

    public void editEmbedMessage (Message message, String title, String description, HashMap<String, String> fieldHashMap, FooterType footerType, Color color, Consumer<? super Message> messageConsumer) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerType, color).build().build()).queue(messageConsumer);
    }

    public void editEmbedMessage (Message message, String title, String description, String footerText, Color color) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, footerText, color).build().build()).queue();
    }

    public void editEmbedMessage (Message message, String title, String description, String footerText, Color color, Consumer<? super Message> messageConsumer) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, footerText, color).build().build()).queue(messageConsumer);
    }

    public void editEmbedMessage (Message message, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerText, color).build().build()).queue();
    }

    public void editEmbedMessage (Message message, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color, Consumer<? super Message> messageConsumer) {
        message.editMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerText, color).build().build()).queue(messageConsumer);
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, FooterType footerType, Color color) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, footerType, color).build().build()).queue();
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, FooterType footerType, Color color, Consumer<? super Message> messageConsumer) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, footerType, color).build().build()).queue(messageConsumer);
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, HashMap<String, String> fieldHashMap, FooterType footerType, Color color) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerType, color).build().build()).queue();
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, HashMap<String, String> fieldHashMap, FooterType footerType, Color color, Consumer<? super Message> messageConsumer) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerType, color).build().build()).queue(messageConsumer);
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, String footerText, Color color) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, footerText, color).build().build()).queue();
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, String footerText, Color color, Consumer<? super Message> messageConsumer) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, footerText, color).build().build()).queue(messageConsumer);
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerText, color).build().build()).queue();
    }

    public void sendEmbedMessage (TextChannel textChannel, String title, String description, HashMap<String, String> fieldHashMap, String footerText, Color color, Consumer<? super Message> messageConsumer) {
        textChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerText, color).build().build()).queue(messageConsumer);
    }

    public void sendEmbedMessage (User user, String title, String description, HashMap<String, String> fieldHashMap, FooterType footerType, Color color) {
        user.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerType, color).build().build()).queue()));
    }

    public void sendEmbedMessage (User user, String title, String description, HashMap<String, String> fieldHashMap, FooterType footerType, Color color, Consumer<? super Message> messageConsumer) {
        user.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, fieldHashMap, footerType, color).build().build()).queue(messageConsumer)));
    }

    public void sendEmbedMessage (User user, String title, String description, FooterType footerType, Color color) {
        user.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, footerType, color).build().build()).queue()));
    }

    public void sendEmbedMessage (User user, String title, String description, FooterType footerType, Color color, Consumer<? super Message> messageConsumer) {
        user.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessageEmbeds(new DiscordEmbedBuilder(title, description, footerType, color).build().build()).queue(messageConsumer)));
    }
}