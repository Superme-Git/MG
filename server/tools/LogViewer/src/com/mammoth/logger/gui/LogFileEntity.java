package com.mammoth.logger.gui;

import com.mammoth.logger.Log;
import com.mammoth.logger.LogLineReader;
import com.mammoth.logger.Position;

import java.util.*;

public class LogFileEntity extends TreeEntity{
	public static final String DEFAULT_ENCODING = "UTF-8";
	private String file;
	private String encoding = DEFAULT_ENCODING;
	
	private LogLineReader reader = new LogLineReader();
	
	protected List<LineEntity> children = new ArrayList<LineEntity>();
	

	
	public LogFileEntity(String file){
		this(file,DEFAULT_ENCODING);
	}
	
	public LogFileEntity(String file,String encoding){
		setFile(file);
		setEncoding(encoding);
	}
	
	public void setFile(String file){
		this.file = file;
	}
	
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	
	
	public void init(){
		reader.setEncoding(encoding);
		reader.setFile(file);
		children.clear();//清空children，防止重复添加
		//TODO 设置encoding时有错误
		reader.parse();
		Position lastP = null;
		for(Position p:reader.lines){
			if(lastP != null){
				children.add(new LineEntity(this,lastP,p));
			}
			lastP = p;
		}
	}
	
	public List<Log> getViewList(int startLine,int endLine,long startPosition,long endPosition){
		List<Log> list = new ArrayList<Log>();
		String str = "";
		int i = startLine+1;
		reader.mark2(startPosition, endPosition);
		Log lastLog = null;
		while((str = reader.readLine()) != null){
			Log log = new Log();
			log.setOrigin(str);
			String strs[] = str.split(" ", 5);
			if(strs.length == 5){
				log.setLineNumber(i);
				log.setDate(strs[0]);
				log.setTime(strs[1]);
				log.setLevel(strs[2]);
				log.setSource(strs[3]);
				log.setMessage(strs[4]);
				lastLog = log;
				list.add(log);
				i++;
			} else if(strs.length == 2){
				log.setLineNumber(i);
				if(lastLog != null){
					log.setDate(lastLog.getDate());
					log.setTime(lastLog.getTime());
					log.setLevel(lastLog.getLevel());
				} 
				log.setMessage(strs[0]);
				list.add(log);
				i++;
			}
		}
		return list;
	}
	
	public String getName(){
		return toString();
	}
	
	public String toString(){
		return file;
	}
	
	public List getChildren(){
		return children;
	}
}

