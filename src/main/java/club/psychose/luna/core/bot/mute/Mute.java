package club.psychose.luna.core.bot.mute;

import club.psychose.luna.core.logging.CrashLog;
import club.psychose.luna.utils.DiscordUtils;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Mute {
    private final ArrayList<Member> mutedMemberArrayList = new ArrayList<>();
    private final HashMap<Member, Integer> memberMuteCountHashMap = new HashMap<>();

    public void addMuteCount (Member member) {
        int muteCount = this.memberMuteCountHashMap.getOrDefault(member, 0);

        if (muteCount >= 3) {
             if (this.mutedMemberArrayList.contains(member)) {
                 this.mutedMemberArrayList.add(member);

                 String timestamp = StringUtils.getDateAndTime("LOG");
                 HashMap<String, String> fieldHashMap = new HashMap<>();
                 fieldHashMap.put("Member", member.getAsMention());
                 fieldHashMap.put("Mute Count", String.valueOf(this.getMuteCount(member)));
                 fieldHashMap.put("Timestamp", timestamp);
                 fieldHashMap.put("Mute Time", "1 Hour");

                 member.getJDA().getGuilds().stream().filter(guild -> guild.isMember(member.getUser())).forEachOrdered(guild -> DiscordUtils.sendLoggingMessage(guild.getId(), "Member muted!", fieldHashMap, "SYSTEM", guild.getTextChannels()));

                 if (member.getUser().hasPrivateChannel())
                     DiscordUtils.sendEmbedMessage(member.getUser(), "You got muted!", "You got automatically muted from any server that contains this bot!\nReason:\nMultiple usages of inappropriate words!\n\nIf you think this is a mistake please contact the administration!", fieldHashMap, "SYSTEM", Color.RED);

                 new Thread(() -> {
                     try {
                         Thread.sleep(TimeUnit.HOURS.toMillis(1));
                     } catch (InterruptedException interruptedException) {
                         CrashLog.saveLogAsCrashLog(interruptedException, member.getJDA().getGuilds());
                     }

                     this.mutedMemberArrayList.remove(member);

                     if (member.getUser().hasPrivateChannel())
                         DiscordUtils.sendEmbedMessage(member.getUser(), "Your mute expired!", "Your mute expired!\nPlease behave nicely now!\nFuture mutes might be increase the time of muting!", fieldHashMap, "SYSTEM", Color.GREEN);
                 }).start();
             }
        }

        muteCount ++;

        if (this.memberMuteCountHashMap.containsKey(member)) {
            this.memberMuteCountHashMap.replace(member, muteCount);
        } else {
            this.memberMuteCountHashMap.put(member, muteCount);
        }
    }

    public boolean isMemberMuted (Member member) {
        return this.mutedMemberArrayList.contains(member);
    }

    public int getMuteCount (Member member) {
        return this.memberMuteCountHashMap.getOrDefault(member, 0);
    }
}