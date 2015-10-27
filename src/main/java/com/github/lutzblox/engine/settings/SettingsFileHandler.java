package com.github.lutzblox.engine.settings;

import java.io.File;
import java.io.IOException;

import com.github.lutzblox.engine.LutzEngine;
import com.github.lutzblox.engine.settings.SettingsManager.Keys;
import com.github.lutzblox.engine.xml.readers.DefaultXMLReader;
import com.github.lutzblox.engine.xml.readers.ReaderParameters;
import com.github.lutzblox.engine.xml.trees.XMLTag;
import com.github.lutzblox.engine.xml.trees.XMLTree;
import com.github.lutzblox.engine.xml.writers.DefaultXMLWriter;

public class SettingsFileHandler {

	public static void loadSettings() throws IOException {

		File settings = new File("settings/settings.xml");

		if (settings.exists()) {

			ReaderParameters readParams = new ReaderParameters();
			readParams.setIgnoreComments(true);
			readParams.setIgnoreWhitespace(false);

			XMLTree tree = new DefaultXMLReader(readParams).read(settings);

			if (tree.getRoot().getName().equals("settings")) {

				for (XMLTag subtype : tree.getRoot().getChildren()) {

					String catName = subtype.getName();

					if (LutzEngine.getGraphicsSettingsManager().getAcceptedCategory().getName()
							.equalsIgnoreCase(catName)) {

						for (XMLTag subsetting : subtype.getChildren("setting")) {

							if (subsetting.hasAttribute("name")) {

								String name = subsetting.getAttribute("name");

								if (Keys.GRAPHICS_ANTIALIASING.getKey().equalsIgnoreCase(name)) {

									try {

										LutzEngine.getGraphicsSettingsManager().changeSetting(
												Keys.GRAPHICS_ANTIALIASING,
												Boolean.parseBoolean(subsetting.getValue().trim()));

									} catch (Exception e) {

										LutzEngine.getEngineLogger().warn("Unable to load setting value for '"
												+ Keys.GRAPHICS_ANTIALIASING.getKey() + "'!");
									}

								} else if (Keys.GRAPHICS_MAXIMUM_FPS.getKey().equalsIgnoreCase(name)) {

									try {

										LutzEngine.getGraphicsSettingsManager().changeSetting(Keys.GRAPHICS_MAXIMUM_FPS,
												Integer.parseInt(subsetting.getValue().trim()));

									} catch (Exception e) {

										LutzEngine.getEngineLogger().warn("Unable to load setting value for '"
												+ Keys.GRAPHICS_MAXIMUM_FPS.getKey() + "'!");
									}

								} else if (Keys.GRAPHICS_SCREENMODE.getKey().equalsIgnoreCase(name)) {

									try {

										LutzEngine.getGraphicsSettingsManager().changeSetting(Keys.GRAPHICS_SCREENMODE,
												Integer.parseInt(subsetting.getValue().trim()));

									} catch (Exception e) {

										LutzEngine.getEngineLogger().warn("Unable to load setting value for '"
												+ Keys.GRAPHICS_SCREENMODE.getKey() + "'!");
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static void saveSettings() throws IOException {

		new File("settings").mkdirs();

		File settings = new File("settings/settings.xml");

		XMLTag root = new XMLTag("settings");

		XMLTag graphics = new XMLTag(LutzEngine.getGraphicsSettingsManager().getAcceptedCategory().getName());

		// Graphics settings

		XMLTag aa = new XMLTag("setting");
		aa.addAttribute("name", Keys.GRAPHICS_ANTIALIASING.getKey());
		aa.setValue(LutzEngine.getGraphicsSettingsManager().retrieveSetting(Keys.GRAPHICS_ANTIALIASING).toString());

		XMLTag mfps = new XMLTag("setting");
		mfps.addAttribute("name", Keys.GRAPHICS_MAXIMUM_FPS.getKey());
		mfps.setValue(LutzEngine.getGraphicsSettingsManager().retrieveSetting(Keys.GRAPHICS_MAXIMUM_FPS).toString());

		XMLTag scrnmode = new XMLTag("setting");
		scrnmode.addAttribute("name", Keys.GRAPHICS_SCREENMODE.getKey());
		scrnmode.setValue(LutzEngine.getGraphicsSettingsManager().retrieveSetting(Keys.GRAPHICS_SCREENMODE).toString());

		graphics.addChild(aa);
		graphics.addChild(mfps);
		graphics.addChild(scrnmode);

		root.addChild(graphics);

		// Write settings to XML file

		XMLTree tree = new XMLTree(root);

		new DefaultXMLWriter().write(tree, settings);
	}
}
