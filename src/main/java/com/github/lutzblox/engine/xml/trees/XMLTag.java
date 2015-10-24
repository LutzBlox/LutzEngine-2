package com.github.lutzblox.engine.xml.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XMLTag {
	
	private Map<String, String> attributes = new HashMap<String, String>();
	private String name;
	private String value = null;
	
	private List<String> childrenNames = new ArrayList<String>();
	private List<XMLTag> children = new ArrayList<XMLTag>();
	private XMLTag parent = null;
	
	public XMLTag(String name) {
		
		this.name = name;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void addChild(XMLTag child) {
		
		children.add(child);
		childrenNames.add(child.getName());
	}
	
	public XMLTag[] getChildren() {
		
		return children.toArray(new XMLTag[children.size()]);
	}
	
	public boolean hasChild(String name) {
		
		return childrenNames.contains(name);
	}
	
	public boolean hasChildren(){
		
		return children.size()>0;
	}
	
	public XMLTag[] getChildren(String name) {
		
		List<XMLTag> tags = new ArrayList<XMLTag>();
		
		for (XMLTag tag : children) {
			
			if (tag.getName().equalsIgnoreCase(name)) {
				
				tags.add(tag);
			}
		}
		
		return tags.toArray(new XMLTag[tags.size()]);
	}
	
	public void setParent(XMLTag parent) {
		
		this.parent = parent;
	}
	
	public XMLTag getParent() {
		
		return parent;
	}
	
	public void setValue(String value) {
		
		this.value = value;
	}
	
	public String getValue() {
		
		return value;
	}
	
	public boolean hasValue(){
		
		return value != null;
	}
	
	public void addAttribute(String attrName, String attrValue) {
		
		attributes.put(attrName, attrValue);
	}
	
	public Map<String, String> getAttributes() {
		
		return attributes;
	}
	
	public String getAttribute(String attrName) {
		
		return attributes.get(attrName);
	}
	
	public boolean hasAttribute(String attrName){
		
		return attributes.get(attrName) != null;
	}
	
	@Override
	public String toString() {
		
		String str = getName() + " (";
		
		for (int i = 0; i < getAttributes().keySet().size(); i++) {
			
			String attrName = getAttributes().keySet().toArray(new String[] {})[i];
			String attrValue = getAttribute(attrName);
			
			str += attrName + "=" + attrValue;
			
			if (i < getAttributes().keySet().size() - 1) {
				
				str += ", ";
			}
		}
		
		str += ")";
		
		if (getValue() != null) {
			
			str += ": " + getValue();
		}
		
		return str;
	}
}
