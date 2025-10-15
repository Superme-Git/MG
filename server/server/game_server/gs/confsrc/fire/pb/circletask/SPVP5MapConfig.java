package fire.pb.circletask;


public class SPVP5MapConfig implements mytools.ConvMain.Checkable ,Comparable<SPVP5MapConfig>{

	public int compareTo(SPVP5MapConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPVP5MapConfig(){
		super();
	}
	public SPVP5MapConfig(SPVP5MapConfig arg){
		this.id=arg.id ;
		this.level1=arg.level1 ;
		this.level2=arg.level2 ;
		this.generalMap=arg.generalMap ;
		this.excellentMap=arg.excellentMap ;
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
	public int level1  = 0  ;
	
	public int getLevel1(){
		return this.level1;
	}
	
	public void setLevel1(int v){
		this.level1=v;
	}
	
	/**
	 * 
	 */
	public int level2  = 0  ;
	
	public int getLevel2(){
		return this.level2;
	}
	
	public void setLevel2(int v){
		this.level2=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> generalMap  ;
	
	public java.util.ArrayList<Integer> getGeneralMap(){
		return this.generalMap;
	}
	
	public void setGeneralMap(java.util.ArrayList<Integer> v){
		this.generalMap=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> excellentMap  ;
	
	public java.util.ArrayList<Integer> getExcellentMap(){
		return this.excellentMap;
	}
	
	public void setExcellentMap(java.util.ArrayList<Integer> v){
		this.excellentMap=v;
	}
	
	
};