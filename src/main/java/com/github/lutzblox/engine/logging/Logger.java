package com.github.lutzblox.engine.logging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Logger implements Cloneable {

	public static class Timestamp {

		public static class Month {

			public static final Month JANUARY = new Month(1, "January", "Jan");
			public static final Month FEBRUARY = new Month(2, "February", "Feb");
			public static final Month MARCH = new Month(3, "March", "Mar");
			public static final Month APRIL = new Month(4, "April", "Apr");
			public static final Month MAY = new Month(5, "May", "May");
			public static final Month JUNE = new Month(6, "June", "June");
			public static final Month JULY = new Month(7, "July", "July");
			public static final Month AUGUST = new Month(8, "August", "Aug");
			public static final Month SEPTEMBER = new Month(9, "September",
					"Sept");
			public static final Month OCTOBER = new Month(10, "October", "Oct");
			public static final Month NOVEMBER = new Month(11, "November",
					"Nov");
			public static final Month DECEMBER = new Month(12, "December",
					"Dec");
			public static final Month UNDECIMBER = new Month(13, "Undecimber",
					"Vnde");

			private int month;

			private String fullName, abbrevName;

			private Month(int month, String fullName, String abbrevName) {

				this.month = month;
				this.fullName = fullName;
				this.abbrevName = abbrevName;
			}

			public int getMonthNumber() {

				return month;
			}

			public String getFullName() {

				return fullName;
			}

			public String getAbbreviatedName() {

				return abbrevName;
			}

			private static Month[] monthArray = { JANUARY, FEBRUARY, MARCH,
					APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER,
					NOVEMBER, DECEMBER, UNDECIMBER };

			public static Month getMonth(int month) {

				for (Month m : monthArray) {

					if (m.getMonthNumber() == month) {

						return m;
					}
				}

				return null;
			}
		}

		private int date, month, year, second, minute, hour_twelve,
				hour_twentyfour;

		public Timestamp(int second, int minute, int hour_twelve,
				int hour_twentyfour, int date, int month, int year) {

			this.second = second;
			this.minute = minute;
			this.hour_twelve = fixHour(hour_twelve, 12);
			this.hour_twentyfour = hour_twentyfour;
			this.date = date;
			this.month = month + 1;
			this.year = year;
		}

		public int getSecond() {

			return second;
		}

		public int getMinute() {

			return minute;
		}

		public int get12Hour() {

			return hour_twelve;
		}

		public int get24Hour() {

			return hour_twentyfour;
		}

		public int getDate() {

			return date;
		}

		public int getMonth() {

			return month;
		}

		public int getYear() {

			return year;
		}

		private int fixHour(int hour, int maxHour) {

			if (hour == 0) {

				hour = maxHour;
			}

			return hour;
		}

		@Override
		public String toString() {

			return LogFormatter.Defaults.TIMESTAMP_FORMAT_DEFAULT
					.formatTimestamp(this);
		}

		public static Timestamp getCurrentTimestamp() {

			Calendar c = Calendar.getInstance();

			return new Timestamp(c.get(Calendar.SECOND),
					c.get(Calendar.MINUTE), c.get(Calendar.HOUR),
					c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.DATE),
					c.get(Calendar.MONTH), c.get(Calendar.YEAR));
		}
	}

	public static class Level {

		public static final Level DEBUG = new Level("debug");
		public static final Level INFO = new Level("info");
		public static final Level WARN = new Level("warn");
		public static final Level FATAL = new Level("fatal");

		private String name;

		public Level(String name) {

			this.name = name;
		}

		public String getName() {

			return name;
		}

		@Override
		public String toString() {

			return name;
		}
	}

	public static enum Verbosity {

		MINIMAL, NORMAL, VERBOSE;

		public boolean validateVerbosity(Verbosity verbosity) {

			switch (this) {

			case MINIMAL:

				if (verbosity.equals(MINIMAL)) {

					return true;
				}

				break;

			case NORMAL:

				if (!verbosity.equals(VERBOSE)) {

					return true;
				}

				break;

			case VERBOSE:

				return true;
			}

			return false;
		}
	}

	private List<LogChannel> channels = new ArrayList<LogChannel>();

	private boolean named = false;

	private String loggerName = "Unnamed";

	public Logger(LogChannel... channels) {

		for (LogChannel channel : channels) {

			this.channels.add(channel);
		}
	}

	public Logger(String loggerName) {

		this(loggerName, LogChannel.DEFAULT_CHANNEL);
	}

	public Logger(String loggerName, LogChannel... channels) {

		this.named = true;
		this.loggerName = loggerName;

		for (LogChannel channel : channels) {

			this.channels.add(channel);
		}
	}

	protected Logger(Logger logger) {

		for (LogChannel channel : logger.getChannels()) {

			channels.add(channel);
		}

		this.named = logger.isNamed();
		this.loggerName = logger.getName();
	}

	public void addChannel(LogChannel channel) {

		channels.add(channel);
	}

	public LogChannel[] getChannels() {

		return channels.toArray(new LogChannel[] {});
	}

	public boolean isNamed() {

		return named;
	}

	public String getName() {

		return loggerName;
	}

	public void log(String toLog) {

		log(toLog, Level.INFO);
	}

	public void log(String toLog, Object... substitutions) {

		log(toLog, Level.INFO, substitutions);
	}

	public void log(String toLog, Level level) {

		log(toLog, level, Verbosity.NORMAL);
	}

	public void log(String toLog, Level level, Object... substitutions) {

		log(toLog, level, Verbosity.NORMAL, substitutions);
	}

	public void log(String toLog, Verbosity verbosity) {

		log(toLog, Level.INFO, verbosity);
	}

	public void log(String toLog, Verbosity verbosity, Object... substitutions) {

		log(toLog, Level.INFO, verbosity, substitutions);
	}

	public void log(String toLog, Level level, Verbosity verbosity) {

		Timestamp t = Timestamp.getCurrentTimestamp();

		for (LogChannel channel : channels) {

			channel.log(toLog, level, verbosity, t, this);
		}
	}

	public void log(String toLog, Level level, Verbosity verbosity,
			Object... substitutions) {

		for (int i = 0; i < substitutions.length; i++) {

			toLog = toLog.replace("{" + i + "}", substitutions[i].toString());
		}

		log(toLog, level);
	}

	public void logCrash(Throwable exception) {

		logException(exception, Level.FATAL);
	}

	public void logException(Throwable exception) {

		logException(exception, Level.WARN);
	}

	public void logException(Throwable exception, Level level) {

		log(exception.getClass().getName() + ": " + exception.getMessage(),
				level, Verbosity.MINIMAL);

		for (StackTraceElement element : exception.getStackTrace()) {

			log(" at " + element, level, Verbosity.MINIMAL);
		}
	}

	public void debug(String toLog) {

		log(toLog, Level.DEBUG, Verbosity.VERBOSE);
	}

	public void debug(String toLog, Object... substitutions) {

		log(toLog, Level.DEBUG, Verbosity.VERBOSE, substitutions);
	}

	public void info(String toLog) {

		log(toLog, Level.INFO);
	}

	public void info(String toLog, Verbosity verbosity) {

		log(toLog, Level.INFO, verbosity);
	}

	public void info(String toLog, Object... substitutions) {

		log(toLog, Level.INFO, substitutions);
	}

	public void info(String toLog, Verbosity verbosity, Object... substitutions) {

		log(toLog, Level.INFO, verbosity, substitutions);
	}

	public void warn(String toLog) {

		log(toLog, Level.WARN);
	}

	public void warn(String toLog, Verbosity verbosity) {

		log(toLog, Level.WARN, verbosity);
	}

	public void warn(String toLog, Object... substitutions) {

		log(toLog, Level.WARN, substitutions);
	}

	public void warn(String toLog, Verbosity verbosity, Object... substitutions) {

		log(toLog, Level.WARN, substitutions);
	}

	public void fatal(String toLog) {

		log(toLog, Level.FATAL);
	}

	public void fatal(String toLog, Verbosity verbosity) {

		log(toLog, Level.FATAL, verbosity);
	}

	public void fatal(String toLog, Object... substitutions) {

		log(toLog, Level.FATAL, substitutions);
	}

	public void fatal(String toLog, Verbosity verbosity,
			Object... substitutions) {

		log(toLog, Level.FATAL, verbosity, substitutions);
	}

	@Override
	public Object clone() {

		return new Logger(this);
	}
}
