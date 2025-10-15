package fire.pb.role;


public class SRoleNameData implements mytools.ConvMain.Checkable ,Comparable<SRoleNameData>{

	public int compareTo(SRoleNameData o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRoleNameData(){
		super();
	}
	public SRoleNameData(SRoleNameData arg){
		this.id=arg.id ;
		this.xManName=arg.xManName ;
		this.xWomanName=arg.xWomanName ;
		this.mWomanName=arg.mWomanName ;
		this.mManName=arg.mManName ;
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
	public String xManName  = null  ;
	
	public String getXManName(){
		return this.xManName;
	}
	
	public void setXManName(String v){
		this.xManName=v;
	}
	
	/**
	 * 
	 */
	public String xWomanName  = null  ;
	
	public String getXWomanName(){
		return this.xWomanName;
	}
	
	public void setXWomanName(String v){
		this.xWomanName=v;
	}
	
	/**
	 * 
	 */
	public String mWomanName  = null  ;
	
	public String getMWomanName(){
		return this.mWomanName;
	}
	
	public void setMWomanName(String v){
		this.mWomanName=v;
	}
	
	/**
	 * 
	 */
	public String mManName  = null  ;
	
	public String getMManName(){
		return this.mManName;
	}
	
	public void setMManName(String v){
		this.mManName=v;
	}
	
	
};