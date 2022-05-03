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

package club.psychose.luna.core.bot.schedulers;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.utils.records.DiscordCommandReaction;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class ReactionScheduler {
    private final HashMap<DiscordCommand, ArrayList<DiscordCommandReaction>> discordCommandReactionHashMap = new HashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public void startScheduler () {
        int timePeriod = Luna.SETTINGS_MANAGER.getBotSettings().getTimePeriod();
        TimeUnit timeUnit = Luna.SETTINGS_MANAGER.getBotSettings().getTimeUnit();

        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            this.discordCommandReactionHashMap.forEach((key, discordCommandReactionArrayList) -> {
                if (((discordCommandReactionArrayList != null) && (discordCommandReactionArrayList.size() != 0))) {
                    Iterator<DiscordCommandReaction> discordCommandReactionIterator = discordCommandReactionArrayList.iterator();

                    while (discordCommandReactionIterator.hasNext()) {
                        DiscordCommandReaction discordCommandReaction = discordCommandReactionIterator.next();

                        boolean timePassed = false;

                        if (discordCommandReaction != null) {
                            long timeBetweenMilliseconds = (System.currentTimeMillis() - discordCommandReaction.getMilliseconds());

                            switch (timeUnit) {
                                case NANOSECONDS -> {
                                    long passedNanoSeconds = TimeUnit.MILLISECONDS.toNanos(timeBetweenMilliseconds);

                                    if (passedNanoSeconds > timePeriod)
                                        timePassed = true;
                                }

                                case MICROSECONDS -> {
                                    long passedMicroSeconds = TimeUnit.MILLISECONDS.toMicros(timeBetweenMilliseconds);

                                    if (passedMicroSeconds > timePeriod)
                                        timePassed = true;
                                }

                                case MILLISECONDS -> {
                                    if (timeBetweenMilliseconds > timePeriod)
                                        timePassed = true;
                                }

                                case SECONDS -> {
                                    long passedSeconds = TimeUnit.MILLISECONDS.toSeconds(timeBetweenMilliseconds);

                                    if (passedSeconds > timePeriod)
                                        timePassed = true;
                                }

                                case MINUTES -> {
                                    long passedMinutes = TimeUnit.MILLISECONDS.toMinutes(timeBetweenMilliseconds);

                                    if (passedMinutes > timePeriod)
                                        timePassed = true;
                                }

                                case HOURS -> {
                                    long passedHours = TimeUnit.MILLISECONDS.toHours(timeBetweenMilliseconds);

                                    if (passedHours > timePeriod)
                                        timePassed = true;
                                }

                                case DAYS -> {
                                    long passedDays = TimeUnit.MILLISECONDS.toDays(timeBetweenMilliseconds);

                                    if (passedDays > timePeriod)
                                        timePassed = true;
                                }
                            }
                        } else {
                            timePassed = true;
                        }

                        if (timePassed)
                            discordCommandReactionIterator.remove();
                    }
                }
            });
        }, 0, timePeriod, timeUnit);
    }

    public void addDiscordCommandReaction (DiscordCommand discordCommand, TextChannel textChannel, String reactionEmoji, String memberID, String messageID, String buffer) {
        ArrayList<DiscordCommandReaction> discordCommandReactionArrayList = this.discordCommandReactionHashMap.getOrDefault(discordCommand, null);
        DiscordCommandReaction discordCommandReaction = new DiscordCommandReaction(reactionEmoji, memberID, messageID, buffer, System.currentTimeMillis());

        if (discordCommandReactionArrayList == null) {
            discordCommandReactionArrayList = new ArrayList<>();
            this.discordCommandReactionHashMap.put(discordCommand, discordCommandReactionArrayList);
        }

        if (!(discordCommandReactionArrayList.contains(discordCommandReaction))) {
            discordCommandReactionArrayList.add(discordCommandReaction);
            this.discordCommandReactionHashMap.replace(discordCommand, discordCommandReactionArrayList);
            Luna.DISCORD_MANAGER.getDiscordMessageUtils().addReaction(textChannel, messageID, discordCommandReaction);
        }
    }

    public void removeMemberReactions (DiscordCommand discordCommand, String memberID, String messageID) {
        ArrayList<DiscordCommandReaction> removeDiscordCommandReactionsArrayList = this.discordCommandReactionHashMap.getOrDefault(discordCommand, null);

        if (removeDiscordCommandReactionsArrayList != null) {
            Iterator<DiscordCommandReaction> discordCommandReactionIterator = removeDiscordCommandReactionsArrayList.iterator();

            while (discordCommandReactionIterator.hasNext()) {
                DiscordCommandReaction discordCommandReaction = discordCommandReactionIterator.next();

                if (discordCommandReaction != null) {
                    if (discordCommandReaction.getMemberID().equals(memberID)) {
                        if (discordCommandReaction.getMessageID().equals(messageID)) {
                            discordCommandReactionIterator.remove();
                        }
                    }
                }
            }

            this.discordCommandReactionHashMap.replace(discordCommand, removeDiscordCommandReactionsArrayList);
        }
    }

    public ArrayList<DiscordCommandReaction> getDiscordCommandReactionArrayList (DiscordCommand discordCommand) {
        return this.discordCommandReactionHashMap.getOrDefault(discordCommand, new ArrayList<>());
    }
}