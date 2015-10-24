package com.github.lutzblox.engine.logging;

public class BoundLogger extends Logger {

	private Logger[] loggers;

	public BoundLogger(Logger logger, Logger... loggers) {

		super(logger);

		this.loggers = loggers;
	}

	public Logger[] getLoggers() {

		Logger[] l = new Logger[loggers.length + 1];

		l[0] = this;

		for (int i = 0; i < loggers.length; i++) {

			l[i + 1] = loggers[i];
		}

		return l;
	}

	@Override
	public void log(String toLog) {

		super.log(toLog);

		for (Logger logger : loggers) {

			logger.log(toLog);
		}
	}

	@Override
	public void log(String toLog, Object... substitutions) {

		super.log(toLog, substitutions);

		for (Logger logger : loggers) {

			logger.log(toLog, substitutions);
		}
	}

	@Override
	public void log(String toLog, Level level) {

		super.log(toLog, level);

		for (Logger logger : loggers) {

			logger.log(toLog, level);
		}
	}

	@Override
	public void log(String toLog, Level level, Object... substitutions) {

		super.log(toLog, level, substitutions);

		for (Logger logger : loggers) {

			logger.log(toLog, level, substitutions);
		}
	}

	@Override
	public void debug(String toLog) {

		super.debug(toLog);

		for (Logger logger : loggers) {

			logger.debug(toLog);
		}
	}

	@Override
	public void debug(String toLog, Object... substitutions) {

		super.debug(toLog, substitutions);

		for (Logger logger : loggers) {

			logger.debug(toLog, substitutions);
		}
	}

	@Override
	public void info(String toLog) {

		super.info(toLog);

		for (Logger logger : loggers) {

			logger.info(toLog);
		}
	}

	@Override
	public void info(String toLog, Object... substitutions) {

		super.info(toLog, substitutions);

		for (Logger logger : loggers) {

			logger.info(toLog, substitutions);
		}
	}

	@Override
	public void warn(String toLog) {

		super.warn(toLog);

		for (Logger logger : loggers) {

			logger.warn(toLog);
		}
	}

	@Override
	public void warn(String toLog, Object... substitutions) {

		super.warn(toLog, substitutions);

		for (Logger logger : loggers) {

			logger.warn(toLog, substitutions);
		}
	}

	@Override
	public void fatal(String toLog) {

		super.fatal(toLog);

		for (Logger logger : loggers) {

			logger.fatal(toLog);
		}

		log(toLog, Level.FATAL);
	}

	@Override
	public void fatal(String toLog, Object... substitutions) {

		super.fatal(toLog, substitutions);

		for (Logger logger : loggers) {

			logger.fatal(toLog, substitutions);
		}
	}

	@Override
	public Object clone() {

		return new BoundLogger(this, loggers);
	}
}
