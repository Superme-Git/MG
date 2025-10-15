package com.mammoth.logger.gui;

import java.io.File;
import java.util.*;


public class LogEntity extends TreeEntity{
	private String directory;
	private String encoding = LogFileEntity.DEFAULT_ENCODING;
	private List<LogFileEntity> children = new ArrayList<LogFileEntity>();
	
	public LogEntity(){
	}
	
	public void setEncoding(String encoding){
		this.encoding = encoding;
		for(LogFileEntity log:children){
			log.setEncoding(encoding);
			log.init();
		}
	}
	
	public void setDirectory(String directoryPath){		
		this.directory = directoryPath;
	}
	
	public void initDirectory(){
		File f = new File(directory);
		String path = f.getAbsolutePath();
		String[] files= f.list();
		for(String s:files){
			LogFileEntity log = new LogFileEntity(path + "\\" + s);
			log.setEncoding(encoding);
//			children.add(log);
            sortChildren(children,log);
		}
	}
	
	public void initFile(){
		LogFileEntity log = new LogFileEntity(directory);
		log.setEncoding(encoding);
//		children.add(log);
        sortChildren(children,log);
	}
	
	
	public void setFile(String filePath){
		this.directory = filePath;
	}
	
	public void setChildren(List children){
		//JUST OVERRIDER;
	}
	
	public List getChildren(){
		return children; 
	}
	
	public String getName(){
		return directory;
	}
	
	public static void main(String[] args){
		LogEntity log = new LogEntity();
		log.setDirectory("D:\\Downloads\\cl");
		log.initDirectory();
		
		for(LogFileEntity oneLog:log.children){
			System.out.println(oneLog);
		}
	}
	private void sortChildren(List<LogFileEntity> list, LogFileEntity log) {

        if (list.isEmpty()) {
            list.add(log);
            return;
        }
        String file1=log.toString().substring(log.toString().lastIndexOf("\\"));
        String file2="";
        for (int i = 0; i < list.size(); i++) {
            file2=list.get(i).toString();
            file2=file2.substring(file2.lastIndexOf("\\"));
            if (file1.compareTo(file2)< 0) {
                list.add(i, log);
                return;
            }
        }
        list.add(log);
//        for (int i = 0; i < list.size(); i++) {
//            if (log.toString().substring(0,log.toString().lastIndexOf(".")).compareTo(list.get(i).toString().substring(0,list.get(i).toString().lastIndexOf("."))) < 0) {
//                list.add(i, log);
//                return;
//            }
//        }
//        list.add(log);
    }

}
