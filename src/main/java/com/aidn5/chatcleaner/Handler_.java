package com.aidn5.chatcleaner;

import java.util.List;
import java.util.regex.Matcher;

import com.aidn5.chatcleaner.config.Command;
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
	public ChatTriggers ChatTriggers;
	public SettingsHandler settingsHandler;
	public SettingsHandler triggersRegex;
	public Looper looper;
	public GuiSettings guiSettings;

	public List<Trigger> triggers = null;
	public boolean onHypixel = false;

	public Handler_() {
		ChatTriggers = new ChatTriggers(Minecraft.getMinecraft());
		settingsHandler = new SettingsHandler("main", Minecraft.getMinecraft());
		triggersRegex = new SettingsHandler("triggersRegex", Minecraft.getMinecraft());
		looper = new Looper();
		guiSettings = new GuiSettings();

	}

	public boolean prepare() {
		if (!ChatTriggers.prepare()) {
			return false;
		}
		triggers = ChatTriggers.getTriggers(false);

		looper.runnableList.add(new Runnable() {
			@Override
			public void run() {
				if (!onHypixel) return;
				List<Trigger> triggers1 = ChatTriggers.getTriggers(true);
				triggers = triggers1;

			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				triggers = ChatTriggers.getTriggers(true);
			}
		}).start();

		ClientCommandHandler.instance.registerCommand(new Command(new String[] { "Chatcleaner" }));
		return true;
	}

	public boolean matchRegex(String message) {
		if (!onHypixel) return false;
		double timeNeeded = System.currentTimeMillis();
		try {
			int currentPriority = Integer.valueOf(settingsHandler.get("priority", "1"));
			for (Trigger trigger : triggers) {
				if (currentPriority == 0) {
					if (!trigger.enabled) continue;
				} else {
					if (currentPriority < trigger.Priority) continue;
				}

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
