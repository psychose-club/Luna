package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.Luna;
import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public final class ReloadDiscordCommand extends DiscordCommand {
    public ReloadDiscordCommand () {
        super("reload", "Reload specific configurations", "!reload <filter | settings>", new String[] {"rl", "rel"}, new PermissionRoles[] {PermissionRoles.EVERYONE}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getAuthor().getId().equals("321249545394847747")) {
            if ((arguments != null) && (arguments.length == 1)) {
                String mode = arguments[0].trim();

                switch (mode) {
                    case "filter" -> {
                        Luna.SETTINGS_MANAGER.loadFilterSettings();
                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Filter reloaded!", "Filter successfully reloaded!", null, "uwu", Color.GREEN);
                    }

                    case "settings" -> {
                        Luna.SETTINGS_MANAGER.loadSettings();
                        DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Settings reloaded!", "Settings successfully reloaded!", null, "uwu", Color.GREEN);
                    }

                    default -> DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid mode!", "Please check the syntax!", null, "oh no qwq", Color.RED);
                }
            } else {
                DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid arguments!", "Please check the syntax!", null, "oh no qwq", Color.RED);
            }
        } else {
            DiscordUtils.sendEmbedMessage(messageReceivedEvent.getTextChannel(), "Invalid permissions!", "You didn't have permissions to execute this command!", null, "oh no qwq", Color.RED);
        }
    }
}