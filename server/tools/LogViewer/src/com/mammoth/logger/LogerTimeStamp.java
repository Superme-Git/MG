package com.mammoth.logger;

/**
 * 时间戳相关日志
 * 
 * @author liuyj
 *
 */
public class LogerTimeStamp {
	public static final byte TYPE_DEFAULT = 1;
	public static final byte TYPE_ALL = 2;
	public static final byte TYPE_H_M_S_MS = 3;
	protected int year;
	protected int month;
	protected int day;
	protected int hour;
	protected int minute;
	protected int second;
	protected int millisecond;
	
	private static byte type = TYPE_H_M_S_MS;
	
	private static StringBuilder sb = new StringBuilder();
	public String toString(){
		sb.setLength(0);
		if(type == TYPE_ALL){
			sb.append(year).append('-').append(month).append('-').append(day).append(' ');
		} 
		sb.append(hour).append(':').append(minute).append(':').append(second);
		if(type == TYPE_ALL || type == TYPE_H_M_S_MS){
			sb.append('.').append(millisecond);
		}
		return sb.toString();
	}
}
