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

package club.psychose.luna.utils.logging;

import club.psychose.luna.utils.StringUtils;

/**
 * <p>The ConsoleLogger class provides methods to send debug information to the console.</p>
 * @author CrashedLife
 */
public final class ConsoleLogger {
    /**
     * <p>We are restricting the access, because the class only contain static methods.</p>
     */
    private ConsoleLogger () {}

    /**
     * <p>Prints an empty line to the console.</p>
     */
    public static void printEmptyLine () {
        System.out.println(" ");
    }

    /**
     * <p>Debugging an object to the console.</p>
     * <p>Also the date and time will be printed from {@link StringUtils}.</p>
     * @param objectToDebug The object that will be printed to the console.
     */
    public static void debug (Object objectToDebug) {
        System.out.println(StringUtils.getDateAndTime("CONSOLE") + " | [Luna]: " + objectToDebug);
    }
}