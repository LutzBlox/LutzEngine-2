package com.github.lutzblox.engine.plugins;

public abstract class Plugin {
	
	private PluginManager manager;
	private String name, id;
	
	public Plugin(PluginManager manager, String name, String id){
		
		this.manager = manager;
		this.name = name;
		this.id = id;
	}
	
	public PluginManager getPluginManager(){
		
		return manager;
	}
	
	public String getName(){
		
		return name;
	}
	
	public String getId(){
		
		return id;
	}

	public abstract void load();
	
	public abstract void unload();
}
