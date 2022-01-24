package club.psychose.luna.core.captcha;

import net.dv8tion.jda.api.entities.Member;

import java.io.File;

public record Captcha (String getServerID, File getImageFile, String getCaptchaCode, Member getMember) {}