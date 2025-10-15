package fire.pb.buff;


public class SBuffClashs implements mytools.ConvMain.Checkable ,Comparable<SBuffClashs>{

	public int compareTo(SBuffClashs o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBuffClashs(){
		super();
	}
	public SBuffClashs(SBuffClashs arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.clashbuffs=arg.clashbuffs ;
		this.invalidbuffs=arg.invalidbuffs ;
		this.overridebuffs=arg.overridebuffs ;
		this.tips=arg.tips ;
		this.clashmapid=arg.clashmapid ;
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
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 
	 */
	public String clashbuffs  = null  ;
	
	public String getClashbuffs(){
		return this.clashbuffs;
	}
	
	public void setClashbuffs(String v){
		this.clashbuffs=v;
	}
	
	/**
	 * 
	 */
	public String invalidbuffs  = null  ;
	
	public String getInvalidbuffs(){
		return this.invalidbuffs;
	}
	
	public void setInvalidbuffs(String v){
		this.invalidbuffs=v;
	}
	
	/**
	 * 
	 */
	public String overridebuffs  = null  ;
	
	public String getOverridebuffs(){
		return this.overridebuffs;
	}
	
	public void setOverridebuffs(String v){
		this.overridebuffs=v;
	}
	
	/**
	 * 
	 */
	public String tips  = null  ;
	
	public String getTips(){
		return this.tips;
	}
	
	public void setTips(String v){
		this.tips=v;
	}
	
	/**
	 * 
	 */
	public String clashmapid  = null  ;
	
	public String getClashmapid(){
		return this.clashmapid;
	}
	
	public void setClashmapid(String v){
		this.clashmapid=v;
	}
	
	
};