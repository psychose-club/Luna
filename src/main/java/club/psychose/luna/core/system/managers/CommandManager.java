package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.commands.executables.*;

import java.util.ArrayList;

public final class CommandManager {
    private final ArrayList<DiscordCommand> discordCommandsArrayList = new ArrayList<>();

    public void initializeCommands () {
        this.discordCommandsArrayList.add(new ClearTempFolderDiscordCommand());
        this.discordCommandsArrayList.add(new HelpDiscordCommand());
        this.discordCommandsArrayList.add(new MusicPlayerDiscordCommand());
        this.discordCommandsArrayList.add(new NukeDiscordCommand());
        this.discordCommandsArrayList.add(new ReloadDiscordCommand());
        this.discordCommandsArrayList.add(new ViewLogsDiscordCommand());
        this.discordCommandsArrayList.add(new VerificationDiscordCommand());
    }

    public ArrayList<DiscordCommand> getDiscordCommandsArrayList () {
        return this.discordCommandsArrayList;
    }
}