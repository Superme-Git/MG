package com.mammoth.logger.gui;

import java.util.ArrayList;
import java.util.List;

import com.mammoth.logger.Log;
import com.mammoth.logger.Position;

public class LineEntity extends TreeEntity {
	protected int startLine;
	protected int endLine;
	protected LogFileEntity parent;
    private List<Log> currentLog = new ArrayList<Log>();
	protected long startPosition;
	protected long endPosition;
	private String startAndEndTime="";
	public LineEntity(LogFileEntity parent,Position start,Position end){
		startLine = start.line;
		endLine = end.line;
		startPosition = start.position;
		endPosition = end.position;
		this.parent = parent;
		
	}	
	public String getName(){
		return toString();
	}
	
	public String toString(){
//        currentLog =getViewList();
//        String startTime="";
//        String endTime="";
//        for(int i=0;i<currentLog.size();i++)
//        {
//            startTime=currentLog.get(i).getTime();
//            if(startTime.length()>5)
//                break;
//        }
//        for(int j=currentLog.size()-1;j>=0;j--)
//        {
//            endTime=currentLog.get(j).getTime();
//            if(endTime.length()>5)
//                break;
//        }
//        startAndEndTime=" ±º‰:" +startTime.substring(0,startTime.indexOf("."))+"÷¡" +  endTime.substring(0,endTime.indexOf("."));
//		return (startLine +1) + "-" + endLine+startAndEndTime;
	    return (startLine +1) + "-" + endLine;
	}
	
	public List getChildren(){
		return null;
	}

	public List<Log> getViewList() {
		return parent.getViewList(startLine,endLine,startPosition, endPosition);
	}

}
