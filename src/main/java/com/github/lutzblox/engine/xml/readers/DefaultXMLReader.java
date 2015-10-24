package com.github.lutzblox.engine.xml.readers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.lutzblox.engine.xml.trees.PITag;
import com.github.lutzblox.engine.xml.trees.XMLTag;
import com.github.lutzblox.engine.xml.trees.XMLTree;


public class DefaultXMLReader extends XMLReader {
	
	public DefaultXMLReader() {
		
		super();
	}
	
	public DefaultXMLReader(ReaderParameters params) {
		
		super(params);
	}
	
	@Override
	public XMLTree read(Document d) {
		
		Element eRoot = d.getDocumentElement();
		
		XMLTag root = new XMLTag(eRoot.getNodeName());
		
		parseElementAttributes(eRoot, root);
		
		parseChildren(eRoot, root);
		
		return new XMLTree(root);
	}
	
	private void parseElementAttributes(Element e, XMLTag tag) {
		
		NamedNodeMap attrs = e.getAttributes();
		
		for (int i = 0; i < attrs.getLength(); i++) {
			
			Node item = attrs.item(i);
			
			if (item.getNodeType() == Node.ATTRIBUTE_NODE) {
				
				tag.addAttribute(item.getNodeName(), item.getNodeValue());
			}
		}
	}
	
	private void parseChildren(Element e, XMLTag tag) {
		
		NodeList children = e.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node nChild = children.item(i);
			
			if (nChild.getNodeType() == Node.ELEMENT_NODE) {
				
				Element eChild = (Element) nChild;
				
				XMLTag child = new XMLTag(eChild.getNodeName());
				
				child.setParent(tag);
				
				parseElementAttributes(eChild, child);
				
				parseChildren(eChild, child);
				
				tag.addChild(child);
				
			} else if (nChild.getNodeType() == Node.TEXT_NODE) {
				
				String value = nChild.getTextContent();
				
				if (value != null && !value.trim().contentEquals("")) {
					
					tag.setValue(value);
				}
				
			}else if(nChild.getNodeType()==Node.PROCESSING_INSTRUCTION_NODE){
				
				PITag child = new PITag(nChild.getNodeName());
				
				child.setValue(nChild.getNodeValue());
				
				child.setParent(tag);
				
				tag.addChild(child);
			}
		}
	}
}
