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

package club.psychose.luna.core.bot.filter;

import club.psychose.luna.Luna;

/*
 * This classes filters the phishing messages.
 */

public final class PhishingFilter {
    // Last detected link.
    private String lastDetectedLink = null;

    // This method checks if the message contains a phishing link.
    public boolean checkMessage (String message) {
        // Checks if the phishing protection was enabled.
        if (Luna.SETTINGS_MANAGER.getPhishingSettings().isPhishingProtectionEnabled()) {
            // Removes legit links to prevent false flags.
            // TODO: Adding custom whitelist. (Maybe next version this version it is a fast hotfix)
            if (message.contains("discordapp.com"))
                message = message.replace("discordapp.com", "");

            if (message.contains("discord.gift"))
                message = message.replace("discord.gift", "");

            if (message.contains("discord.gg"))
                message = message.replace("discord.gg", "");

            // The replacement will be called here, if we call it before it'll let the bots create exploits to bypass the protection.
            // We replace all spaces and all non-printable characters to prevent exploits.
            message = message.replaceAll(" ", "").replaceAll("\\p{C}", "").trim();

            // Checks if the domain list is enabled and if domains are available to check.
            if ((Luna.SETTINGS_MANAGER.getPhishingSettings().isDomainListEnabled()) && (Luna.SETTINGS_MANAGER.getPhishingSettings().getPhishingDomainsArrayList().size() != 0)) {
                for (String domain : Luna.SETTINGS_MANAGER.getPhishingSettings().getPhishingDomainsArrayList()) {
                    if (message.contains(domain)) {
                        this.lastDetectedLink = domain;
                        return false;
                    }
                }
            }

            // Checks if the suspicious domain list is enabled and if domains are available to check.
            if ((Luna.SETTINGS_MANAGER.getPhishingSettings().isSuspiciousListEnabled()) && (Luna.SETTINGS_MANAGER.getPhishingSettings().getPhishingDomainsSuspiciousArrayList().size() != 0)) {
                for (String suspiciousDomain : Luna.SETTINGS_MANAGER.getPhishingSettings().getPhishingDomainsSuspiciousArrayList()) {
                    if (message.contains(suspiciousDomain)) {
                        this.lastDetectedLink = suspiciousDomain;
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // This method returns the last detected link.
    public String getLastDetectedLink () {
        return this.lastDetectedLink;
    }
}