package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.core.captcha.CaptchaGenerator;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public final class CaptchaManager {
    private final ArrayList<Captcha> captchaArrayList = new ArrayList<>();
    private final CaptchaGenerator captchaGenerator = new CaptchaGenerator();

    public void addCaptcha (Captcha captcha) {
        if (!(this.captchaArrayList.contains(captcha)))
            this.captchaArrayList.add(captcha);
    }

    public void removeCaptcha (Captcha captcha) {
        this.captchaArrayList.remove(captcha);
    }

    public ArrayList<Captcha> getCaptchaArrayList () {
        return this.captchaArrayList;
    }

    public boolean hasMemberACaptcha (Member member) {
        return ((member != null) && (this.captchaArrayList.stream().anyMatch(captcha -> captcha.getMember().getId().equals(member.getId()))));
    }

    public boolean hasMemberACaptcha (User user) {
        return ((user != null) && (this.captchaArrayList.stream().anyMatch(captcha -> captcha.getMember().getId().equals(user.getId()))));
    }

    public Captcha getMemberCaptcha (User user) {
        return this.captchaArrayList.stream().filter(captcha -> captcha.getMember().getId().equals(user.getId())).findFirst().orElse(null);
    }

    public CaptchaGenerator getCaptchaGenerator () {
        return this.captchaGenerator;
    }
}