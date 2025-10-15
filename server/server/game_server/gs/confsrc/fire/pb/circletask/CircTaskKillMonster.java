package fire.pb.circletask;


public class CircTaskKillMonster implements mytools.ConvMain.Checkable ,Comparable<CircTaskKillMonster>{

	public int compareTo(CircTaskKillMonster o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskKillMonster(){
		super();
	}
	public CircTaskKillMonster(CircTaskKillMonster arg){
		this.id=arg.id ;
		this.ctgroup=arg.ctgroup ;
		this.school=arg.school ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.mapid=arg.mapid ;
		this.monsterids=arg.monsterids ;
		this.monsternum=arg.monsternum ;
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
	public int school  = 0  ;
	
	public int getSchool(){
		return this.school;
	}
	
	public void setSchool(int v){
		this.school=v;
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
	public String monsterids  = null  ;
	
	public String getMonsterids(){
		return this.monsterids;
	}
	
	public void setMonsterids(String v){
		this.monsterids=v;
	}
	
	/**
	 * 
	 */
	public int monsternum  = 0  ;
	
	public int getMonsternum(){
		return this.monsternum;
	}
	
	public void setMonsternum(int v){
		this.monsternum=v;
	}
	
	
};