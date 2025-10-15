package fire.pb.circletask;


public class CircTaskItemCollect implements mytools.ConvMain.Checkable ,Comparable<CircTaskItemCollect>{

	public int compareTo(CircTaskItemCollect o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskItemCollect(){
		super();
	}
	public CircTaskItemCollect(CircTaskItemCollect arg){
		this.id=arg.id ;
		this.ctgroup=arg.ctgroup ;
		this.school=arg.school ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.mapid=arg.mapid ;
		this.monsterid=arg.monsterid ;
		this.itemid=arg.itemid ;
		this.itemnum=arg.itemnum ;
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
	public String monsterid  = null  ;
	
	public String getMonsterid(){
		return this.monsterid;
	}
	
	public void setMonsterid(String v){
		this.monsterid=v;
	}
	
	/**
	 * 
	 */
	public int itemid  = 0  ;
	
	public int getItemid(){
		return this.itemid;
	}
	
	public void setItemid(int v){
		this.itemid=v;
	}
	
	/**
	 * 
	 */
	public int itemnum  = 0  ;
	
	public int getItemnum(){
		return this.itemnum;
	}
	
	public void setItemnum(int v){
		this.itemnum=v;
	}
	
	
};