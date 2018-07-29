package com.aidn5.chatcleaner.gui;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class GuiSettings {

	public Configuration main_settings;

	public boolean non_important_msg = true;
	public boolean mid_important_msg = false;
	public boolean high_important_msg = false;

	public GuiSettings(String path) {
		main_settings = new Configuration(new File(path + "main_settings.cfg"));
		main_settings = new Configuration(new File(path + "regex_settings.cfg"));
		syncConfig();
	}

	public void onConfigChange() {
		main_settings.save();
		syncConfig();
	}

	public void syncConfig() {
		String settingsCategory = "Settings";
		String regexCategory = "Messages";

		main_settings.addCustomCategoryComment(settingsCategory, "Change main mod's settings");
		non_important_msg = main_settings.getBoolean("non-important-messages", settingsCategory, true,
				"Hiding non-important-messages in-game");

		mid_important_msg = main_settings.getBoolean("mid-important-messages", settingsCategory, false,
				"Hiding mid-important-messages in-game");

		high_important_msg = main_settings.getBoolean("high-important-messages", settingsCategory, false,
				"Hiding mid-important-messages in-game");

		main_settings.save();
	}

	public void resetConfig() {
		mid_important_msg = true;
		mid_important_msg = true;
		mid_important_msg = true;
		main_settings.save();
		syncConfig();
	}
}
