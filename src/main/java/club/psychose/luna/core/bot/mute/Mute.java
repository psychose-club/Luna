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

package club.psychose.luna.core.bot.mute;

import club.psychose.luna.Luna;
import club.psychose.luna.enums.FooterType;
import club.psychose.luna.utils.logging.CrashLog;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.awt.*;
import java.time.Duration;
import java.util.HashMap;

public final class Mute {
    private final HashMap<Member, Integer> memberMuteCountHashMap = new HashMap<>();

    public void addMuteCount (Member member) {
        int muteCount = this.memberMuteCountHashMap.getOrDefault(member, 0);

        if (muteCount >= 3) {
            String timestamp = StringUtils.getDateAndTime("LOG");
            HashMap<String, String> fieldHashMap = new HashMap<>();
            fieldHashMap.put("Member", member.getAsMention());
            fieldHashMap.put("Mute Count", String.valueOf(this.getMuteCount(member)));
            fieldHashMap.put("Timestamp", timestamp);
            fieldHashMap.put("Mute Time", "12 Hours");

            // TODO: Let user choose which time the user should get muted.
            member.getJDA().getGuilds().stream().filter(guild -> guild.isMember(member.getUser())).forEachOrdered(guild -> Luna.DISCORD_MANAGER.getDiscordBotUtils().sendLoggingMessage(guild.getId(), "Member muted!", fieldHashMap, guild.getTextChannels()));

            try {
                member.timeoutFor(Duration.ofHours(12)).reason("Multiple usage of inappropriate words!").queue();
            } catch (HierarchyException hierarchyException) {
                CrashLog.saveLogAsCrashLog(hierarchyException, member.getJDA().getGuilds());
            }

            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(member.getUser(), "You got muted!", "You got automatically muted from any server that contains this bot!\nReason:\nMultiple usages of inappropriate words!\n\nIf you think this is a mistake please contact the administration!", fieldHashMap, FooterType.ERROR, Color.RED);
        }

        muteCount ++;

        if (this.memberMuteCountHashMap.containsKey(member)) {
            this.memberMuteCountHashMap.replace(member, muteCount);
        } else {
            this.memberMuteCountHashMap.put(member, muteCount);
        }
    }

    public int getMuteCount (Member member) {
        return this.memberMuteCountHashMap.getOrDefault(member, 0);
    }
}