package com.aidn5.chatcleaner.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.aidn5.chatcleaner.ChatCleaner;
import com.aidn5.chatcleaner.Handler_;
import com.aidn5.chatcleaner.services.ChatTriggers.Trigger;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

public class GuiSettings {
	private HashMap<String, ConfigCategory> caHashMap;

	public HashMap<String, ConfigCategory> getMessages() {
		List<Trigger> triggers = ChatCleaner.Handler_.triggers;

		caHashMap = new HashMap<String, ConfigCategory>();

		for (Trigger trigger : triggers) {
			String color = ChatFormatting.WHITE + "";
			if (trigger.Priority == 2) color = ChatFormatting.YELLOW + "";
			else if (trigger.Priority == 3) color = ChatFormatting.DARK_RED + "";

			ConfigCategory category;
			if (!caHashMap.containsKey(trigger.catogery)) {
				category = new ConfigCategory(trigger.catogery);
				caHashMap.put(trigger.catogery, category);
			} else {
				category = caHashMap.get(trigger.catogery);
			}

			category.put(trigger.ID, new Property(color + trigger.disc,
					ChatCleaner.Handler_.triggersRegex.get(trigger.ID, "false"), Type.BOOLEAN));

		}

		return caHashMap;
	}

	public void onConfigChange() {
		Handler_ handler_ = ChatCleaner.Handler_;
		if (caHashMap == null || caHashMap.size() == 0) return;

		for (Entry<String, ConfigCategory> category : caHashMap.entrySet()) {
			for (Entry<String, Property> setting : category.getValue().entrySet()) {
				handler_.triggersRegex.set(setting.getKey(), setting.getValue().getString(), false);
			}
		}
		handler_.triggersRegex.SaveUserSettings();
		handler_.triggers = handler_.ChatTriggers.getTriggers(false);
	}

}
