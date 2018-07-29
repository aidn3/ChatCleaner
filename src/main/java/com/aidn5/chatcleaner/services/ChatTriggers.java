package com.aidn5.chatcleaner.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.aidn5.chatcleaner.config.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;

public class ChatTriggers {
	String PATH_CACHE = null;
	Settings settings;
	JsonObject cache_settings;
	List<Trigger> default_settings;

	public ChatTriggers(Minecraft mc) {
		settings = new Settings();
		PATH_CACHE = mc.mcDataDir.getAbsolutePath() + "/config/" + Config.AUTHOR + "-" + Config.MODID + "/cache.json";
	}

	public boolean prepare() {

		default_settings = new JsonParser_().parse(settings.LoadDefaultSettings());

		if (default_settings == null) {
			return false;
		}
		return true;
	}

	public List<Trigger> getTriggers() {
		JsonParser_ jsonParser_ = new JsonParser_();
		List<Trigger> triggers = null;
		try {
			String rawTriggers = IOUtils.toString(new URL(Config.updateURL));
			triggers = jsonParser_.parse(rawTriggers);
			if (triggers != null) {
				settings.SaveCacheSettings(rawTriggers, PATH_CACHE);
				return triggers;
			}
		} catch (Exception e) {}

		cache_settings = settings.LoadCacheSettings(PATH_CACHE);
		triggers = jsonParser_.parse(cache_settings);
		if (triggers != null) return triggers;

		return default_settings;

	}

	private class JsonParser_ {
		List<Trigger> parse(String string) {
			if (string == null || string.isEmpty()) return null;
			List<Trigger> tiggersArray = null;
			try {
				return parse(new JsonParser().parse(string).getAsJsonObject());
			} catch (Exception ignored) {}
			return null;
		}

		List<Trigger> parse(JsonObject jsonObject) {
			if (jsonObject == null) return null;
			try {
				JsonArray tiggersJson = jsonObject.getAsJsonArray("triggers");

				List<Trigger> tiggersArray = new ArrayList<Trigger>();
				Trigger trigger;
				for (JsonElement jsonElement2 : tiggersJson) {
					try {
						trigger = new Trigger();
						trigger.pattern = Pattern.compile(jsonElement2.getAsJsonObject().get("regex").getAsString());
						trigger.replaceWith = jsonElement2.getAsJsonObject().get("replaceWith").getAsString();
						trigger.Priority = jsonElement2.getAsJsonObject().get("priority").getAsInt();
						trigger.ID = jsonElement2.getAsJsonObject().get("id").getAsString();
						tiggersArray.add(trigger);
					} catch (Exception ignored) {}
				}
				return tiggersArray;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
			return null;
		}
	}

	private class Settings {
		JsonObject LoadDefaultSettings() {
			JsonObject jsonObject = null;
			try {
				InputStreamReader inputStreamReader = new InputStreamReader(
						getClass().getClassLoader().getResourceAsStream("assets/default_settings.json"), "UTF-8");

				BufferedReader readIn = new BufferedReader(inputStreamReader);
				StringBuilder response = new StringBuilder("");

				String inputLine = "";
				while ((inputLine = readIn.readLine()) != null)
					response.append(inputLine);

				jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
				if (jsonObject == null) throw new Exception("Can't parse default_Settings");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonObject;
		}

		JsonObject LoadCacheSettings(String PATH) {
			JsonObject jsonObject = null;
			try {
				BufferedReader br = new BufferedReader(new FileReader(PATH));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				br.close();

				jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();

			} catch (Exception e) {}
			return jsonObject;
		}

		void SaveCacheSettings(String data, String PATH) {
			try {
				PrintWriter writer = new PrintWriter(PATH, "UTF-8");
				writer.println(data);
				writer.close();
			} catch (Exception e) {}
		}
	}

	public class Trigger {
		public Pattern pattern = null;
		public String replaceWith = "";
		public int Priority = 1;
		public String ID = "";
	}
}
