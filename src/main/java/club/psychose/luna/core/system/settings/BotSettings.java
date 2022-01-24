package club.psychose.luna.core.system.settings;

public final class BotSettings {
    private String botToken = "NULL";
    private String messageFilterURL = "NULL";
    private String messageWhitelistFilterURL = "NULL";

    public void setBotToken (String value) {
        this.botToken = value;
    }

    public void setMessageFilterURL (String value) {
        this.messageFilterURL = value;
    }

    public void setMessageWhitelistFilterURL (String value) {
        this.messageWhitelistFilterURL = value;
    }

    public String getBotToken () {
        return this.botToken;
    }

    public String getMessageFilterURL () {
        return this.messageFilterURL;
    }

    public String getMessageWhitelistFilterURL () {
        return this.messageWhitelistFilterURL;
    }
}