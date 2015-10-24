package com.github.lutzblox.engine.logging.channels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import com.github.lutzblox.engine.logging.Logger.Verbosity;

public class FileListChannel extends ListChannel {

	private File file;

	public FileListChannel(File file, String id) {

		super(null, id);

		this.file = file;
	}

	public FileListChannel(File file, String id, Verbosity verbosity) {

		super(null, id, verbosity);

		this.file = file;
	}

	public File getFile() {

		return file;
	}

	public void printList() throws FileNotFoundException {

		PrintStream ps = new PrintStream(file);

		for (String toLog : getList()) {

			ps.println(toLog);
		}

		ps.close();
	}
}
