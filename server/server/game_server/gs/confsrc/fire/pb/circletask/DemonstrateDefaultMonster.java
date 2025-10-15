package fire.pb.circletask;


public class DemonstrateDefaultMonster implements mytools.ConvMain.Checkable ,Comparable<DemonstrateDefaultMonster>{

	public int compareTo(DemonstrateDefaultMonster o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DemonstrateDefaultMonster(){
		super();
	}
	public DemonstrateDefaultMonster(DemonstrateDefaultMonster arg){
		this.id=arg.id ;
		this.npcid=arg.npcid ;
		this.mapid=arg.mapid ;
		this.schoolname=arg.schoolname ;
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
	public int npcid  = 0  ;
	
	public int getNpcid(){
		return this.npcid;
	}
	
	public void setNpcid(int v){
		this.npcid=v;
	}
	
	/**
	 * 
	 */
	public int mapid  = 0  ;
	
	public int getMapid(){
		return this.mapid;
	}
	
	public void setMapid(int v){
		this.mapid=v;
	}
	
	/**
	 * 
	 */
	public String schoolname  = null  ;
	
	public String getSchoolname(){
		return this.schoolname;
	}
	
	public void setSchoolname(String v){
		this.schoolname=v;
	}
	
	
};