package fire.pb.main;


public class LoginQueueUp implements mytools.ConvMain.Checkable ,Comparable<LoginQueueUp>{

	public int compareTo(LoginQueueUp o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public LoginQueueUp(){
		super();
	}
	public LoginQueueUp(LoginQueueUp arg){
		this.id=arg.id ;
		this.connectusernum=arg.connectusernum ;
		this.time=arg.time ;
		this.enterusernum=arg.enterusernum ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 连接人数
	 */
	public int connectusernum  = 0  ;
	
	public int getConnectusernum(){
		return this.connectusernum;
	}
	
	public void setConnectusernum(int v){
		this.connectusernum=v;
	}
	
	/**
	 * 时间
	 */
	public int time  = 0  ;
	
	public int getTime(){
		return this.time;
	}
	
	public void setTime(int v){
		this.time=v;
	}
	
	/**
	 * 放入人数
	 */
	public int enterusernum  = 0  ;
	
	public int getEnterusernum(){
		return this.enterusernum;
	}
	
	public void setEnterusernum(int v){
		this.enterusernum=v;
	}
	
	
};