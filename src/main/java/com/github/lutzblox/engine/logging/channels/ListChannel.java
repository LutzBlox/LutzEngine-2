package com.github.lutzblox.engine.logging.channels;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.github.lutzblox.engine.logging.LogChannel;
import com.github.lutzblox.engine.logging.Logger.Verbosity;

public class ListChannel extends LogChannel {

	private List<String> logList = new ArrayList<String>();

	public ListChannel(PrintStream stream, String id) {

		super(stream, id);
	}

	public ListChannel(PrintStream stream, String id, Verbosity verbosity) {

		super(stream, id, verbosity);
	}

	public String[] getList() {

		return logList.toArray(new String[] {});
	}

	@Override
	public void printToStream(String toLog) {

		logList.add(toLog);
	}

	public void printList() throws IOException {

		for (String toLog : logList) {

			this.getPrintStream().println(toLog);
		}
	}

	public void printList(ChannelFinalizer finalizer) throws IOException {

		for(String str : finalizer.getFinalStrings()){
			
			logList.add(str);
		}
		
		this.printList();
	}
}
