package com.world2.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.locojoy.sns.util.ConfigUtil;

public class SystemConfUtil {

	private static Properties systemProp;
	
    static {
    	systemProp = new Properties();
		try {
			systemProp.load(new FileInputStream(ConfigUtil.defaultTomcatCommonConfigPath() + "bugreport.properties"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
    
    public static String getFileAbsPath(){
    	return systemProp.getProperty("file_abs_path");
    }
	
	
}
