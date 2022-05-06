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

package club.psychose.luna.core.system.settings;

import club.psychose.luna.Luna;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * This class contains the settings for the server configuration usage.
 */

public final class ServerSettings {
    private final HashMap<String, ServerSetting> serverConfigurationHashMap = new HashMap<>();

    // This method adds a server configuration.
    public void addServerConfiguration (String serverID, ServerSetting serverSetting) {
        if (!(this.serverConfigurationHashMap.containsKey(serverID))) {
            this.serverConfigurationHashMap.put(serverID, serverSetting);
            Luna.SETTINGS_MANAGER.saveServerSettings();
        }
    }

    // This method reloads a server configuration.
    public void reloadServerConfiguration (String serverID) {

    }

    // This method removes a server configuration.
    public void removeServerConfiguration (String serverID) {
        if (this.serverConfigurationHashMap.containsKey(serverID)) {
            this.serverConfigurationHashMap.remove(serverID);
            Luna.SETTINGS_MANAGER.saveServerSettings();
        }
    }

    // This method replaces an already existing server configuration with a new one.
    public void replaceChannelConfiguration (String serverID, DiscordChannels discordChannel, String channelID) {
        if ((serverID != null) && (discordChannel != null) && (channelID != null)) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                switch (discordChannel) {
                    case BOT_INFORMATION -> this.getServerConfigurationHashMap().replace(serverID, new ServerSetting(serverSetting.getOwnerRoleID(), serverSetting.getAdminRoleID(), serverSetting.getModeratorRoleID(), serverSetting.getVerificationRoleID(), channelID, serverSetting.getLoggingChannelID(), serverSetting.getVerificationChannelID()));
                    case LOGGING -> this.getServerConfigurationHashMap().replace(serverID, new ServerSetting(serverSetting.getOwnerRoleID(), serverSetting.getAdminRoleID(), serverSetting.getModeratorRoleID(), serverSetting.getVerificationRoleID(), serverSetting.getBotInformationChannelID(), channelID, serverSetting.getVerificationChannelID()));
                    case VERIFICATION -> this.getServerConfigurationHashMap().replace(serverID, new ServerSetting(serverSetting.getOwnerRoleID(), serverSetting.getAdminRoleID(), serverSetting.getModeratorRoleID(), serverSetting.getVerificationRoleID(), serverSetting.getBotInformationChannelID(), serverSetting.getLoggingChannelID(), channelID));
                }

                Luna.SETTINGS_MANAGER.saveServerSettings();
            }
        }
    }

    // This method returns the server ids from the configurations.
    public ArrayList<String> getServerConfigurationIDsArrayList () {
        ArrayList<String> serverConfigurationIDsArrayList = new ArrayList<>();
        this.serverConfigurationHashMap.forEach((key, value) -> serverConfigurationIDsArrayList.add(key));
        return serverConfigurationIDsArrayList;
    }

    // This method checks if a specific configuration exists.
    public boolean checkIfServerConfigurationExist (String serverID) {
        return this.serverConfigurationHashMap.containsKey(serverID);
    }

    // This method checks if a specific user has a permission.
    public boolean containsPermission (String serverID, User user, List<Role> roles, PermissionRoles[] permissionRoles) {
        if ((roles != null) && (permissionRoles != null)) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                for (Role role : roles) {
                    for (PermissionRoles permissionRole : permissionRoles) {
                        switch (permissionRole) {
                            case BOT_OWNER -> {
                                if (Luna.SETTINGS_MANAGER.getBotSettings().getBotOwnerID().equals(user.getId()))
                                    return true;
                            }

                            case OWNER -> {
                                if (serverSetting.getOwnerRoleID().equals(role.getId()))
                                    return true;
                            }

                            case ADMIN -> {
                                if (serverSetting.getAdminRoleID().equals(role.getId()))
                                    return true;
                                }

                            case MODERATOR -> {
                                if (serverSetting.getModeratorRoleID().equals(role.getId()))
                                    return true;
                            }

                            case VERIFICATION -> {
                                if (serverSetting.getVerificationRoleID().equals(role.getId()))
                                    return true;
                            }

                            case EVERYONE -> {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    // This method checks if a specific channel is valid.
    public boolean isValidChannel (String serverID, String textChannelID, DiscordChannels[] discordChannels) {
        if (discordChannels != null) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                for (DiscordChannels discordChannel : discordChannels) {
                    switch (discordChannel) {
                        case ANY_CHANNEL -> {
                            return true;
                        }

                        case BOT_INFORMATION -> {
                            if (serverSetting.getBotInformationChannelID().equals(textChannelID))
                                return true;
                        }

                        case LOGGING -> {
                            if (serverSetting.getLoggingChannelID().equals(textChannelID))
                                return true;
                        }

                        case VERIFICATION -> {
                            if (serverSetting.getVerificationChannelID().equals(textChannelID))
                                return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    // This method returns the entire server configurations.
    public HashMap<String, ServerSetting> getServerConfigurationHashMap () {
        return this.serverConfigurationHashMap;
    }

    // This method returns a role from a specific permission role.
    public Role getDiscordServerRoleViaPermissionRole (String serverID, PermissionRoles permissionRole, List<Role> roleList) {
        if (permissionRole != null) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                switch (permissionRole) {
                    case OWNER -> {
                        return Luna.DISCORD_MANAGER.getDiscordRoleUtils().getRoleViaID(serverSetting.getOwnerRoleID(), roleList);
                    }

                    case ADMIN -> {
                        return Luna.DISCORD_MANAGER.getDiscordRoleUtils().getRoleViaID(serverSetting.getAdminRoleID(), roleList);
                    }

                    case MODERATOR -> {
                        return Luna.DISCORD_MANAGER.getDiscordRoleUtils().getRoleViaID(serverSetting.getModeratorRoleID(), roleList);
                    }

                    case VERIFICATION -> {
                        return Luna.DISCORD_MANAGER.getDiscordRoleUtils().getRoleViaID(serverSetting.getVerificationRoleID(), roleList);
                    }
                }
            }
        }

        return null;
    }

    // This method returns a discord channel id.
    public String getDiscordChannelID (String serverID, DiscordChannels discordChannel) {
        if (discordChannel != null) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                switch (discordChannel) {
                    case ANY_CHANNEL -> {
                        return "";
                    }

                    case BOT_INFORMATION -> {
                        return serverSetting.getBotInformationChannelID();
                    }

                    case LOGGING -> {
                        return serverSetting.getLoggingChannelID();
                    }

                    case VERIFICATION -> {
                        return serverSetting.getVerificationChannelID();
                    }
                }
            }
        }

        return null;
    }
}