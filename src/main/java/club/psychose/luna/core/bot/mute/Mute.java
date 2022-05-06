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

/*
 * This class provides the methods to mute a member.
 */

public final class Mute {
    private final HashMap<Member, Integer> memberMuteCountHashMap = new HashMap<>();

    // This method adds the mute count up.
    public void addMuteCount (Member member) {
        // Fetches the users mute count.
        int muteCount = this.getMuteCount(member);

        // If the mute count is more than 3 it'll mute the user automatically for 12 hours.
        if (muteCount >= 3) {
            String timestamp = StringUtils.getDateAndTime("LOG");
            HashMap<String, String> fieldHashMap = new HashMap<>();
            fieldHashMap.put("Member", member.getAsMention());
            fieldHashMap.put("Mute Count", String.valueOf(this.getMuteCount(member)));
            fieldHashMap.put("Timestamp", timestamp);
            fieldHashMap.put("Mute Time", "12 Hours");

            member.getJDA().getGuilds().stream().filter(guild -> guild.isMember(member.getUser())).forEachOrdered(guild -> Luna.DISCORD_MANAGER.getDiscordBotUtils().sendLoggingMessage(guild.getId(), "Member muted!", fieldHashMap, guild.getTextChannels()));

            try {
                member.timeoutFor(Duration.ofHours(12)).reason("Multiple usage of inappropriate words!").queue();
            } catch (HierarchyException hierarchyException) {
                CrashLog.saveLogAsCrashLog(hierarchyException, member.getJDA().getGuilds());
            }

            Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(member.getUser(), "You got muted!", "You got automatically muted from any server that contains this bot!\nReason:\nMultiple usages of inappropriate words!\n\nIf you think this is a mistake please contact the administration!", fieldHashMap, FooterType.ERROR, Color.RED);
        }

        // Counts the mute count up.
        muteCount ++;

        // Replaces the member mute count.
        if (this.memberMuteCountHashMap.containsKey(member)) {
            this.memberMuteCountHashMap.replace(member, muteCount);
        } else {
            this.memberMuteCountHashMap.put(member, muteCount);
        }
    }

    // This method returns the member mute count. (Default: 0)
    public int getMuteCount (Member member) {
        return this.memberMuteCountHashMap.getOrDefault(member, 0);
    }
}