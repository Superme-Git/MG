package fire.pb.game;


public class SPVPCrossWhiteList implements mytools.ConvMain.Checkable ,Comparable<SPVPCrossWhiteList>{

	public int compareTo(SPVPCrossWhiteList o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPVPCrossWhiteList(){
		super();
	}
	public SPVPCrossWhiteList(SPVPCrossWhiteList arg){
		this.id=arg.id ;
		this.userid=arg.userid ;
		this.roleid=arg.roleid ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
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
	public int userid  = 0  ;
	
	public int getUserid(){
		return this.userid;
	}
	
	public void setUserid(int v){
		this.userid=v;
	}
	
	/**
	 * 
	 */
	public long roleid  = 0L  ;
	
	public long getRoleid(){
		return this.roleid;
	}
	
	public void setRoleid(long v){
		this.roleid=v;
	}
	
	
};