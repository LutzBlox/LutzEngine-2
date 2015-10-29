package com.github.lutzblox.engine.plugins;

import com.github.lutzblox.engine.logging.Logger;
import com.github.lutzblox.engine.logging.LoggerFactory;

public class PluginManager {

	private String id;
	private Logger logger;

	public PluginManager(String id) {

		this.id = id;
		logger = LoggerFactory.getLogger(id);
	}

	public String getPluginId() {

		return id;
	}

	public Logger getPluginLogger() {

		return logger;
	}
}
