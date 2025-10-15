package fire.pb.circletask;


public class Random4AnYeMaXiTuan implements mytools.ConvMain.Checkable ,Comparable<Random4AnYeMaXiTuan>{

	public int compareTo(Random4AnYeMaXiTuan o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Random4AnYeMaXiTuan(){
		super();
	}
	public Random4AnYeMaXiTuan(Random4AnYeMaXiTuan arg){
		this.id=arg.id ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.roundmin=arg.roundmin ;
		this.roundmax=arg.roundmax ;
		this.questrate=arg.questrate ;
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
	public int levelmin  = 0  ;
	
	public int getLevelmin(){
		return this.levelmin;
	}
	
	public void setLevelmin(int v){
		this.levelmin=v;
	}
	
	/**
	 * 
	 */
	public int levelmax  = 0  ;
	
	public int getLevelmax(){
		return this.levelmax;
	}
	
	public void setLevelmax(int v){
		this.levelmax=v;
	}
	
	/**
	 * 
	 */
	public int roundmin  = 0  ;
	
	public int getRoundmin(){
		return this.roundmin;
	}
	
	public void setRoundmin(int v){
		this.roundmin=v;
	}
	
	/**
	 * 
	 */
	public int roundmax  = 0  ;
	
	public int getRoundmax(){
		return this.roundmax;
	}
	
	public void setRoundmax(int v){
		this.roundmax=v;
	}
	
	/**
	 * 
	 */
	public String questrate  = null  ;
	
	public String getQuestrate(){
		return this.questrate;
	}
	
	public void setQuestrate(String v){
		this.questrate=v;
	}
	
	
};