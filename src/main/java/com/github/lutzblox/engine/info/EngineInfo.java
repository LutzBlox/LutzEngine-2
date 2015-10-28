package com.github.lutzblox.engine.info;

import java.io.File;
import java.io.FileNotFoundException;

import com.github.lutzblox.engine.LutzEngine;

public class EngineInfo {

	public static void loadInfoIntoSystem() {

		File info = new File("engine/engine.info");

		try {

			InfoFileLoader.load(info);

		} catch (FileNotFoundException e) {

			LutzEngine.getEngineLogger()
					.warn("No engine.info file found!  Errors may occur as a result of missing information!");
		}
	}
}
