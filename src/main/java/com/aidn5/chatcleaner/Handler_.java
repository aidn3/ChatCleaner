package com.aidn5.chatcleaner;

import java.util.List;
import java.util.regex.Matcher;

import com.aidn5.chatcleaner.config.Command;
import com.aidn5.chatcleaner.config.Config;
import com.aidn5.chatcleaner.gui.GuiSettings;
import com.aidn5.chatcleaner.services.ChatTriggers;
import com.aidn5.chatcleaner.services.ChatTriggers.Trigger;
import com.aidn5.chatcleaner.services.Looper;
import com.aidn5.chatcleaner.services.SettingsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;

public class Handler_ {
	private ChatTriggers ChatTriggers;
	public SettingsHandler settingsHandler;
	public Looper looper;
	public GuiSettings guiSettings;

	private List<Trigger> triggers = null;
	public boolean onHypixel = false;

	public Handler_() {
		ChatTriggers = new ChatTriggers(Minecraft.getMinecraft());
		settingsHandler = new SettingsHandler("main", Minecraft.getMinecraft());
		looper = new Looper();
		guiSettings = new GuiSettings(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/config/" + Config.AUTHOR
				+ "-" + Config.MODID + "/");

		triggers = ChatTriggers.getTriggers();
	}

	public boolean prepare() {
		if (!ChatTriggers.prepare()) {
			return false;
		}
		looper.runnableList.add(new Runnable() {
			@Override
			public void run() {
				if (!onHypixel) return;
				List<Trigger> triggers1 = ChatTriggers.getTriggers();
				triggers = triggers1;

			}
		});
		triggers = ChatTriggers.getTriggers();
		ClientCommandHandler.instance.registerCommand(new Command(new String[] { "Chatcleaner" }));
		return true;
	}

	public boolean matchRegex(String message) {
		if (!onHypixel) return false;
		try {
			int currentPriority = Integer.valueOf(settingsHandler.get("priority", "1"));
			for (Trigger trigger : triggers) {
				if (!checkSettings(trigger.Priority)) continue;

				Matcher matcher = trigger.pattern.matcher(message);
				if (matcher.find()) {
					if (!trigger.replaceWith.isEmpty()) {
						showMessage(trigger.replaceWith, Minecraft.getMinecraft());
					}
					return true;
				}
			}
		} catch (Exception ignored) {}
		return false;
	}

	private boolean checkSettings(int Priority) {
		switch (Priority) {
		case 1:
			return guiSettings.non_important_msg;
		case 2:
			return guiSettings.mid_important_msg;
		case 3:
			return guiSettings.high_important_msg;
		default:
			return false;
		}

	}

	public void showMessage(String message, Minecraft mc) {
		try {
			IChatComponent component = new ChatComponentText(message);

			ChatStyle style = new ChatStyle();
			style.setColor(EnumChatFormatting.DARK_AQUA);
			component.setChatStyle(style);

			mc.thePlayer.addChatMessage(component);
		} catch (Exception ignored) {}
	}

}
