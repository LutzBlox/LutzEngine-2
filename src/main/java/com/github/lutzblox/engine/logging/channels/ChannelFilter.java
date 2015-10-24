package com.github.lutzblox.engine.logging.channels;

import com.github.lutzblox.engine.logging.Logger.Level;
import com.github.lutzblox.engine.logging.Logger.Timestamp;
import com.github.lutzblox.engine.logging.Logger.Verbosity;

public interface ChannelFilter {

	public boolean filter(String toLog, Level level, Verbosity verbosity, Timestamp timestamp);
}
