package com.github.lutzblox.engine.info;

import java.io.File;
import java.io.IOException;

import com.github.lutzblox.engine.LutzEngine;

public class EngineInfo {

	public static void loadInfoIntoSystem() {

		File info = new File("engine/engine.info");

		try {

			InfoFileLoader.load(info);

		} catch (IOException e) {

			LutzEngine.getEngineLogger()
					.warn("Could not load engine.info file!  Errors may occur as a result of missing information!");
		}
	}
}
