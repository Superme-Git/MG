package fire.pb.role;


public class SServiceLevelConfig implements mytools.ConvMain.Checkable ,Comparable<SServiceLevelConfig>{

	public int compareTo(SServiceLevelConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SServiceLevelConfig(){
		super();
	}
	public SServiceLevelConfig(SServiceLevelConfig arg){
		this.id=arg.id ;
		this.slevel=arg.slevel ;
		this.lastday=arg.lastday ;
		this.openday=arg.openday ;
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
	public int slevel  = 0  ;
	
	public int getSlevel(){
		return this.slevel;
	}
	
	public void setSlevel(int v){
		this.slevel=v;
	}
	
	/**
	 * 
	 */
	public int lastday  = 0  ;
	
	public int getLastday(){
		return this.lastday;
	}
	
	public void setLastday(int v){
		this.lastday=v;
	}
	
	/**
	 * 
	 */
	public int openday  = 0  ;
	
	public int getOpenday(){
		return this.openday;
	}
	
	public void setOpenday(int v){
		this.openday=v;
	}
	
	
};