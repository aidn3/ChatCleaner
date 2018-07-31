package com.aidn5.chatcleaner.gui;

import java.util.ArrayList;
import java.util.List;

import com.aidn5.chatcleaner.ChatCleaner;
import com.aidn5.chatcleaner.config.Config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiConfig_ extends GuiConfig {
	public GuiConfig_(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), Config.MODID, false, false, Config.MOD_NAME);
		titleLine2 = "Do //" + Config.MODID + " to override these settings";

	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (ConfigCategory configCategory : ChatCleaner.Handler_.guiSettings.getMessages().values()) {
			list.add(new ConfigElement(configCategory));
		}
		return list;
	}

}
