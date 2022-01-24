package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.core.bot.DiscordBot;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.stream.Collectors;

public final class HelpDiscordCommand extends DiscordCommand {
    public HelpDiscordCommand () {
        super("help", "Shows the help usage of other commands!", "!help", new String[] {"?"}, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        // Creates a command help hashmap from the available commands.
        HashMap<String, String> commandHelpHashMap = DiscordBot.COMMAND_MANAGER.getDiscordCommandsArrayList().stream().collect(Collectors.toMap(DiscordCommand::getCommandName, discordCommand -> discordCommand.getCommandDescription() + " | " + discordCommand.getCommandSyntax(), (a, b) -> b, HashMap::new));

        // Sends an embed message.
        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Command - Help", "Command usage:", commandHelpHashMap, "\uD83D\uDC08 L U N A \uD83D\uDC08", Color.MAGENTA);
    }
}
