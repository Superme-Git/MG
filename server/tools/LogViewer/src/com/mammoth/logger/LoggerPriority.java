package com.mammoth.logger;

public class LoggerPriority {
	public static final String LEVEL_DEBUG = "[DEBUG]";
	public static final String LEVEL_INFO = "[INFO]";
	public static final String LEVEL_WARN = "[WARN]";
	public static final String LEVEL_FATAL = "[FATAL]";
	public static final String LEVEL_ERROR = "[ERROR]";
	public static final String REGEX_PRIORITY = "\\[(DEBUG|INFO|WARN|FATAL|ERROR)\\]-\\[(.+?)\\((.+?)\\)\\] (.*?)\n";
}
