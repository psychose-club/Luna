package club.psychose.luna.core.bot.button;

import club.psychose.luna.core.logging.CrashLog;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.util.concurrent.TimeUnit;

public final class DiscordButton {
    private final Button button;
    private final boolean requiresButtonOwner;

    private boolean timeoutEnabled = false;
    private boolean buttonActivated = false;
    private Member buttonOwner = null;
    private Thread buttonTimeOutThread = null;

    public DiscordButton (String buttonText, String buttonID, ButtonStyle buttonStyle, boolean requiresButtonOwner) {
        this.button = Button.of(buttonStyle, buttonID, buttonText);
        this.requiresButtonOwner = requiresButtonOwner;
    }

    public DiscordButton (String buttonText, String buttonID, Emoji emoji, ButtonStyle buttonStyle, boolean requiresButtonOwner) {
        this.button = Button.of(buttonStyle, buttonID, buttonText, emoji);
        this.requiresButtonOwner = requiresButtonOwner;
    }

    public void activateButtonTimeout () {
        if (!(this.timeoutEnabled)) {
            this.timeoutEnabled = true;
            this.activateButton();

            this.buttonTimeOutThread = new Thread(() -> {
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(2));
                } catch (InterruptedException interruptedException) {
                    CrashLog.saveLogAsCrashLog(interruptedException, null);
                }

                this.deactivateButtonTimeout();
            });

            this.buttonTimeOutThread.start();
        } else {
            this.deactivateButtonTimeout();
            this.activateButtonTimeout();
        }
    }

    public void deactivateButtonTimeout () {
        this.timeoutEnabled = false;
        this.setButtonOwner(null);
        this.deactivateButton();

        if (this.buttonTimeOutThread != null)
            this.buttonTimeOutThread = null;
    }

    public void setButtonOwner (Member member) {
        this.buttonOwner = member;
    }

    private void activateButton () {
        this.buttonActivated = true;
    }

    private void deactivateButton () {
        this.buttonActivated = false;
    }

    public boolean isButtonActivated () {
        return this.buttonActivated;
    }

    public boolean isMemberButtonOwner (Member member) {
        return ((this.requiresButtonOwner) && (this.buttonOwner != null) && (this.buttonOwner.getId().equals(member.getId())));
    }

    public Button getButton () {
        return this.button;
    }
}