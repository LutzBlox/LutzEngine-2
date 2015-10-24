package com.github.lutzblox.engine.logging.channels.filters;

import com.github.lutzblox.engine.logging.Logger.Level;

public class IssueFilter extends LevelFilter {

	public IssueFilter() {

		super(Level.WARN, Level.FATAL);
	}
}
