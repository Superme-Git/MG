package fire.pb.role;


public class SServerMaxLevelConfig implements mytools.ConvMain.Checkable ,Comparable<SServerMaxLevelConfig>{

	public int compareTo(SServerMaxLevelConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SServerMaxLevelConfig(){
		super();
	}
	public SServerMaxLevelConfig(SServerMaxLevelConfig arg){
		this.id=arg.id ;
		this.maxlevel=arg.maxlevel ;
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
	public int maxlevel  = 0  ;
	
	public int getMaxlevel(){
		return this.maxlevel;
	}
	
	public void setMaxlevel(int v){
		this.maxlevel=v;
	}
	
	
};