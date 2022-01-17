package club.psychose.luna.core.bot;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.listeners.MessageListener;
import club.psychose.luna.core.bot.listeners.ReadyListener;
import club.psychose.luna.core.system.managers.CommandManager;
import club.psychose.luna.core.logging.CrashLog;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    public static final CommandManager COMMAND_MANAGER = new CommandManager();

    public void startDiscordBot () {
        if (!(Luna.SETTINGS_MANAGER.getBotSettings().getBotToken().equals("null"))) {
            JDABuilder jdaBuilder = JDABuilder.createDefault(Luna.SETTINGS_MANAGER.getBotSettings().getBotToken());
            jdaBuilder.setActivity(Activity.listening(":robot: psychose.club :robot:"));
            jdaBuilder.setStatus(OnlineStatus.ONLINE);

            jdaBuilder.addEventListeners(new ReadyListener());
            jdaBuilder.addEventListeners(new MessageListener());

            COMMAND_MANAGER.initializeCommands();

            try {
                jdaBuilder.build();
                jdaBuilder.setAutoReconnect(true);
            } catch (LoginException loginException) {
                CrashLog.saveLogAsCrashLog(loginException, null);
            }
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Bot token is null!"), null);
        }
    }
}