package com.locojoy.jmonitor.webapp;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;


public class Mime {

	static private Properties prop = new Properties();
	static private String propfn = "mime.properties";
	static private String defaulttype = "application/octet-stream";
	private static Logger logger =  Logger.getLogger(Mime.class.getName());

	static {
		try {
			prop.load(Mime.class.getResourceAsStream(propfn));
			logger.info(propfn + " loaded");
		} catch (IOException e) {
			logger.info(propfn + " load error");
		}
	}

	static public String type(String suffix) {
		String t = prop.getProperty(suffix);
		return t != null ? t : defaulttype;
	}
}
