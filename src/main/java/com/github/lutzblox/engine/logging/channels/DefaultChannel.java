package com.github.lutzblox.engine.logging.channels;

import com.github.lutzblox.engine.logging.LogChannel;
import com.github.lutzblox.engine.logging.Logger.Verbosity;

public class DefaultChannel extends LogChannel {

	public DefaultChannel(String id) {
		
		super(System.out, id);
	}

	public DefaultChannel(String id, Verbosity verbosity){
		
		super(System.out, id, verbosity);
	}
}
