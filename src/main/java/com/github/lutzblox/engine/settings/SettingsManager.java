package com.github.lutzblox.engine.settings;

import java.util.HashMap;
import java.util.Map;

import com.github.lutzblox.engine.LutzEngine;
import com.github.lutzblox.engine.exceptions.SettingException;

public class SettingsManager {

	public static class Category {

		private String name;

		private Category(String name) {

			this.name = name;
		}

		public String getName() {

			return name;
		}
	}

	public static class Key {

		private Category category;
		private String key;

		private Key(Category category, String key) {

			this.category = category;
			this.key = key;
		}

		public Category getCategory() {

			return category;
		}

		public String getKey() {

			return key;
		}
	}

	public static class Categories {

		public static final Category GRAPHICS = new Category("graphics");
	}

	public static class Keys {

		// Organized by category
		// Named CATEGORY_NAMEOFSETTING
		// Key should be categoryprefix_nameofsetting
		public static final Key GRAPHICS_ANTIALIASING = new Key(Categories.GRAPHICS, "grph_antialiasing");
		public static final Key GRAPHICS_MAXIMUM_FPS = new Key(Categories.GRAPHICS, "grph_maxfps");
		public static final Key GRAPHICS_SCREENMODE = new Key(Categories.GRAPHICS, "grph_screenmode");
	}

	private Map<Key, Object> settings = new HashMap<Key, Object>();

	private Category accepted;

	public SettingsManager(Category accepted) {

		this.accepted = accepted;
	}

	public void addSetting(Key key, Object defaultValue) {

		if (key.getCategory() == accepted) {

			settings.put(key, defaultValue);

			LutzEngine.getEngineLogger().debug("Setting '" + key.getKey() + "' for settings manager '" + accepted.getName()
					+ "' added with default value: " + defaultValue);

		} else {

			throw SettingException.unacceptedKey(key, accepted);
		}
	}

	public void changeSetting(Key key, Object value) {

		if (settings.containsKey(key)) {

			settings.put(key, value);

			LutzEngine.getEngineLogger()
					.debug("Setting '" + key.getKey() + "' for settings manager '" + accepted.getName() + "' set to: " + value);
		}
	}

	public Object retrieveSetting(Key key) {

		return settings.get(key);
	}

	public Category getAcceptedCategory() {

		return accepted;
	}
}
