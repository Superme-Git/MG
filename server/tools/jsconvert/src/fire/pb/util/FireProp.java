package fire.pb.util;

import java.util.Properties;

import fire.pb.main.ConfigMgr;


public class FireProp {

	
	public static int getIntValue(Properties prop, String key) {
		String value = prop.getProperty(key);
		return Integer.parseInt(value.trim());

	}

	
	public static long getLongValue(Properties prop, String key) {
		String value = prop.getProperty(key);
		return Long.parseLong(value.trim());

	}

	public static float getFloatValue(Properties prop, String key) {
		String value = prop.getProperty(key);
		return Float.parseFloat(value.trim());

	}
	public static boolean getBooleanValue(Properties prop, String key) {
		String value = prop.getProperty(key);
		return value.trim().equals("1");
		
	}
   
	public static int getIntValue(String fName, String key) {
		Properties prop = ConfigMgr.getInstance().getPropConf(fName);
		String value = prop.getProperty(key);
		return Integer.parseInt(value.trim());
	}

	public static long getLongValue(String fName, String key) {
		Properties prop = ConfigMgr.getInstance().getPropConf(fName);
		String value = prop.getProperty(key);
		return Long.parseLong(value.trim());

	}

	public static float getFloatValue(String fName, String key) {
		Properties prop = ConfigMgr.getInstance().getPropConf(fName);
		String value = prop.getProperty(key);
		return Float.parseFloat(value.trim());
	}
	public static boolean getBooleanValue(String fName, String key) {
		Properties prop = ConfigMgr.getInstance().getPropConf(fName);
		String value = prop.getProperty(key);
		return value.trim().equals("1");
	}
	
	
	public static String getStringValue(Properties prop, String key) {
		return prop.getProperty(key);
	}
	
}
