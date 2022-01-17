package club.psychose.luna.core.bot.listeners;

import club.psychose.luna.Luna;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.core.logging.exceptions.InvalidConfigurationDataException;
import club.psychose.luna.core.system.settings.ServerSetting;
import club.psychose.luna.utils.DiscordUtils;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Map;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady (ReadyEvent readyEvent) {
        List<TextChannel> textChannelList = readyEvent.getJDA().getTextChannels();

        for (Map.Entry<String, ServerSetting> serverSettingEntry : Luna.SETTINGS_MANAGER.getServerSettings().getServerConfigurationHashMap().entrySet()) {
            String serverID = serverSettingEntry.getKey();
            ServerSetting serverSetting = serverSettingEntry.getValue();

            if (serverSetting != null) {
                if (serverSetting.getVerificationChannelID() != null) {
                    TextChannel verificationTextChannel = DiscordUtils.getTextChannel(serverSetting.getVerificationChannelID(), textChannelList);

                    if (verificationTextChannel != null) {
                        DiscordUtils.refreshVerificationChannel(serverID, verificationTextChannel, textChannelList);
                    } else {
                        CrashLog.saveLogAsCrashLog(new NullPointerException("Verification channel not found for the server server with the id + " + serverID +  "!"), readyEvent.getJDA().getGuilds());
                    }
                } else {
                    CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Invalid configuration for the server with the id" + serverID + "!"), readyEvent.getJDA().getGuilds());
                }
            } else {
                CrashLog.saveLogAsCrashLog(new InvalidConfigurationDataException("Invalid configuration for the server with the id" + serverID + "!"), readyEvent.getJDA().getGuilds());
            }
        }

        StringUtils.debug("Bot started successfully!");
        StringUtils.printEmptyLine();
    }
}