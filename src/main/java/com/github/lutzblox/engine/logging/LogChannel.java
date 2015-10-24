package com.github.lutzblox.engine.logging;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.github.lutzblox.engine.logging.Logger.Level;
import com.github.lutzblox.engine.logging.Logger.Timestamp;
import com.github.lutzblox.engine.logging.Logger.Verbosity;
import com.github.lutzblox.engine.logging.channels.ChannelFilter;
import com.github.lutzblox.engine.logging.channels.ChannelFinalizer;
import com.github.lutzblox.engine.logging.channels.DefaultChannel;

public class LogChannel {

	private PrintStream stream;

	private Verbosity channelVerbosity;

	private LogFormatter formatter = LogFormatter.Defaults.TIMESTAMP_FORMAT_DEFAULT;

	private boolean valid = true, closed = false;

	private String id;

	private List<ChannelFilter> filters = new ArrayList<ChannelFilter>();

	public static final LogChannel DEFAULT_CHANNEL = new DefaultChannel(
			"default");

	public LogChannel(LogChannel channel) {

		this(channel.getPrintStream(), channel.getId(), channel.getVerbosity());
		this.setLogFormatter(channel.getLogFormatter());

		for (ChannelFilter filter : channel.getFilters()) {

			this.addFilter(filter);
		}
	}

	public LogChannel(PrintStream stream, String id) {

		this(stream, id, Verbosity.VERBOSE);
	}

	public LogChannel(PrintStream stream, String id, Verbosity verbosity) {

		this.stream = stream;
		this.id = id;
		this.channelVerbosity = verbosity;
	}

	public String getId() {

		return id;
	}

	public void setLogFormatter(LogFormatter formatter) {

		this.formatter = formatter;
	}

	public LogFormatter getLogFormatter() {

		return formatter;
	}

	public void setPrintStream(PrintStream stream) {

		this.stream = stream;
	}

	public PrintStream getPrintStream() {

		return stream;
	}

	public void setVerbosity(Verbosity verbosity) {

		this.channelVerbosity = verbosity;
	}

	public Verbosity getVerbosity() {

		return channelVerbosity;
	}

	public void addFilter(ChannelFilter filter) {

		filters.add(filter);
	}

	public ChannelFilter[] getFilters() {

		return filters.toArray(new ChannelFilter[] {});
	}

	private boolean filter(String toLog, Level level, Verbosity verbosity,
			Timestamp timestamp) {

		for (ChannelFilter filter : filters) {

			if (!filter.filter(toLog, level, verbosity, timestamp)) {

				return false;
			}
		}

		return true;
	}

	public void log(String toLog, Level level, Verbosity verbosity,
			Timestamp timestamp, Logger logger) {

		if (isValid()) {

			if (filter(toLog, level, verbosity, timestamp)) {

				if (channelVerbosity.validateVerbosity(verbosity)) {

					if (logger.isNamed()) {

						printToStream("["
								+ formatter.formatTimestamp(timestamp) + "]["
								+ level.getName().toUpperCase() + "]["
								+ logger.getName().toUpperCase() + "]: "
								+ toLog);

					} else {

						printToStream("["
								+ formatter.formatTimestamp(timestamp) + "]["
								+ level.getName().toUpperCase() + "]: " + toLog);
					}
				}
			}

		} else {

			failChannel(timestamp);
		}
	}

	public void printToStream(String toLog) {

		stream.println(toLog);
	}

	public void failChannel(Timestamp timestamp) {

		valid = false;

		System.err
				.println("["
						+ formatter.formatTimestamp(timestamp)
						+ "]["
						+ Level.FATAL.getName().toUpperCase()
						+ "][CHANNEL '"
						+ id.toUpperCase()
						+ "']: This channel's print stream is no longer valid.  It will no longer produce any output.");

		try {

			close();

		} catch (Exception e) {
		}
	}

	public void finalizeChannel(ChannelFinalizer finalizer) {

		valid = false;

		for (String line : finalizer.getFinalStrings()) {

			printToStream(line);
		}

		close();
	}

	public boolean isValid() {

		return !closed && valid;
	}

	public void close() {

		closed = true;

		if (stream != null) {

			stream.close();
		}
	}
}
