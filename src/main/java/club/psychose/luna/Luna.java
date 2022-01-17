package club.psychose.luna;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.system.managers.FileManager;
import club.psychose.luna.core.system.managers.SettingsManager;
import club.psychose.luna.utils.Constants;
import club.psychose.luna.utils.StringUtils;

public class Luna {
    public static final FileManager FILE_MANAGER = new FileManager();
    public static final SettingsManager SETTINGS_MANAGER = new SettingsManager();

    public static void main (String[] arguments) {
        System.out.println("   __ \n" +
                "  / / _   _ _ __   __ _ \n" +
                " / / | | | | '_ \\ / _` |\n" +
                "/ /__| |_| | | | | (_| |\n" +
                "\\____/\\__,_|_| |_|\\__,_|\n" +
                "\n");
        StringUtils.debug("Copyright © 2022 psychose.club");
        StringUtils.debug("Version: " + Constants.VERSION);
        StringUtils.debug("Build Version: " + Constants.BUILD);
        StringUtils.printEmptyLine();
        StringUtils.debug("This is a private psychose.club project!");
        StringUtils.debug("Publishing is not allowed!");
        StringUtils.printEmptyLine();
        StringUtils.debug("Loading settings...");
        SETTINGS_MANAGER.loadSettings();
        StringUtils.debug("Settings loaded!");
        StringUtils.printEmptyLine();
        StringUtils.debug("Starting bot...");
        new DiscordBot().startDiscordBot();
    }
}