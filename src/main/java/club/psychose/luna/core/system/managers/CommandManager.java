package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.bot.commands.executables.HelpDiscordCommand;

import java.util.ArrayList;

public class CommandManager {
    private final ArrayList<DiscordCommand> discordCommandsArrayList = new ArrayList<>();

    public void initializeCommands () {
        this.discordCommandsArrayList.add(new HelpDiscordCommand());
    }

    public ArrayList<DiscordCommand> getDiscordCommandsArrayList () {
        return this.discordCommandsArrayList;
    }
}