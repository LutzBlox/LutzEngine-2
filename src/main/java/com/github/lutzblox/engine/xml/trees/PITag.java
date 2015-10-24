package com.github.lutzblox.engine.xml.trees;

public class PITag extends XMLTag {
	
	public PITag(String name) {
		
		super(name);
	}
	
	@Override
	public void addChild(XMLTag child) {
		
		throw new UnsupportedOperationException("Cannot add children to processing instructions!");
	}
	
	@Override
	public void addAttribute(String attrName, String attrValue) {
		
		throw new UnsupportedOperationException("Cannot add attributes to processing instructions!");
	}
	
	@Override
	public String toString() {
		
		return "Processing Instruction (" + getName() + "): \"" + (getValue() != null ? getValue().trim() + "\"" : "\"");
	}
}
