package com.mammoth.logger;


/**
 * »’÷æ
 * 
 * @author liuyj
 *
 */
public class Log {
	public static final int LEVEL_NONE = 0;
	public static final int LEVEL_DEBUG = 1;
	public static final int LEVEL_FATAL = 2;
	public static final int LEVEL_WARN = 3;
	public static final int LEVEL_INFO = 4;
	public static final int LEVEL_ERROR = 5;
	
	private int lineNumber = 1;//0
	private String date = "";//1
	private String time = "";//2
	private String level = "";//3
	private String source = "";//4
	private String message = "";//5

	
	public Log(){
	}
	
	public Log(String line){
	}
	
	public int getLogLevel(){
		if(level.contains("INFO")){
			return LEVEL_INFO;
		} else if(level.contains("DEBUG")){
			return LEVEL_DEBUG;
		} else if(level.contains("ERROR")){
			return LEVEL_ERROR;
		} else if(level.contains("WARN")){
			return LEVEL_WARN;
		} else if(level.contains("FATAL")){
			return LEVEL_FATAL;
		}
		return LEVEL_NONE;
	}
	
	public String toString(){
		return lineNumber + " " + date + " " + time + " " + level; 
	}
	
//	public static Log parseLine(int line,String logLine){
//		String strs[] = logLine.split("-", 4);
//		
//		if(strs.length == 4){
//			Log log = new Log();
//			log.setLineNumber(line);
//			log.setTime(strs[0]);
//			log.setLevel(strs[1]);
//			log.setSource(strs[2]);
//			log.setMessage(strs[3]);
//			return log;
//		} else if(strs.length == 1){
//			Log log = new Log();
//			log.setLineNumber(line);
//			log.setMessage(strs[0]);
//			return log;
//		}
//		return null;
//	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getLineNumber() {
		return Integer.valueOf(lineNumber);
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public boolean match(SearchCondition condition){
		if(condition == null){
			return true;
		}
		return condition.match(this);
	}
	
	public static void main(String[] args) throws Exception{

	}

	public void appendMessage(String string) {
		message += string;
	}

	private String origin;
	public void setOrigin(String str) {
		origin = str;
	}
	public String getOrigin(){
		return origin;
	}

	private boolean marked = false;
	public void unmark(){
		marked = false;
	}
	public void mark() {
		System.out.println("marked");
		marked = true;
	}

	public boolean isMarked() {
		return marked;
	}
	
	private boolean searched =false;
	
	public void markSearch(){
		searched = true;
	}
	
	public void unmarkSearch(){
		searched = false;
	}
	
	public boolean isSearched(){
		return searched;
	}
}
