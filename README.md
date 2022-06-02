# Luna
What is Luna?  
Luna is an open-source discord moderation bot with unique features.

## Table of contents
1. [System Requirements](#systemrequirements)
2. [Setup](#setup)
    1. [Bot Settings](#botsettings)
    2. [Message Filter Settings](#messagefiltersettings)
    3. [Mute Settings](#mutesettings)
    4. [MySQL Settings](#mysqlsettings)
    5. [Phishing Protection Settings](#phishingprotectionsettings)
    6. [Add a Discord server](#adddiscordserver)
3. [Improvements / Feature Requests / Issues / Security](#issues)
4. [Workspace](#gradleworkspace)
5. [Release mirror](#mirror)

#

#### System requirements <a name="systemrequirements"></a>

To run Luna you'll require to have Java 17 installed and a working MySQL database.  
Also the server / computer where Luna runs need to have a stable internet connection to connect with discord (obviously lmao)

#

#### Setup <a name="setup"></a>

The first setup of Luna requires some time, but it's not that hard.  
To create the json files you need to run the bot once and then go to the Luna folder. (Located under home and then under psychose.club)

These are the options in the json files:

###### Bot Settings <a name="botsettings"></a>
The bot settings has the following options:
- Bot Token (Your Discord Bot Token)
- YouTube API Key (Your Google / YouTube API Key (Google how to get one))
- Bot Owner ID (Your Discord Account ID to set you as Owner)
- Syntax Prefix (Bot syntax prefix for commands. Default: L!command)
- Time Period (Time period for the BotScheduler.java class.)
- Time Unit (Time unit for the BotScheduler.java class.)

###### Message Filter Settings <a name="messagefiltersettings"></a>
The message filter settings has the following options:
- Enable Blacklist (Enables or disables the blacklist for bad words)
    - Default lists:
        - Blacklist:
            - https://development.psychose.club/Luna/assets/blacklist
        - Whitelist:
            - https://development.psychose.club/Luna/assets/whitelist
        - Character filter:
            - https://development.psychose.club/Luna/assets/character_filter.json

- Custom Filters (Enable or disables custom filters)
- Custom Blacklist URL (URL to customize blacklist (Custom Filters needs to be enabled))
- Custom Whitelist URL (URL to customize whitelist (Custom Filters needs to be enabled))
- Custom Character Filter URL (URL to customize character filters (Custom Filters needs to be enabled))
- Fallback to default (When custom filters are enabled you can choose if the default lists should be used if the bot can't reach the custom lists.)

###### Mute Settings <a name="mutesettings"></a>
The mute settings has the following options:
- Enable Muting (Enables or disables muting)
- Warnings needed for mute (The warnings needed to mute a member)
- Mute Time
- Mute Time Unit
- Time to reset all mutes
- Reset Time Unit

###### MySQL Settings <a name="mysqlsettings"></a>
The MySQL settings has the following options:
- MySQL Hostname
- MySQL Port
- MySQL Username
- MySQL Password
- MySQL Database Name
- MySQL JDBC URL
- MySQL Cache Prepared Statement
- MySQL Prepared Statement Cache Size
- MySQL Prepared Statement Cache SQL Limit
- MySQL Minimum Idle
- MySQL Maximum Pool Size
- MySQL Idle Timeout
- MySQL Leak Detection Threshold
- MySQL Connection Timeout
- MySQL Validation Timeout
- MySQL Max Lifetime

###### Phishing Protection Settings <a name="phishingprotectionsettings"></a>
The Phishing protection settings has the following options:
- Enable Phishing Protection (Enables or disables the protection)
- Block domains (Block phishing domains)
- Block suspicious domains (Block domains that could be phishing, but it isn't 100% sure)
- Auto-Mute (Auto-Mutes member)
- Auto-Ban (Auto-Ban member)
- Domain List (URL to domain list)
- Suspicious Domain List (URL to suspicious domain list (amogus))

###### Add a Discord server <a name="adddiscordserver"></a>
To add a Discord you need to execute as bot owner the following command:
``{Command Prefix}serverconfiguration add <Server ID> | <Owner Role ID> <Admin Role ID> <Moderator Role ID> <Verification Role ID> <Bot Information Channel ID> <Logging Channel ID> <Verification Channel ID>``
#

#### Improvements / Feature Requests / Issues / Security <a name="issues"></a>
We appreciate every help, report and feature request.  
Please create a detailed issue in Issues about a bug or feature request.  
Please report security vulnerabilities directly to us on our discord server!!!
- https://www.psychose.club/discord
#

#### Workspace <a name="gradleworkspace"></a>
To contribute you need to fork the project and please create pull requests to the development branch.  
If you didn't create a pull request to the development branch it'll be declined!  

The project runs with gradle, if your IDE didn't import the project look at: (https://gradle.org/install/)  
#

#### Mirror <a name="mirror"></a>

https://development.psychose.club/Luna/releases/1.1.3/Luna.jar    

Also, if you want to donate please join our discord server and message us.  
Stay safe and never give up <3