package cn.com.zc.db.exception;

public class FileParseException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String exceptionInfo = null;

	public FileParseException(){
		super();
	}
	
	public FileParseException(String errorMessage){
		super(errorMessage);
		this.exceptionInfo = errorMessage;
	}
	
	public FileParseException(String errorMessage,Throwable e){
		super(errorMessage,e);
		this.exceptionInfo = errorMessage;
		e.printStackTrace();
	}
	
	public FileParseException(Throwable e){
		super(e);
		e.printStackTrace();
	}
	
	public String getExceptionMessage(){
		return exceptionInfo;
	}
	
	public String toString() {
		return this.getClass().getName()+"{"
				+ ", exceptionInfo='"
				+ exceptionInfo
				+ "'}";
	}


}
