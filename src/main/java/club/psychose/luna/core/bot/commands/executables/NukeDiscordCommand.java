package club.psychose.luna.core.bot.commands.executables;

import club.psychose.luna.core.bot.commands.DiscordCommand;
import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.core.logging.NukeLog;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.DiscordUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public final class NukeDiscordCommand extends DiscordCommand {
    public NukeDiscordCommand () {
        super("nuke", "Deletes the complete channel history from a channel.", "!nuke", new String[] {"clear"}, new PermissionRoles[] {PermissionRoles.OWNER, PermissionRoles.ADMIN, PermissionRoles.MODERATOR}, new DiscordChannels[] {DiscordChannels.ANY_CHANNEL});
    }

    @Override
    public void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getMember() != null) {
            NukeLog.saveNukeLog(messageReceivedEvent.getTextChannel(), messageReceivedEvent.getMember());

            messageReceivedEvent.getTextChannel().sendMessage("Channel hit by a nuke :(\nhttps://media.giphy.com/media/3o7abwbzKeaRksvVaE/giphy.gif").queue();

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
            } catch (InterruptedException ignored) {}

            DiscordUtils.deleteChannelHistory(messageReceivedEvent.getGuild().getId(), messageReceivedEvent.getTextChannel());
        } else {
            CrashLog.saveLogAsCrashLog(new NullPointerException("Member not found!"), messageReceivedEvent.getJDA().getGuilds());
        }
    }
}