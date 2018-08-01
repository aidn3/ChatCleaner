package com.aidn5.chatcleaner.config;

import java.util.ArrayList;
import java.util.List;

import com.aidn5.chatcleaner.ChatCleaner;
import com.aidn5.chatcleaner.services.SettingsHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Command extends CommandBase {

	private String[] commands_name;

	public Command(String[] commands_name_) {
		commands_name = commands_name_;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		SettingsHandler settings = ChatCleaner.Handler_.settingsHandler;
		int current = Integer.valueOf(settings.get("priority", "1"));
		int changeTo = 0;
		if (current == 3) {
			changeTo = 0;
		} else {
			changeTo = current + 1;
		}
		sender.addChatMessage(
				new ChatComponentText(EnumChatFormatting.AQUA + Config.MOD_NAME + ": " + EnumChatFormatting.GRAY
						+ "switching to hide " + EnumChatFormatting.YELLOW + getPriorityName(changeTo)));
		settings.set("priority", changeTo + "");
	}

	private String getPriorityName(int stat) {
		switch (stat) {
		case 0:
			return "(disabled)";
		case 1:
		default:
			return "non-important-messages";
		case 2:
			return "mid-important-messages";
		case 3:
			return "really-important-messages";

		}
	}

	@Override
	public String getCommandName() {
		return "/" + commands_name[0];
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList();
		for (String command : commands_name) {
			aliases.add("/" + command);
		}
		return aliases;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	public boolean canSenderUseCommand(ICommandSender sender) {
		return true;
	}

	public void showMessage(String message, ICommandSender sender) {
		sender.addChatMessage(new ChatComponentText(message));
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

}
