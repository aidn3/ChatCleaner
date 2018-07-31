package com.aidn5.chatcleaner.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import com.aidn5.chatcleaner.config.Config;

import net.minecraft.client.Minecraft;

public class SettingsHandler {
	public boolean DIR_CHECKED = false;
	public Properties settings_data;

	public String CONFIG_DIR;
	public String LOCAL_SET;
	public boolean IN_JAR = false;

	public SettingsHandler(String settings_path, Minecraft mc) {
		CONFIG_DIR = mc.mcDataDir.getAbsolutePath() + "/config/" + Config.AUTHOR + "-" + Config.MODID + "/";
		LOCAL_SET = CONFIG_DIR + settings_path + ".cfg";

		checkDir();
		settings_data = new Properties();
		reloadUserSettings();
	}

	public SettingsHandler(String settings_path, boolean inJar) {
		IN_JAR = true;
		LOCAL_SET = settings_path;
		DIR_CHECKED = false;

		settings_data = new Properties();
		reloadUserSettings();

	}

	public String get(String key, String default_) {
		try {
			String value = settings_data.getProperty(key);
			if (value == null || value.isEmpty()) return default_;
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean set(String key, String value) {
		return set(key, value, true);
	}

	public boolean set(String key, String value, boolean save) {
		try {
			settings_data.put(key, value);

			if (IN_JAR) return true;
			return (save ? SaveUserSettings() : true);
		} catch (Exception e) {}
		return false;
	}

	public boolean clear() {
		try {
			settings_data = new Properties();

			if (IN_JAR) return true;
			return SaveUserSettings();
		} catch (Exception ignore) {}
		return false;
	}

	public boolean reloadUserSettings() {
		if (!checkDir() && !IN_JAR) return false;
		try {
			if (IN_JAR) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						getClass().getClassLoader().getResourceAsStream(LOCAL_SET), "UTF-8");

				BufferedReader readIn = new BufferedReader(inputStreamReader);
				settings_data.load(readIn);

			} else {
				InputStream inputstream = new FileInputStream(LOCAL_SET);
				settings_data.load(inputstream);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean SaveUserSettings() {
		if (!checkDir() || IN_JAR) return false;
		try {
			OutputStream out = new FileOutputStream(LOCAL_SET);
			settings_data.store(out, "localsettings");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkDir() {
		if (IN_JAR) return false;
		if (DIR_CHECKED) return true;
		try {
			File dir = new File(CONFIG_DIR);
			if (!dir.exists()) {
				if (dir.mkdirs()) throw new Exception("unable to make dir");
			}
			if (!dir.isDirectory()) throw new Exception("PATH is NOT DIR");
			File settings_file = new File(LOCAL_SET);
			if (!settings_file.exists()) {
				if (!settings_file.createNewFile()) {

					throw new Exception("Cannot create settings file for " + settings_file.getName());
				}
			}
		} catch (Exception e) {
			return false;
		}

		DIR_CHECKED = true;
		return true;
	}
}
