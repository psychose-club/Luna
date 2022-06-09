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

import club.psychose.luna.core.system.managers.FileManager;
import club.psychose.luna.core.system.managers.LogManager;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.logging.ConsoleLogger;

/**
 * <p>This class is the main class of the application.</p>
 * <p>The main method is the method that will be called at first, when the application is started.</p>
 * @author CrashedLife
 */

public final class Luna {
    /**
     * <p>These are the public application constants that will be accessed from other classes.</p>
     */
    public static final FileManager FILE_MANAGER = new FileManager();
    public static final LogManager LOG_MANAGER = new LogManager();

    /**
     * <p>We are restricting the access, because we didn't want that the class will be initialized from outside of the class.</p>
     */
    private Luna () {}

    /**
     * <p>This method will be called when the application starts.</p>
     * @param arguments Arguments passed from the cli or another application.
     */
    public static void main (String[] arguments) {
        new Luna().startApplication();
    }

    /**
     * <p>Starts the application.</p>
     */
    private void startApplication () {
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
    }
}