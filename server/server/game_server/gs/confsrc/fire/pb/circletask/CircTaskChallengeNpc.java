package fire.pb.circletask;


public class CircTaskChallengeNpc implements mytools.ConvMain.Checkable ,Comparable<CircTaskChallengeNpc>{

	public int compareTo(CircTaskChallengeNpc o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskChallengeNpc(){
		super();
	}
	public CircTaskChallengeNpc(CircTaskChallengeNpc arg){
		this.id=arg.id ;
		this.ctgroup=arg.ctgroup ;
		this.school=arg.school ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.npcid=arg.npcid ;
		this.battleinfo=arg.battleinfo ;
		this.npcsrv=arg.npcsrv ;
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
	public String npcid  = null  ;
	
	public String getNpcid(){
		return this.npcid;
	}
	
	public void setNpcid(String v){
		this.npcid=v;
	}
	
	/**
	 * 
	 */
	public String battleinfo  = null  ;
	
	public String getBattleinfo(){
		return this.battleinfo;
	}
	
	public void setBattleinfo(String v){
		this.battleinfo=v;
	}
	
	/**
	 * 
	 */
	public int npcsrv  = 0  ;
	
	public int getNpcsrv(){
		return this.npcsrv;
	}
	
	public void setNpcsrv(int v){
		this.npcsrv=v;
	}
	
	
};