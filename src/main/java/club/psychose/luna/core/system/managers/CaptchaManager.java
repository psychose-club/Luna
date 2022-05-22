/*
 * Copyright © 2022 psychose.club
 * Discord: https://www.psychose.club/discord
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.psychose.luna.core.system.managers;

import club.psychose.luna.core.captcha.Captcha;
import club.psychose.luna.core.captcha.CaptchaGenerator;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

/*
 * This class manages the opened captchas.
 */

public final class CaptchaManager {
    private final ArrayList<Captcha> captchaArrayList = new ArrayList<>();
    private final CaptchaGenerator captchaGenerator = new CaptchaGenerator();

    // This method adds a captcha to the ArrayList.
    public void addCaptcha (Captcha captcha) {
        if (!(this.captchaArrayList.contains(captcha)))
            this.captchaArrayList.add(captcha);
    }

    // This method removes a captcha from the ArrayList.
    public void removeCaptcha (Captcha captcha) {
        this.captchaArrayList.remove(captcha);
    }

    // This method returns the captchas.
    public ArrayList<Captcha> getCaptchaArrayList () {
        return this.captchaArrayList;
    }

    // This method checks if a member has a captcha opened.
    public boolean hasMemberACaptcha (Member member) {
        return ((member != null) && (this.captchaArrayList.stream().anyMatch(captcha -> captcha.getMember().getId().equals(member.getId()))));
    }

    // This method checks if a user has a captcha opened.
    public boolean hasMemberACaptcha (User user) {
        return ((user != null) && (this.captchaArrayList.stream().anyMatch(captcha -> captcha.getMember().getId().equals(user.getId()))));
    }

    // This method returns the member captcha.
    public Captcha getMemberCaptcha (User user) {
        return this.captchaArrayList.stream().filter(captcha -> captcha.getMember().getId().equals(user.getId())).findFirst().orElse(null);
    }

    // This method returns the captcha generator.
    public CaptchaGenerator getCaptchaGenerator () {
        return this.captchaGenerator;
    }
}