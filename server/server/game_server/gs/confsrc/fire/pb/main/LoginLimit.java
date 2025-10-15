package fire.pb.main;


public class LoginLimit implements mytools.ConvMain.Checkable ,Comparable<LoginLimit>{

	public int compareTo(LoginLimit o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public LoginLimit(){
		super();
	}
	public LoginLimit(LoginLimit arg){
		this.id=arg.id ;
		this.serverid=arg.serverid ;
		this.loginlimittype=arg.loginlimittype ;
		this.whitelist=arg.whitelist ;
		this.blacklist=arg.blacklist ;
		this.msgid=arg.msgid ;
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
	 * 
	 */
	public int serverid  = 0  ;
	
	public int getServerid(){
		return this.serverid;
	}
	
	public void setServerid(int v){
		this.serverid=v;
	}
	
	/**
	 * 
	 */
	public int loginlimittype  = 0  ;
	
	public int getLoginlimittype(){
		return this.loginlimittype;
	}
	
	public void setLoginlimittype(int v){
		this.loginlimittype=v;
	}
	
	/**
	 * 
	 */
	public String whitelist  = null  ;
	
	public String getWhitelist(){
		return this.whitelist;
	}
	
	public void setWhitelist(String v){
		this.whitelist=v;
	}
	
	/**
	 * 
	 */
	public String blacklist  = null  ;
	
	public String getBlacklist(){
		return this.blacklist;
	}
	
	public void setBlacklist(String v){
		this.blacklist=v;
	}
	
	/**
	 * 
	 */
	public int msgid  = 0  ;
	
	public int getMsgid(){
		return this.msgid;
	}
	
	public void setMsgid(int v){
		this.msgid=v;
	}
	
	
};