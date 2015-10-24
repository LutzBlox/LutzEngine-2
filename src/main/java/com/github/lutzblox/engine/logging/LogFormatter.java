package com.github.lutzblox.engine.logging;

import com.github.lutzblox.engine.logging.Logger.Timestamp;
import com.github.lutzblox.engine.logging.Logger.Timestamp.Month;

public class LogFormatter {

	public static class Defaults {

		public static final LogFormatter TIMESTAMP_FORMAT_DEFAULT = new LogFormatter(
				"m/dd/yyyy h:ii:ss");
	}

	private String timestampFormat;

	public LogFormatter(String timestampFormat) {

		this.timestampFormat = timestampFormat;
	}

	public void setTimestampFormat(String format) {

		this.timestampFormat = format;
	}

	public String getTimestampFormat() {

		return timestampFormat;
	}

	public String formatTimestamp(Timestamp timestamp) {

		String format = getTimestampFormat();

		format = format.replace("dd", addZeros(timestamp.getDate(), 2));
		format = format.replace("d", Integer.toString(timestamp.getDate()));
		format = format.replace("mm", addZeros(timestamp.getMonth(), 2));
		format = format.replace("m", Integer.toString(timestamp.getMonth()));
		format = format.replace("MM", Month.getMonth(timestamp.getMonth())
				.getFullName());
		format = format.replace("M", Month.getMonth(timestamp.getMonth())
				.getAbbreviatedName());
		format = format.replace("yyyy", addZeros(timestamp.getYear(), 2));
		format = format.replace("yy",
				formatYearToLength(Integer.toString(timestamp.getYear()), 2));
		format = format.replace("y", Integer.toString(timestamp.getYear()));

		format = format.replace("hh", addZeros(timestamp.get12Hour(), 2));
		format = format.replace("h", Integer.toString(timestamp.get12Hour()));
		format = format.replace("HH", addZeros(timestamp.get24Hour(), 2));
		format = format.replace("H", Integer.toString(timestamp.get24Hour()));
		format = format.replace("ii", addZeros(timestamp.getMinute(), 2));
		format = format.replace("i", Integer.toString(timestamp.getMinute()));
		format = format.replace("ss", addZeros(timestamp.getSecond(), 2));
		format = format.replace("s", Integer.toString(timestamp.getSecond()));

		return format;
	}

	private String addZeros(int toAdd, int desiredLength) {

		String start = Integer.toString(toAdd);

		while (start.length() < desiredLength) {

			start = "0" + start;
		}

		return start;
	}

	private String formatYearToLength(String str, int length) {

		if (str.length() > length) {

			str = str.substring(str.length() - length);
		}

		return str;
	}
}
