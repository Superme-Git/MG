package com.world2.utils;

import java.util.*;
import java.io.*;
/**
 * �ļ�������
 * @author lxn
 *
 */
public class FileManager {
	
	private FileManager(){
		
	}
	
	private static FileManager instance = new FileManager();
	
	public static FileManager getInstance(){
		return instance;
	}
	
	public synchronized List<FileBean> getFiles(String searchName){
		String path = UploadConfig.getInstance().getFileAbsPath();
		List<FileBean> list = getFiles(path,searchName);
		return list;
	}
	
	private List<FileBean> getFiles(String dir,String searchName){
		List<FileBean> list = new ArrayList<FileBean>();
		String path = UploadConfig.getInstance().getFileAbsPath();
		int length = path.length();
		File file = new File(dir);
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File item : files){
				if(item.isDirectory())
					list.addAll(getFiles(item.getPath(),searchName));
				else{
					String name = item.getName();
					if(searchName != null && searchName.trim().length()!= 0 && !name.contains(searchName))
						continue;
					FileBean bean = new FileBean();
					bean.setFileName(name);
					if(item.isDirectory())
						bean.setPath(true);
					else
						bean.setPath(false);
					String pathtmp = item.getPath();
					bean.setFilePath(pathtmp.substring(length));
					list.add(bean);
				}
			}
		}
		return list;
	}
}
