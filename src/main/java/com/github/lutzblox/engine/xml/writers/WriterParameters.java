package com.github.lutzblox.engine.xml.writers;

public class WriterParameters {

	private boolean formatOutput = true;
	private String encoding = "UTF-8", version = "1.0";
	
	public void setFormatOutput(boolean format){
		
		this.formatOutput = format;
	}
	
	public boolean getFormatOutput(){
		
		return formatOutput;
	}
	
	public void setEncoding(String encoding){
		
		this.encoding = encoding;
	}
	
	public String getEncoding(){
		
		return encoding;
	}
	
	public void setXMLVersion(String version){
		
		this.version = version;
	}
	
	public String getXMLVersion(){
		
		return version;
	}
}
