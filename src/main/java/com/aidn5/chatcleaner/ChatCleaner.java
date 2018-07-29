package com.aidn5.chatcleaner;

import com.aidn5.chatcleaner.config.Config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid = Config.MODID, version = Config.VERSION, name = Config.MOD_NAME, clientSideOnly = true, guiFactory = "com.aidn5.chatcleaner.gui.GuiFactory")
public class ChatCleaner {
	public static Handler_ Handler_;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		Handler_ = new Handler_();
		if (Handler_.prepare()) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerChatReceive(ClientChatReceivedEvent event) {
		if (event.type != 0) return;

		String message = event.message.getUnformattedText();
		if (Handler_.matchRegex(message)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void playerLoggedIn(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			boolean b = mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
			Handler_.onHypixel = b;
		} catch (Exception ignore) {}
	}

	@SubscribeEvent
	public void onLoggedOut(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		Handler_.onHypixel = false;
	}

	@SubscribeEvent
	public void onGameTick(TickEvent.ClientTickEvent event) {
		Handler_.looper.doTick();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(Config.MODID)) {
			Handler_.guiSettings.onConfigChange();
		}
	}

}
