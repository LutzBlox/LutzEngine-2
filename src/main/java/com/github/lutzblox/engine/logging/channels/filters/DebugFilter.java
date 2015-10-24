package com.github.lutzblox.engine.logging.channels.filters;

import com.github.lutzblox.engine.logging.Logger.Level;

public class DebugFilter extends LevelFilter {

	public DebugFilter() {

		super(Level.DEBUG);
	}
}
