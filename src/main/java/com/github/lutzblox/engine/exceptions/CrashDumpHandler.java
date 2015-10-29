package com.github.lutzblox.engine.exceptions;

import java.io.File;
import java.io.PrintStream;
import java.util.Calendar;

import com.github.lutzblox.engine.LutzEngine;
import com.github.lutzblox.engine.logging.channels.ListChannel;
import com.github.lutzblox.engine.plugins.Plugin;

public class CrashDumpHandler {

	public static void dump(Throwable t, ListChannel dumpCh) {

		try {

			File crashDumpDir = new File("crash-dumps");
			crashDumpDir.mkdirs();

			File crashDump = new File("crash-dumps/dump-" + getDumpTimestampFormatted() + ".txt");
			crashDump.createNewFile();

			PrintStream ps = new PrintStream(crashDump);

			ps.println("###########################");
			ps.println("##  GENERAL INFORMATION  ##");
			ps.println("###########################");
			ps.println();

			ps.println("Error Type: " + (t != null ? t.getClass().getSimpleName() : "-"));
			ps.println();

			ps.println("###########################");
			ps.println("##  RUNTIME INFORMATION  ##");
			ps.println("###########################");
			ps.println();

			ps.println("Time Running: " + LutzEngine.getRunTimeMillis() + " milliseconds");
			ps.println();

			ps.println("#########################");
			ps.println("##  INSTALLED PLUGINS  ##");
			ps.println("#########################");
			ps.println();

			Plugin[] plugins = LutzEngine.getPlugins();

			if (plugins.length > 0) {

				for (Plugin p : plugins) {

					ps.println(p.getName() + " (ID: " + p.getId() + ")");
				}

			} else {

				ps.println("No plugins installed");
			}

			ps.println();

			if (t != null) {

				ps.println("##############################");
				ps.println("##  FULL ERROR STACK TRACE  ##");
				ps.println("##############################");
				ps.println();

				t.printStackTrace(ps);

				ps.println();

			} else {

				ps.println("#########################");
				ps.println("##  NO ERROR DETECTED  ##");
				ps.println("#########################");
				ps.println();
			}

			ps.println("##########################");
			ps.println("##  ENGINE INFORMATION  ##");
			ps.println("##########################");
			ps.println();

			ps.println("LutzEngine Version: " + System.getProperty("lutzengine.version"));
			ps.println("LutzEngine Build: " + System.getProperty("lutzengine.build"));
			ps.println();

			ps.println("#######################");
			ps.println("##  FULL ENGINE LOG  ##");
			ps.println("#######################");
			ps.println();

			dumpCh.setPrintStream(ps);

			dumpCh.printList();

			ps.println();
			ps.println("##########################");
			ps.println("##  SYSTEM INFORMATION  ##");
			ps.println("##########################");
			ps.println();

			ps.println("Operating System: " + System.getProperty("os.name"));
			ps.println("OS Version: " + System.getProperty("os.version"));
			ps.println("OS Architecture: " + System.getProperty("os.arch"));
			ps.println("Java Version: " + System.getProperty("java.version"));
			ps.println("Java Vendor: " + System.getProperty("java.vendor"));

			dumpCh.close();

		} catch (Exception e) {

			System.err.println("An error occurred while creating the crash dump file!");

			e.printStackTrace();
		}
	}

	private static String getDumpTimestampFormatted() {

		Calendar c = Calendar.getInstance();

		String str = (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR) + "_"
				+ c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND);

		return str;
	}
}
