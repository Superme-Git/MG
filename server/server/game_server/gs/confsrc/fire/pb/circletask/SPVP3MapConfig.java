package fire.pb.circletask;


public class SPVP3MapConfig implements mytools.ConvMain.Checkable ,Comparable<SPVP3MapConfig>{

	public int compareTo(SPVP3MapConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPVP3MapConfig(){
		super();
	}
	public SPVP3MapConfig(SPVP3MapConfig arg){
		this.id=arg.id ;
		this.level1=arg.level1 ;
		this.level2=arg.level2 ;
		this.map=arg.map ;
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
	public int map  = 0  ;
	
	public int getMap(){
		return this.map;
	}
	
	public void setMap(int v){
		this.map=v;
	}
	
	
};