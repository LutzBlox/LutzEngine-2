package com.github.lutzblox.engine.xml.readers;

public class ReaderParameters {

	private boolean ignoreComments = false, ignoreWhitespace = false, validate = false, namespaceAware = false;
	
	public void setIgnoreComments(boolean ignore){
		
		this.ignoreComments = ignore;
	}
	
	public boolean getIgnoreComments(){
		
		return ignoreComments;
	}
	
	public void setIgnoreWhitespace(boolean ignore){
		
		this.ignoreWhitespace = ignore;
	}
	
	public boolean getIgnoreWhitespace(){
		
		return ignoreWhitespace;
	}
	
	public void setValidate(boolean validate){
		
		this.validate = validate;
	}
	
	public boolean getValidate(){
		
		return validate;
	}
	
	public void setNamespaceAware(boolean namespaceAware){
		
		this.namespaceAware = namespaceAware;
	}
	
	public boolean getNamespaceAware(){
		
		return namespaceAware;
	}
}
