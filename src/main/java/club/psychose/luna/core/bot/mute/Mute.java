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
import club.psychose.luna.utils.logging.exceptions.InvalidConfigurationDataException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.awt.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/*
 * This class provides the methods to mute a member.
 */

public final class Mute {
    // This method adds the mute count up.
    public void addMuteCount (Member member) {
        Luna.MY_SQL_MANAGER.addMute(member.getId());

        // Fetches the users mute count.
        int muteCount = this.getMuteCount(member);

        // Checks if the mute count is higher that the set maximum receivable mutes.
        // If it's higher it'll automatically mute the member.
        if (muteCount >= Luna.SETTINGS_MANAGER.getMuteSettings().getWarningsNeededForMute()) {
            String timestamp = StringUtils.getDateAndTime("LOG");
            HashMap<String, String> fieldHashMap = new HashMap<>();
            fieldHashMap.put("Member", member.getAsMention());
            fieldHashMap.put("Mute Count", String.valueOf(muteCount));
            fieldHashMap.put("Timestamp", timestamp);
            fieldHashMap.put("Mute Time", this.resolveTime());

            member.getJDA().getGuilds().stream().filter(guild -> guild.isMember(member.getUser())).forEachOrdered(guild -> Luna.DISCORD_MANAGER.getDiscordBotUtils().sendLoggingMessage(guild.getId(), "Member muted!", fieldHashMap, guild.getTextChannels()));

            try {
                String[] splitTime = this.resolveTime().split(" ");
                int timePeriod = Integer.parseInt(splitTime[0].trim());
                TimeUnit timeUnit = Arrays.stream(TimeUnit.values()).filter(resolveTimeUnit -> resolveTimeUnit.name().equalsIgnoreCase(splitTime[1].trim())).findFirst().orElse(null);

                Duration duration = null;
                switch (Objects.requireNonNull(timeUnit)) {
                    case NANOSECONDS -> duration = Duration.ofNanos(timePeriod);
                    case MICROSECONDS -> CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Microseconds are not supported for mutes!"));
                    case MILLISECONDS -> duration = Duration.ofMillis(timePeriod);
                    case SECONDS -> duration = Duration.ofSeconds(timePeriod);
                    case MINUTES -> duration = Duration.ofMinutes(timePeriod);
                    case HOURS -> duration = Duration.ofHours(timePeriod);
                    case DAYS -> duration = Duration.ofDays(timePeriod);
                    default -> CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Unsupported TimeUnit selected for mutes!"));
                }

                if (duration != null) {
                    member.timeoutFor(duration).reason("Multiple usage of inappropriate words detected! | Automatically performed by Luna").queue();
                    Luna.DISCORD_MANAGER.getDiscordMessageBuilder().sendEmbedMessage(member.getUser(), "You got muted!", "You got automatically muted from any server that contains this bot!\nReason:\nMultiple usages of inappropriate words!\n\nIf you think this is a mistake please contact the administration!", fieldHashMap, FooterType.ERROR, Color.RED);
                }
            } catch (HierarchyException hierarchyException) {
                CrashLog.saveLogAsCrashLog(hierarchyException);
            }
        }
    }

    // This method resolves the mute time and unit.
    private String resolveTime () {
        int timePeriod = Luna.SETTINGS_MANAGER.getMuteSettings().getMuteTime();
        TimeUnit timeUnit = Luna.SETTINGS_MANAGER.getMuteSettings().getMuteTimeUnit();

        return timePeriod + " " + timeUnit.name().substring(0, 1).toUpperCase().trim() + timeUnit.name().substring(1).toLowerCase().trim();
    }

    // This method returns the member mute count. (Default: 0)
    public int getMuteCount (Member member) {
        return Luna.MY_SQL_MANAGER.getUserMutes(member.getId());
    }
}