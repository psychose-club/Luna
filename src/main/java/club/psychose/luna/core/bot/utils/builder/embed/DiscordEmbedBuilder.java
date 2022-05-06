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

package club.psychose.luna.core.bot.utils.builder.embed;

import club.psychose.luna.enums.FooterType;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.security.SecureRandom;
import java.util.HashMap;

/*
 * This method builds the embedded message.
 */

public final class DiscordEmbedBuilder {
    private final SecureRandom secureRandom = new SecureRandom();
    private final EmbedBuilder embedBuilder = new EmbedBuilder();
    private final String[] successFooterText = new String[] {"uwu", "owo", "ballin", "swag", "support human rights!", "stay safe! <3", "here a cookie \uD83C\uDF6A", "\uD83C\uDFF3️\u200D\uD83C\uDF08", "\uD83C\uDDFA\uD83C\uDDE6", "fuck sleep", "TFS", "hermine <3", "damian please feed hermine.", "love you <3"};
    private final String[] errorFooterText = new String[] {"oh no!", "qwq", "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "⛔️", "pls help me", "null", "404", "This is not the NASA!", ":(", "who r u?", "beep boop"};
    private final String[] musicFooterText = new String[] {"Fetched cover from amogus by R3AP3", "party", "mp3 player!", "visit kescher.at", "mommy :o", "beats provided by jvlix666", "sponsored by SoSBunker", "https://damian.psychose.club/"};

    private final String embedTitle;
    private final String embedDescription;
    private final HashMap<String, String> fieldHashMap;
    private final FooterType footerType;
    private final String footerText;
    private final Color embedColor;

    // Public constructors.
    public DiscordEmbedBuilder (String embedTitle, String embedDescription, FooterType footerType, Color embedColor) {
        this.embedTitle = embedTitle;
        this.embedDescription = embedDescription;
        this.fieldHashMap = null;
        this.footerType = footerType;
        this.footerText = null;
        this.embedColor = embedColor;
    }

    public DiscordEmbedBuilder (String embedTitle, String embedDescription, HashMap<String, String> fieldHashMap, FooterType footerType, Color embedColor) {
        this.embedTitle = embedTitle;
        this.embedDescription = embedDescription;
        this.fieldHashMap = fieldHashMap;
        this.footerType = footerType;
        this.footerText = null;
        this.embedColor = embedColor;
    }

    public DiscordEmbedBuilder (String embedTitle, String embedDescription, String footerText, Color embedColor) {
        this.embedTitle = embedTitle;
        this.embedDescription = embedDescription;
        this.fieldHashMap = null;
        this.footerType = null;
        this.footerText = footerText;
        this.embedColor = embedColor;
    }

    public DiscordEmbedBuilder (String embedTitle, String embedDescription, HashMap<String, String> fieldHashMap, String footerText, Color embedColor) {
        this.embedTitle = embedTitle;
        this.embedDescription = embedDescription;
        this.fieldHashMap = fieldHashMap;
        this.footerType = null;
        this.footerText = footerText;
        this.embedColor = embedColor;
    }

    // Builds the EmbedBuilder.
    public EmbedBuilder build () {
        this.embedBuilder.setTitle(this.embedTitle);
        this.embedBuilder.setDescription(this.embedDescription);
        this.embedBuilder.setAuthor("\uD83D\uDC08 L U N A \uD83D\uDC08");
        this.embedBuilder.setThumbnail("https://cdn.discordapp.com/app-icons/934768351710965801/c3542b33281164d8c80e26c434b2834c.png?size=256");
        this.embedBuilder.setColor(this.embedColor);

        if (this.fieldHashMap != null)
            this.fieldHashMap.forEach((key, value) -> this.embedBuilder.addField(key, value, false));

        // This method selects a random footer text.
        if (this.footerType != null) {
            switch (this.footerType) {
                case SUCCESS -> this.embedBuilder.setFooter(this.successFooterText[this.secureRandom.nextInt(this.successFooterText.length)]);
                case ERROR -> this.embedBuilder.setFooter(this.errorFooterText[this.secureRandom.nextInt(this.errorFooterText.length)]);
                case MUSIC -> this.embedBuilder.setFooter(this.musicFooterText[this.secureRandom.nextInt(this.musicFooterText.length)]);
            }
        } else {
            this.embedBuilder.setFooter(this.footerText);
        }

        return this.embedBuilder;
    }
}