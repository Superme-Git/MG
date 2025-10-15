package fire.pb.circletask;


public class CircTaskPatrol implements mytools.ConvMain.Checkable ,Comparable<CircTaskPatrol>{

	public int compareTo(CircTaskPatrol o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskPatrol(){
		super();
	}
	public CircTaskPatrol(CircTaskPatrol arg){
		this.id=arg.id ;
		this.ctgroup=arg.ctgroup ;
		this.schoolid=arg.schoolid ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.mapid=arg.mapid ;
		this.battlecfgid=arg.battlecfgid ;
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
	public int ctgroup  = 0  ;
	
	public int getCtgroup(){
		return this.ctgroup;
	}
	
	public void setCtgroup(int v){
		this.ctgroup=v;
	}
	
	/**
	 * 
	 */
	public int schoolid  = 0  ;
	
	public int getSchoolid(){
		return this.schoolid;
	}
	
	public void setSchoolid(int v){
		this.schoolid=v;
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
	public String mapid  = null  ;
	
	public String getMapid(){
		return this.mapid;
	}
	
	public void setMapid(String v){
		this.mapid=v;
	}
	
	/**
	 * 
	 */
	public String battlecfgid  = null  ;
	
	public String getBattlecfgid(){
		return this.battlecfgid;
	}
	
	public void setBattlecfgid(String v){
		this.battlecfgid=v;
	}
	
	
};