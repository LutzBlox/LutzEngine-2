package com.github.lutzblox.engine.logging;

import java.util.HashMap;
import java.util.Map;

import com.github.lutzblox.engine.LutzEngine;

public class LoggerFactory {

	private static Map<String, Logger> loggers = new HashMap<String, Logger>();
	
	public static Logger getLogger(String name){
		
		return getLogger(name, LutzEngine.getChannels());
	}
	
	public static Logger getLogger(String name, LogChannel... channels){
		
		if(loggers.containsKey(name)){
			
			return loggers.get(name);
			
		}else{
			
			Logger l = new Logger(name, channels);
			
			loggers.put(name, l);
			
			return l;
		}
	}
}
