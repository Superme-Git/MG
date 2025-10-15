package com.world2.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class UploadConfig {
	
	static String path = null;
	//static PropertiesConfiguration  passwdConfig;
	public final static String FILE_SEPARATOR = "/"; 
	private PropertiesConfiguration  config = null;
	
	private UploadConfig() {
		path = defaultTomcatCommonConfigPath();
		initConfigproperties();
	}
	
	private static UploadConfig instance = new UploadConfig();
	
	public static UploadConfig getInstance(){
		if(instance == null)
			instance = new UploadConfig();
		return instance;
	}
	
    public String defaultTomcatCommonConfigPath() {

    	String cpath = "";
    	cpath = UploadConfig.class.getResource("/").getPath();
    	try {
    		cpath = URLDecoder.decode(cpath, "UTF-8");
    		int posi = cpath.indexOf("webapps");
    		if (posi > 0) {
    			cpath = cpath.substring(0, posi);
    			cpath = cpath + "common/upload_config/";
    		}
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    	return cpath;
    }
    
    public String getTomcatHome() {
    	String cpath = "";
    	cpath = UploadConfig.class.getResource("/").getPath();
    	try {
    		cpath = URLDecoder.decode(cpath, "UTF-8");
    		int posi = cpath.indexOf("webapps");
    		if (posi > 0) {
    			cpath = cpath.substring(0, posi);
    		}
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    	return cpath;
    }
    
	
	public String getFileAbsPath() {
		return config.getString("fileabspath");
	}
	
	
	public String getUploadWindow(){
		return config.getString("UploadWindow");
	}
	
	
	public String getUploadWindowCountLimit(){
		return config.getString("UploadWindow_LimitCount");
	}
	
	
	private void initConfigproperties() {
		try {
			config = new PropertiesConfiguration(path+"conf.props");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if ("../../../dfs".indexOf("..")> -1) {
			System.out.println(true);
		}
	}

}
