package com.mammoth.util;

import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class TimeStamp {
	/**
	 * 打印服务器构建时间
	 */
	private static String getBuildTime(String jarname) {
		try{
			JarFile jarFile = new JarFile(jarname);
			Manifest mf = jarFile.getManifest();
			return mf.getMainAttributes().getValue("tstamp");
		} catch(Exception e){
		}
		return "";
	}
	
	public static void log(String message){
		System.out.println(message);
	}

	public static String getTimeStampInformation() {
		String time = getBuildTime("LogViewer.jar");
		if(time == null) return "无法取得构建时间";
		if(time.length() == 0) return "无法取得构建时间";
		return "构建时间:" + time;
	}
}
