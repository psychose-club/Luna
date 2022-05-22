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

package club.psychose.luna;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.system.managers.DiscordManager;
import club.psychose.luna.core.system.managers.FileManager;
import club.psychose.luna.core.system.managers.MySQLManager;
import club.psychose.luna.core.system.managers.SettingsManager;
import club.psychose.luna.core.system.updater.ApplicationChecker;
import club.psychose.luna.core.system.updater.ApplicationDownloader;
import club.psychose.luna.core.system.updater.UniversalUpdaterDownloader;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.logging.ConsoleLogger;

/*
 * This is the main class of the application.
 */

public final class Luna {
    // Initialize the variables.
    public static final ApplicationChecker APPLICATION_CHECKER = new ApplicationChecker();
    public static final DiscordManager DISCORD_MANAGER = new DiscordManager();
    public static final FileManager FILE_MANAGER = new FileManager();
    public static final MySQLManager MY_SQL_MANAGER = new MySQLManager();
    public static final SettingsManager SETTINGS_MANAGER = new SettingsManager();

    // This is the main method of the application.
    public static void main (String[] arguments) {
        System.out.println("""
                   __\s
                  / / _   _ _ __   __ _\s
                 / / | | | | '_ \\ / _` |
                / /__| |_| | | | | (_| |
                \\____/\\__,_|_| |_|\\__,_|
                """);
        ConsoleLogger.debug("Copyright © 2022 psychose.club");
        ConsoleLogger.debug("Version: " + Constants.VERSION);
        ConsoleLogger.debug("Build Version: " + Constants.BUILD);
        ConsoleLogger.printEmptyLine();

        // Checks if the development mode is disabled.
        if (!(Constants.DEVELOPMENT_MODE)) {
            ConsoleLogger.debug("Checking for updates...");

            // Searching for an update.
            if (APPLICATION_CHECKER.checkIfUpdateIsAvailable()) {
                ConsoleLogger.debug("Update found!");
                ConsoleLogger.debug("Downloading UniversalUpdater...");

                if (new UniversalUpdaterDownloader().downloadUniversalUpdater()) {
                    ConsoleLogger.debug("SUCCESS! UniversalUpdater downloaded!");
                    new ApplicationDownloader().downloadUpdate();
                } else {
                    ConsoleLogger.debug("ERROR! Failed to download UniversalUpdater!");
                }

                return;
            }

            ConsoleLogger.debug("Update not found!");
        } else {
            ConsoleLogger.debug("Update searching disabled, because development mode is enabled!");
        }

        ConsoleLogger.printEmptyLine();

        new Luna().initializeBot();
    }

    // Initializes the bot.
    private void initializeBot () {
        ConsoleLogger.debug("Loading settings...");
        SETTINGS_MANAGER.loadSettings();
        ConsoleLogger.debug("Settings loaded!");
        ConsoleLogger.printEmptyLine();
        ConsoleLogger.debug("Start schedulers...");
        DISCORD_MANAGER.getBotScheduler().startScheduler();
        DISCORD_MANAGER.getReactionScheduler().startScheduler();
        ConsoleLogger.debug("Schedulers started!");
        ConsoleLogger.printEmptyLine();
        ConsoleLogger.debug("Starting bot...");
        new DiscordBot().startDiscordBot();
    }
}