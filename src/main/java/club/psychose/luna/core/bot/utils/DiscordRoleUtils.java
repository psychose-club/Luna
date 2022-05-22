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

import club.psychose.luna.core.captcha.Captcha;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

/*
 * This class provides the utils for the Discord roles.
 */

public final class DiscordRoleUtils {
    // This method adds the verification role to a user.
    public void addVerificationRoleToUser (User user, Captcha captcha, Role verificationRole) {
        if (captcha.getMember().getId().equals(user.getId())) {
            if (captcha.getMember().getGuild().getRoles().contains(verificationRole))
                captcha.getMember().getGuild().addRoleToMember(user, verificationRole).queue();
        }
    }

    // This method checks if a specific role exist on a server.
    public boolean checkIfRoleExistOnServer (String serverID, String roleID, List<Guild> guildList) {
        return guildList.stream().filter(guild -> guild.getId().equals(serverID)).flatMap(guild -> guild.getRoles().stream()).anyMatch(role -> role.getId().equals(roleID));
    }

    // This method returns a role over a role id.
    public Role getRoleViaID (String roleID, List<Role> roleList) {
        return (roleList.size() != 0 ? (roleList.stream().filter(role -> role.getId().equals(roleID)).findFirst().orElse(null)) : null);
    }
}