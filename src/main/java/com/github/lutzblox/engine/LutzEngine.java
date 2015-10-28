package com.github.lutzblox.engine;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import com.github.lutzblox.engine.debugging.DebugConfig;
import com.github.lutzblox.engine.exceptions.CrashDumpHandler;
import com.github.lutzblox.engine.info.EngineInfo;
import com.github.lutzblox.engine.logging.LogChannel;
import com.github.lutzblox.engine.logging.Logger;
import com.github.lutzblox.engine.logging.Logger.Level;
import com.github.lutzblox.engine.logging.LoggerFactory;
import com.github.lutzblox.engine.logging.LoggingUtils;
import com.github.lutzblox.engine.logging.channels.DefaultChannel;
import com.github.lutzblox.engine.logging.channels.FileChannel;
import com.github.lutzblox.engine.logging.channels.FileListChannel;
import com.github.lutzblox.engine.logging.channels.ListChannel;
import com.github.lutzblox.engine.logging.channels.filters.DebugFilter;
import com.github.lutzblox.engine.logging.channels.filters.IssueFilter;
import com.github.lutzblox.engine.logging.channels.filters.LevelFilter;
import com.github.lutzblox.engine.screen.Resolution;
import com.github.lutzblox.engine.screen.Screen;
import com.github.lutzblox.engine.settings.SettingsFileHandler;
import com.github.lutzblox.engine.settings.SettingsManager;
import com.github.lutzblox.engine.settings.SettingsManager.Keys;

public class LutzEngine {

	private static String gameName = "";

	private static SettingsManager graphicsSettings;

	private static LogChannel logCh, stdCh;
	private static FileListChannel errorCh, debugCh;
	private static ListChannel dumpCh;

	private static Logger engineLogger;

	private static boolean running = false, crashed = false;

	private static Thread main;

	private static Throwable crashError = null;
	
	private static long startTime = 0, runTime = 0;

	public static void setGameName(String name) {

		gameName = name;

		Screen.refreshTitle();
	}

	public static String getGameName() {

		return gameName;
	}

	public static void startEngine() {

		performSetup();

		running = true;
	}

	public static void performSetup() {
		
		startTime = System.currentTimeMillis();

		new File("logs").mkdirs();

		stdCh = new DefaultChannel("std_out");

		LoggingUtils.moveOldLogs();

		try {

			logCh = new FileChannel(LoggingUtils.getLogFile(), "engine-log");
			logCh.addFilter(new LevelFilter(Level.INFO, Level.WARN, Level.FATAL));

		} catch (Exception e) {

			System.err.println("Logger unavailable!");

			e.printStackTrace();
		}

		errorCh = new FileListChannel(LoggingUtils.getErrorFile(), "engine-errors");
		errorCh.addFilter(new IssueFilter());

		debugCh = new FileListChannel(LoggingUtils.getDebugFile(), "debug-log");
		debugCh.addFilter(new DebugFilter());

		dumpCh = new ListChannel(null, "dump-log");

		engineLogger = LoggerFactory.getLogger("engine");
		
		getEngineLogger().info("Loading info...");
		
		EngineInfo.loadInfoIntoSystem();

		getEngineLogger().info("Performing OS-specific setup...");

		Screen.performOSSetup();

		if (DebugConfig.isEnabled() && DebugConfig.getPrintResolutions()) {

			Resolution[] fullscr = Resolution.getSupportedResolutions();

			String fullscrStr = "";

			for (int i = 0; i < fullscr.length; i++) {

				if (i > 0) {

					fullscrStr += ", ";
				}

				Resolution r = fullscr[i];

				fullscrStr += r.getWidth() + "x" + r.getHeight();
			}

			String windowedStr = (int) Screen.getMaximumWindowSize().getWidth()+"x"+(int) Screen.getMaximumWindowSize().getHeight();
			
			getEngineLogger().debug("Resolutions available for fullscreen modes: "+fullscrStr);
			getEngineLogger().debug("Maximum resolution allowed for windowed mode: "+windowedStr);
		}

		getEngineLogger().info("Loading settings...");

		graphicsSettings = new SettingsManager(SettingsManager.Categories.GRAPHICS);
		graphicsSettings.addSetting(SettingsManager.Keys.GRAPHICS_ANTIALIASING, true);
		graphicsSettings.addSetting(SettingsManager.Keys.GRAPHICS_MAXIMUM_FPS, 60);
		graphicsSettings.addSetting(SettingsManager.Keys.GRAPHICS_SCREENMODE, 0);

		try {

			SettingsFileHandler.loadSettings();

		} catch (Exception e) {

			getEngineLogger().warn("Settings could not be loaded!  Defaulting all settings...");

			getEngineLogger().logException(e);
		}

		main = new Thread() {

			@Override
			public void run() {

				// Setup screen

				try {

					getEngineLogger().info("Setting up screen...");

					Screen.setup(Integer.parseInt(
							getGraphicsSettingsManager().retrieveSetting(Keys.GRAPHICS_SCREENMODE).toString()));
					
					int i = 1/0;

				} catch (Exception e) {

					getEngineLogger().fatal("Unable to setup screen!");
					getEngineLogger().logCrash(e);

					crashEngine(e);
				}

				// Wait for execution to end

				while (running)
					;
			}
		};
		main.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {

				getEngineLogger().fatal("An error occurred in thread '" + arg0.getName() + "'!");
				getEngineLogger().logCrash(arg1);

				crashEngine(arg1);
			}
		});
		main.setName("engine-main");
		main.start();
	}

	public static void crashEngine(Throwable t) {

		crashed = true;
		crashError = t;

		getEngineLogger().fatal("A fatal error has occurred in the engine!");

		shutdownEngine();
	}

	public static void shutdownEngine() {
		
		runTime = System.currentTimeMillis()-startTime;

		running = false;

		getEngineLogger().info("Shutting down engine...");

		getEngineLogger().info("Saving settings...");

		try {

			SettingsFileHandler.saveSettings();

		} catch (Exception e) {

			LutzEngine.getEngineLogger().warn("Settings were not able to be saved!");

			LutzEngine.getEngineLogger().logException(e);
		}

		getEngineLogger().info("Finalizing shutdown...");

		if (errorCh.getList().length != 0) {

			try {

				if (!LoggingUtils.getErrorFile().exists()) {

					LoggingUtils.getErrorFile().createNewFile();
				}

				errorCh.printList();

			} catch (Exception e) {

				System.err.println("Unable to write error log!");

				e.printStackTrace();
			}
		}

		errorCh.close();

		if (DebugConfig.isEnabled() && debugCh.getList().length > 0) {

			try {

				if (!LoggingUtils.getDebugFile().exists()) {

					LoggingUtils.getDebugFile().createNewFile();
				}

				debugCh.printList();

			} catch (Exception e) {

				System.err.println("Unable to write debug log!");

				e.printStackTrace();
			}
		}

		debugCh.close();

		logCh.close();

		if (crashed) {

			CrashDumpHandler.dump(crashError, dumpCh);
		}

		System.exit(crashed ? 1 : 0);
	}

	public static Logger addChannels(Logger logger) {

		logger.addChannel(logCh);
		logger.addChannel(errorCh);
		logger.addChannel(stdCh);
		logger.addChannel(debugCh);
		logger.addChannel(dumpCh);

		return logger;
	}

	public static LogChannel[] getChannels() {

		return new LogChannel[] { logCh, errorCh, stdCh, debugCh, dumpCh };
	}

	public static Logger getEngineLogger() {

		return engineLogger;
	}

	public static SettingsManager getGraphicsSettingsManager() {

		return graphicsSettings;
	}

	public static boolean isRunning() {

		return running;
	}
	
	public static long getRunTimeMillis(){
		
		return (running ? System.currentTimeMillis()-startTime : runTime);
	}
}
