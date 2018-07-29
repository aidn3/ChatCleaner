package com.aidn5.chatcleaner.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.aidn5.chatcleaner.ChatCleaner;
import com.aidn5.chatcleaner.config.Config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiConfig_ extends GuiConfig {
	public GuiConfig_(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), Config.MODID, false, false, Config.MOD_NAME);
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		Set<String> gategoriesName = ChatCleaner.Handler_.guiSettings.main_settings.getCategoryNames();
		for (String gategoryName : gategoriesName) {
			list.add(new ConfigElement(ChatCleaner.Handler_.guiSettings.main_settings.getCategory(gategoryName)));
		}
		return list;
	}

}
