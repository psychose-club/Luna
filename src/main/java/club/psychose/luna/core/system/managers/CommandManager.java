package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.commands.executables.HelpDiscordCommand;
import club.psychose.luna.core.bot.commands.executables.NukeDiscordCommand;
import club.psychose.luna.core.bot.commands.executables.ReloadDiscordCommand;

import java.util.ArrayList;

public class CommandManager {
    private final ArrayList<DiscordCommand> discordCommandsArrayList = new ArrayList<>();

    public void initializeCommands () {
        this.discordCommandsArrayList.add(new HelpDiscordCommand());
        this.discordCommandsArrayList.add(new NukeDiscordCommand());
        this.discordCommandsArrayList.add(new ReloadDiscordCommand());
    }

    public ArrayList<DiscordCommand> getDiscordCommandsArrayList () {
        return this.discordCommandsArrayList;
    }
}