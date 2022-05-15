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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * This class provides a scheduler to handle the bot actions.
 */

public final class BotScheduler {
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    // This method starts the scheduler.
    public void startScheduler () {
        // Starts the scheduler.
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Fetches the ILoveRadio streams.
            Luna.DISCORD_MANAGER.getDiscordMusicBotUtils().getILoveRadioFetcher().fetchStreams();

            // Fetches the latest message filters.
            Luna.SETTINGS_MANAGER.loadFilterSettings();
        }, 0, 30, TimeUnit.MINUTES);
    }
}