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

package club.psychose.luna.core.system.settings;

import java.util.ArrayList;

/*
 * This class contains the settings for the phishing protection usage.
 */

public final class PhishingSettings {
    private final ArrayList<String> phishingDomainsArrayList = new ArrayList<>();
    private final ArrayList<String> phishingDomainsSuspiciousArrayList = new ArrayList<>();

    // These are the default values for the settings.
    private boolean enablePhishingProtection = true;
    private boolean enableDomainList = true;
    private boolean enableSuspiciousList = true;
    private boolean enableAutomaticMute = true;
    private boolean enableAutomaticBan = false;
    private String domainListURL = "https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/txt/domain-list.txt";
    private String suspiciousListURL = "https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/txt/suspicious-list.txt";
    
    public void setEnablePhishingProtection (boolean value) {
        this.enablePhishingProtection = value;
    }

    public void setEnableDomainList (boolean value) {
        this.enableDomainList = value;
    }

    public void setEnableSuspiciousList (boolean value) {
        this.enableSuspiciousList = value;
    }

    public void setEnableAutomaticMute (boolean value) {
        this.enableAutomaticMute = value;
    }

    public void setEnableAutomaticBan (boolean value) {
        this.enableAutomaticBan = value;
    }

    public void setDomainListURL (String value) {
        this.domainListURL = value;
    }

    public void setSuspiciousListURL (String value) {
        this.suspiciousListURL = value;
    }

    public boolean isPhishingProtectionEnabled () {
        return this.enablePhishingProtection;
    }

    public boolean isDomainListEnabled () {
        return this.enableDomainList;
    }

    public boolean isSuspiciousListEnabled () {
        return this.enableSuspiciousList;
    }

    public boolean isAutomaticMuteEnabled () {
        return this.enableAutomaticMute;
    }

    public boolean isAutomaticBanEnabled () {
        return this.enableAutomaticBan;
    }

    public String getDomainListURL () {
        return this.domainListURL;
    }

    public String getSuspiciousListURL () {
        return this.suspiciousListURL;
    }

    public ArrayList<String> getPhishingDomainsArrayList () {
        return this.phishingDomainsSuspiciousArrayList;
    }

    public ArrayList<String> getPhishingDomainsSuspiciousArrayList () {
        return this.phishingDomainsSuspiciousArrayList;
    }
}