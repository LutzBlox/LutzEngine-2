package com.github.lutzblox.engine.xml.trees;

public class XMLTree {

	private XMLTag root;
	
	public XMLTree(){
		
		this(null);
	}
	
	public XMLTree(XMLTag root){
		
		this.root = root;
	}
	
	public XMLTag getRoot(){
		
		return root;
	}
}
