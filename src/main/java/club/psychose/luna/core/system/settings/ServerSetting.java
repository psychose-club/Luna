package club.psychose.luna.core.system.settings;


public record ServerSetting (String getOwnerRoleID, String getAdminRoleID, String getModeratorRoleID, String getVerificationRoleID, String getBotInfoChannelID, String getLoggingChannelID, String getVerificationChannelID)  {}