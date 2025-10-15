package fire.pb.circletask;


public class PVPAwardConfig implements mytools.ConvMain.Checkable ,Comparable<PVPAwardConfig>{

	public int compareTo(PVPAwardConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PVPAwardConfig(){
		super();
	}
	public PVPAwardConfig(PVPAwardConfig arg){
		this.id=arg.id ;
		this.type=arg.type ;
		this.flag=arg.flag ;
		this.level1=arg.level1 ;
		this.level2=arg.level2 ;
		this.winawardid=arg.winawardid ;
		this.loseawardid=arg.loseawardid ;
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
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 
	 */
	public int flag  = 0  ;
	
	public int getFlag(){
		return this.flag;
	}
	
	public void setFlag(int v){
		this.flag=v;
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
	public int winawardid  = 0  ;
	
	public int getWinawardid(){
		return this.winawardid;
	}
	
	public void setWinawardid(int v){
		this.winawardid=v;
	}
	
	/**
	 * 
	 */
	public int loseawardid  = 0  ;
	
	public int getLoseawardid(){
		return this.loseawardid;
	}
	
	public void setLoseawardid(int v){
		this.loseawardid=v;
	}
	
	
};