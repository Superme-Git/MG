package fire.pb.exception;

public class SceneResourceLoadException extends Exception {
	
	private static final long serialVersionUID = 5577844719534218454L;
	
	private String exceptionInfo = null;

	public SceneResourceLoadException(){
		super();
	}
	
	public SceneResourceLoadException(String errorMessage){
		super(errorMessage);
		this.exceptionInfo = errorMessage;
	}
	
	public SceneResourceLoadException(String errorMessage,Throwable e){
		super(errorMessage,e);
		this.exceptionInfo = errorMessage;
		e.printStackTrace();
	}
	
	public SceneResourceLoadException(Throwable e){
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
