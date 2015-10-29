package com.github.lutzblox.engine.plugins.loading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.github.lutzblox.engine.LutzEngine;
import com.github.lutzblox.engine.info.InfoFileLoader;
import com.github.lutzblox.engine.plugins.Plugin;
import com.github.lutzblox.engine.plugins.PluginManager;

public class PluginLoader {

	public static Plugin[] loadPlugins() {

		File pluginDir = new File("plugins/");

		if (pluginDir.exists()) {

			List<Plugin> plugins = new ArrayList<Plugin>();

			for (String jarName : pluginDir.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {

					return name.endsWith(".jar");
				}
			})) {

				try {

					File fullJar = new File(pluginDir, jarName);

					List<String> classes = new ArrayList<String>();

					ZipInputStream jar = new ZipInputStream(new FileInputStream(fullJar));

					for (ZipEntry entry = jar.getNextEntry(); entry != null; entry = jar.getNextEntry()) {

						if (!entry.isDirectory() && entry.getName().endsWith(".class")) {

							classes.add(entry.getName().replace("\\", "/").replace("/", ".").substring(0,
									entry.getName().length() - ".class".length()));
						}
					}

					jar.close();

					ClassLoader loader = URLClassLoader.newInstance(new URL[] { fullJar.toURI().toURL() },
							ClassLoader.getSystemClassLoader());

					for (String className : classes) {

						loader.loadClass(className);
					}

					InputStream info = loader.getResourceAsStream("plugin.info");

					if (info != null) {

						InfoFileLoader.load(info);

						info.close();

						String name = System.getProperty("name");
						String id = System.getProperty("id");
						String mainClass = System.getProperty("mainclass");

						if (name != null && id != null && mainClass != null) {

							System.clearProperty("name");
							System.clearProperty("id");
							System.clearProperty("mainclass");

							try {

								Class<?> main = Class.forName(mainClass, true, loader);
								Class<? extends Plugin> mainPlugin = main.asSubclass(Plugin.class);
								Constructor<? extends Plugin> ctorPlugin = mainPlugin
										.getConstructor(PluginManager.class, String.class, String.class);

								PluginManager manager = new PluginManager(id);

								Plugin plugin = ctorPlugin.newInstance(manager, name, id);

								plugins.add(plugin);

							} catch (ClassCastException e) {

								LutzEngine.getEngineLogger()
										.warn("Main plugin class file '" + name + "' does not extend Plugin!");
								LutzEngine.getEngineLogger().logException(e);

							} catch (ClassNotFoundException e) {

								LutzEngine.getEngineLogger()
										.warn("Could not load main plugin class file '" + name + "'!");
								LutzEngine.getEngineLogger().logException(e);

							} catch (NoSuchMethodException e) {

								LutzEngine.getEngineLogger().warn("Main plugin file '" + name
										+ "' does not have a constructor that takes a PluginManager and two strings as parameters!");
								LutzEngine.getEngineLogger().logException(e);

							} catch (SecurityException e) {

								LutzEngine.getEngineLogger()
										.warn("The plugin loader encountered errors while obtaining the constructor for main plugin class '"
												+ name + "'!");
								LutzEngine.getEngineLogger().logException(e);

							} catch (InstantiationException e) {

								LutzEngine.getEngineLogger()
										.warn("An error occurred while calling the constructor in '" + name + "'!");
								LutzEngine.getEngineLogger().logException(e);

							} catch (IllegalAccessException e) {

								LutzEngine.getEngineLogger()
										.warn("An error occurred while calling the constructor in '" + name + "'!");
								LutzEngine.getEngineLogger().logException(e);

							} catch (IllegalArgumentException e) {

								LutzEngine.getEngineLogger()
										.warn("An error occurred while calling the constructor in '" + name + "'!");
								LutzEngine.getEngineLogger().logException(e);

							} catch (InvocationTargetException e) {

								LutzEngine.getEngineLogger()
										.warn("An error occurred while calling the constructor in '" + name + "'!");
								LutzEngine.getEngineLogger().logException(e);
							}

						}
					}

				} catch (IOException e) {

					LutzEngine.getEngineLogger().warn("An error occurred while loading '/plugins/" + jarName + "'!");
					LutzEngine.getEngineLogger().logException(e);

				} catch (ClassNotFoundException e) {

					LutzEngine.getEngineLogger()
							.warn("An error occurred while loading classes within '/plugins/" + jarName + "'!");
					LutzEngine.getEngineLogger().logException(e);
				}
			}

			return plugins.toArray(new Plugin[plugins.size()]);
		}

		return new Plugin[0];
	}
}
