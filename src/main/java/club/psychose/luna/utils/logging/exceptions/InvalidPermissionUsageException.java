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

package club.psychose.luna.utils.logging.exceptions;

/*
 * This exception will be thrown if a Discord command shares one or more permissions along with the bot owner permission.
 * This is to prevent a security vulnerability when a command should only be accessible for the bot owner but let other roles access it.
 * The bot owner has full access to the bot already, so it isn't needed to add it to a command along with other permissions.
 */

public final class InvalidPermissionUsageException extends Exception {
    public InvalidPermissionUsageException (String message) {
        super(message, new Throwable());
    }
}