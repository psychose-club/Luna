/*
 * Copyright © 2022 psychose.club
 * Contact: psychose.club@gmail.com
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
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.entities.Role;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public final class ServerSettings {
    private final HashMap<String, ServerSetting> serverConfigurationHashMap = new HashMap<>();

    public void addServerConfiguration (String serverID, ServerSetting serverSetting) {
        if (!(this.serverConfigurationHashMap.containsKey(serverID))) {
            if (!(Files.exists(Constants.getLunaFolderPath("\\servers\\" + serverID)))) {
                try {
                    Files.createDirectories(Constants.getLunaFolderPath("\\servers\\" + serverID));
                    Files.createDirectories(Constants.getLunaFolderPath("\\servers\\" + serverID + "\\settings\\"));
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException, null);
                    return;
                }
            }

            this.serverConfigurationHashMap.put(serverID, serverSetting);
            Luna.SETTINGS_MANAGER.saveServerSettings();
        }
    }

    public void removeServerConfiguration (String serverID) {
        if (this.serverConfigurationHashMap.containsKey(serverID)) {
            if (Files.exists(Constants.getLunaFolderPath("\\servers\\" + serverID))) {
                try {
                    Files.deleteIfExists(Constants.getLunaFolderPath("\\servers\\" + serverID));
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException, null);
                }
            }

            this.serverConfigurationHashMap.remove(serverID);
            Luna.SETTINGS_MANAGER.saveServerSettings();
        }
    }

    public void replaceChannelConfiguration (String serverID, DiscordChannels discordChannel, String channelID) {
        if ((serverID != null) && (discordChannel != null) && (channelID != null)) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                switch (discordChannel) {
                    case BOT_INFORMATION -> this.getServerConfigurationHashMap().replace(serverID, new ServerSetting(serverSetting.getOwnerRoleID(), serverSetting.getAdminRoleID(), serverSetting.getModeratorRoleID(), serverSetting.getVerificationRoleID(), channelID, serverSetting.getLoggingChannelID(), serverSetting.getVerificationChannelID()));
                    case LOGGING -> this.getServerConfigurationHashMap().replace(serverID, new ServerSetting(serverSetting.getOwnerRoleID(), serverSetting.getAdminRoleID(), serverSetting.getModeratorRoleID(), serverSetting.getVerificationRoleID(), serverSetting.getBotInfoChannelID(), channelID, serverSetting.getVerificationChannelID()));
                    case VERIFICATION -> this.getServerConfigurationHashMap().replace(serverID, new ServerSetting(serverSetting.getOwnerRoleID(), serverSetting.getAdminRoleID(), serverSetting.getModeratorRoleID(), serverSetting.getVerificationRoleID(), serverSetting.getBotInfoChannelID(), serverSetting.getLoggingChannelID(), channelID));
                }

                Luna.SETTINGS_MANAGER.saveServerSettings();
            }
        }
    }

    public boolean containsPermission (String serverID, List<Role> roles, PermissionRoles[] permissionRoles) {
        if ((roles != null) && (permissionRoles != null)) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                for (Role role : roles) {
                    for (PermissionRoles permissionRole : permissionRoles) {
                        switch (permissionRole) {
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
                            if (serverSetting.getBotInfoChannelID().equals(textChannelID))
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

    public String getDiscordChannelID (String serverID, DiscordChannels discordChannel) {
        if (discordChannel != null) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                switch (discordChannel) {
                    case ANY_CHANNEL -> {
                        return "";
                    }

                    case BOT_INFORMATION -> {
                        return serverSetting.getBotInfoChannelID();
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

    public HashMap<String, ServerSetting> getServerConfigurationHashMap () {
        return this.serverConfigurationHashMap;
    }

    public Role getDiscordServerRoleViaID (String serverID, PermissionRoles permissionRole, List<Role> roleList) {
        if (permissionRole != null) {
            ServerSetting serverSetting = this.getServerConfigurationHashMap().getOrDefault(serverID, null);

            if (serverSetting != null) {
                switch (permissionRole) {
                    case OWNER -> {
                        return DiscordUtils.getRoleViaID(serverSetting.getOwnerRoleID(), roleList);
                    }

                    case ADMIN -> {
                        return DiscordUtils.getRoleViaID(serverSetting.getAdminRoleID(), roleList);
                    }

                    case MODERATOR -> {
                        return DiscordUtils.getRoleViaID(serverSetting.getModeratorRoleID(), roleList);
                    }

                    case VERIFICATION -> {
                        return DiscordUtils.getRoleViaID(serverSetting.getVerificationRoleID(), roleList);
                    }
                }
            }
        }

        return null;
    }
}