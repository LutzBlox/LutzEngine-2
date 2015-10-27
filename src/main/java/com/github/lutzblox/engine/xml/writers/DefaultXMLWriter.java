package com.github.lutzblox.engine.xml.writers;

import com.github.lutzblox.engine.xml.trees.PITag;
import com.github.lutzblox.engine.xml.trees.XMLTag;
import com.github.lutzblox.engine.xml.trees.XMLTree;

public class DefaultXMLWriter extends XMLWriter {

	public DefaultXMLWriter() {

		super();
	}

	public DefaultXMLWriter(WriterParameters params) {

		super(params);
	}

	@Override
	public String write(XMLTree tree) {

		String contents = "<?xml version=\"" + getWriterParameters().getXMLVersion() + "\" encoding=\""
				+ getWriterParameters().getEncoding() + "\"?>" + (getWriterParameters().getFormatOutput() ? "\n" : "");

		contents += writeTag(tree.getRoot(), "");

		return contents;
	}

	private String writeTag(XMLTag tag, String indent) {

		if (tag instanceof PITag) {

			String contents = "";

			if (getWriterParameters().getFormatOutput()) {

				contents = (indent + "<?" + tag.getName() + "\n" + (tag.getValue() == null ? "" : tag.getValue())
						+ "\n?>").replace("\n", "\n" + indent);

			} else {

				contents = "<?" + tag.getName() + "\n" + (tag.getValue() == null ? "" : tag.getValue()) + "\n?>";
			}

			return contents;

		} else {

			String contents = (getWriterParameters().getFormatOutput() ? indent : "") + "<" + tag.getName();

			for (String attrName : tag.getAttributes().keySet()) {

				contents += " " + attrName + "=\"" + tag.getAttribute(attrName) + "\"";
			}

			if (tag.getValue() != null) {
				
				contents += ">";

				String value = tag.getValue();

				if (getWriterParameters().getFormatOutput()) {

					value.replace("\n", "\n" + indent);
				}

				contents += value;

				contents += "</" + tag.getName() + ">";

			} else if (tag.hasChildren()) {
				
				contents += ">";

				for (XMLTag child : tag.getChildren()) {

					if (getWriterParameters().getFormatOutput()) {

						contents += "\n";
					}

					contents += writeTag(child, indent + "\t");
				}

				contents += "\n" + (getWriterParameters().getFormatOutput() ? indent : "") + "</" + tag.getName() + ">";

			} else {

				contents += " />";
			}

			return contents;
		}
	}
}
