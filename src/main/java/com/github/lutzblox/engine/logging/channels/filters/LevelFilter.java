package com.github.lutzblox.engine.logging.channels.filters;

import com.github.lutzblox.engine.logging.Logger.Level;
import com.github.lutzblox.engine.logging.Logger.Timestamp;
import com.github.lutzblox.engine.logging.Logger.Verbosity;
import com.github.lutzblox.engine.logging.channels.ChannelFilter;

public class LevelFilter implements ChannelFilter {

	private Level[] levels;
	
	public LevelFilter(Level... levels){
		
		this.levels = levels;
	}
	
	@Override
	public boolean filter(String toLog, Level level, Verbosity verbosity,
			Timestamp timestamp) {

		for(Level l : levels){
			
			if(l == level){
				
				return true;
			}
		}
		
		return false;
	}
}
