package com.locojoy.jmonitor.main;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	
    private Date dat = new Date();
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    
	@Override
	public String format(LogRecord record) {
		StringBuilder sb = new StringBuilder();
		dat.setTime(record.getMillis());
		sb.append(df.format(dat));
		sb.append(": ");
		sb.append(record.getLevel().getLocalizedName());
		sb.append(": ");
		sb.append(formatMessage(record));
		sb.append("\n");
		if (record.getThrown() != null) {
		    try {
		        StringWriter sw = new StringWriter();
		        PrintWriter pw = new PrintWriter(sw);
		        record.getThrown().printStackTrace(pw);
		        pw.close();
			sb.append(sw.toString());
		    } catch (Exception ex) {
		    }
		}
		return sb.toString();
	}

}
