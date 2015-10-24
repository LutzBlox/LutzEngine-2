package com.github.lutzblox.engine.debugging;

public class DebugConfig {

	private static boolean enabled = false;
	
	public static void enable(boolean enabled){
		
		DebugConfig.enabled = enabled;
	}
	
	public static boolean isEnabled(){
		
		return enabled;
	}
}
