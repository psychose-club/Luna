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

package club.psychose.luna.core.bot.utils;

import club.psychose.luna.Luna;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;

/*
 * This class provides the utils for the Discord members.
 */

public final class DiscordMemberUtils {
    // This method checks if a member has a specific permission.
    public boolean checkUserPermission (Member member, String serverID, PermissionRoles[] permissions) {
        return (member != null && (member.getId().equals(Luna.SETTINGS_MANAGER.getBotSettings().getBotOwnerID()) || (member.getRoles().size() == 0 ? Arrays.asList(permissions).contains(PermissionRoles.EVERYONE) : Luna.SETTINGS_MANAGER.getServerSettings().containsPermission(serverID, member.getUser(), member.getRoles(), permissions))));
    }
}