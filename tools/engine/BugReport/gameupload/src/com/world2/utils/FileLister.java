package com.world2.utils;

import java.io.*;
public class FileLister {
	class FileNameFilterMethod implements FilenameFilter {
		private String fileName = null;
		public FileNameFilterMethod(String fileName){
			this.fileName = fileName; 
		}
		public boolean accept(File dir, String name) {
			if (name.startsWith(fileName)){
				return true;
			}
			else
				return false;
		}
	}
	
	private FileLister(){
		
	}
	
	private static FileLister instance = new FileLister();
	
	public static FileLister getInstance(){
		return instance;
	}
	
	private static String rootpath = UploadConfig.getInstance().getFileAbsPath();
	
	public String[] getFile(String name){
		File file = new File(rootpath);
		if(null == name)
			return null;
		return file.list(new FileNameFilterMethod(name));
	}
}
