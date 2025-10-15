package fire.pb.exception;

public class NoSuchUniquePetException extends Exception {

	private static final long serialVersionUID = 5752391467137192101L;
	
	private String exceptionInfo = null;

	public NoSuchUniquePetException(){
		super();
	}
	
	public NoSuchUniquePetException(String errorMessage){
		super(errorMessage);
		this.exceptionInfo = errorMessage;
	}
	
	public NoSuchUniquePetException(String errorMessage,Throwable e){
		super(errorMessage,e);
		this.exceptionInfo = errorMessage;
		e.printStackTrace();
	}
	
	public NoSuchUniquePetException(Throwable e){
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
