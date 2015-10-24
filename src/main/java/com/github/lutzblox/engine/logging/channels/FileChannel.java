package com.github.lutzblox.engine.logging.channels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import com.github.lutzblox.engine.logging.LogChannel;
import com.github.lutzblox.engine.logging.Logger.Verbosity;

public class FileChannel extends LogChannel {

	public FileChannel(File file, String id) throws FileNotFoundException {

		super(new PrintStream(file), id);
	}

	public FileChannel(File file, String id, Verbosity verbosity)
			throws FileNotFoundException {

		super(new PrintStream(file), id, verbosity);
	}
}
