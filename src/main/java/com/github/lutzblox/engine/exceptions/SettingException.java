package com.github.lutzblox.engine.exceptions;

import com.github.lutzblox.engine.settings.SettingsManager.Category;
import com.github.lutzblox.engine.settings.SettingsManager.Key;

public class SettingException extends RuntimeException {

	private static final long serialVersionUID = -7117644773174042381L;

	public SettingException(String message){
		
		super(message);
	}
	
	public static SettingException unacceptedKey(Key key, Category category){
		
		return new SettingException("The key '"+key.getKey()+"' is categorized as "+key.getCategory().getName().toUpperCase()+", but the SettingsManager trying to accept it only accepts those categorized as "+category.getName().toUpperCase()+"!");
	}
}
