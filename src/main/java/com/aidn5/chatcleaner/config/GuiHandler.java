package com.aidn5.chatcleaner.config;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aidn5.chatcleaner.services.SettingsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiHandler extends GuiScreen {
	private int White = Color.WHITE.getRGB();
	private FontRenderer fontRender;
	private List<List<String>> hoverText;
	private SettingsHandler settings;
	private int Width;
	private int Height;

	public GuiHandler(SettingsHandler settings) {
		this.settings = settings;
		Minecraft minecraft = Minecraft.getMinecraft();
		fontRender = minecraft.fontRendererObj;
		ScaledResolution scaled = new ScaledResolution(minecraft);
		Width = scaled.getScaledWidth();
		Height = scaled.getScaledHeight();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		drawCenteredString(fontRender, Config.MOD_NAME, this.Width / 2, 5, White);
		drawCenteredString(fontRender, "Version " + Config.VERSION + " by " + Config.AUTHOR, Width / 2, 16, White);

		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i) instanceof GuiButton) {
				GuiButton btn = buttonList.get(i);
				if (btn.isMouseOver()) {
					drawHoveringText(hoverText.get(i), mouseX, mouseY);
				}
			}
		}
	}

	private List<String> toolTipText(String[] string) {
		List<String> list = new ArrayList<String>();
		for (String tip : string) {
			list.add(tip);
		}
		return list;
	}

	@Override
	public void initGui() {
		initGui_(false);
		super.initGui();
	}

	public void initGui_(boolean refresh) {
		if (!refresh && buttonList != null && buttonList.size() != 0)
			return;
		buttonList = new ArrayList();
		hoverText = new ArrayList();

		buttonList.add(new GuiButton(1, width / 2 - 70, height / 2 - 50, 140, 20,
				"important messages" + checkStatus(settings.get("high-important-msg", "ON").equals("ON"))));
		hoverText.add(toolTipText(new String[] { "idk..." }));

		buttonList.add(new GuiButton(2, width / 2 - 70, height / 2 - 28, 140, 20,
				"mid-important messages" + checkStatus(settings.get("mid-important-msg", "ON").equals("ON"))));
		hoverText.add(toolTipText(new String[] { "'CROSS TEAMING...',etc." }));

		buttonList.add(new GuiButton(3, width / 2 - 70, height / 2 - 6, 140, 20,
				"non-important messages" + checkStatus(settings.get("non-important-msg", "ON").equals("ON"))));
		hoverText.add(toolTipText(new String[] { "'You purchased ****', etc." }));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.enabled) {
			if (button.id == 1) {
				settings.set("high-important-msg",
						(settings.get("high-important-msg", "ON").equals("ON") ? "OFF" : "ON"));

			} else if (button.id == 2) {
				settings.set("mid-important-msg",
						(settings.get("mid-important-msg", "ON").equals("ON") ? "OFF" : "ON"));

			} else if (button.id == 3) {
				settings.set("non-important-msg",
						(settings.get("non-important-msg", "ON").equals("ON") ? "OFF" : "ON"));
			}
		}
	}

	public String checkStatus(boolean status) {
		if (status)
			return " (on)";
		return " (off)";
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		Minecraft.getMinecraft().displayGuiScreen(null);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}