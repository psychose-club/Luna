package club.psychose.luna.core.bot.commands;

import club.psychose.luna.core.bot.button.DiscordButton;
import club.psychose.luna.enums.DiscordChannels;
import club.psychose.luna.enums.PermissionRoles;
import club.psychose.luna.utils.StringUtils;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class DiscordCommand {
    private final String commandName;
    private final String commandDescription;
    private final String commandSyntax;
    private final String[] aliases;
    private final PermissionRoles[] permissions;
    private final DiscordChannels[] discordChannels;
    private final HashMap<String, ArrayList<DiscordButton>> discordButtonsHashMap = new HashMap<>();

    public DiscordCommand (String commandName, String commandDescription, String commandSyntax, String[] aliases, PermissionRoles[] permissions, DiscordChannels[] discordChannels) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandSyntax = commandSyntax;
        this.aliases = aliases;
        this.permissions = permissions;
        this.discordChannels = discordChannels;
    }

    public abstract void onCommandExecution (String[] arguments, MessageReceivedEvent messageReceivedEvent);
    public void onButtonExecution (ButtonClickEvent buttonClickEvent) {}

    public void createNewButtonList (String listID) {
        // Checks if the list id not exists.
        if (!(this.discordButtonsHashMap.containsKey(listID))) {
            this.discordButtonsHashMap.put(listID, new ArrayList<>());
        } else {
            StringUtils.debug("Warning! The list with the ID " + listID + " already exists!");
        }
    }

    public void deleteButtonList (String listID) {
        // Removes the list.
        this.discordButtonsHashMap.remove(listID);
    }

    // Registers the button the list.
    public void registerButton (String listID, DiscordButton discordButton) {
        if (this.discordButtonsHashMap.containsKey(listID)) {
            this.discordButtonsHashMap.get(listID).add(discordButton);
        } else {
            StringUtils.debug("Warning! The list with the ID " + listID + " didn't exist!");
        }
    }

    public void deleteButton (String listID, DiscordButton discordButton) {
        if (this.discordButtonsHashMap.containsKey(listID)) {
            this.discordButtonsHashMap.get(listID).remove(discordButton);
        } else {
            StringUtils.debug("Warning! The list with the ID " + listID + " didn't exist!");
        }
    }

    public ArrayList<DiscordButton> getDiscordBotArrayList (String listID) {
        return this.discordButtonsHashMap.getOrDefault(listID, null);
    }

    public DiscordButton getDiscordButton (String listID, String buttonID) {
        return this.discordButtonsHashMap.getOrDefault(listID, null).stream().filter(discordButton -> discordButton.getButton().getId() != null).filter(discordButton -> discordButton.getButton().getId().equals(buttonID)).findFirst().orElse(null);
    }

    public String getCommandName () {
        return this.commandName;
    }

    public String getCommandDescription () {
        return this.commandDescription;
    }

    public String getCommandSyntax () {
        return this.commandSyntax;
    }

    public String[] getAliases () {
        return this.aliases;
    }

    public PermissionRoles[] getPermissions () {
        return this.permissions;
    }

    public DiscordChannels [] getDiscordChannels () {
        return this.discordChannels;
    }
}