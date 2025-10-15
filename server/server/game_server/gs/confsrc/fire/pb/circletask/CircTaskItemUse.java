package fire.pb.circletask;


public class CircTaskItemUse implements mytools.ConvMain.Checkable ,Comparable<CircTaskItemUse>{

	public int compareTo(CircTaskItemUse o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskItemUse(){
		super();
	}
	public CircTaskItemUse(CircTaskItemUse arg){
		this.id=arg.id ;
		this.ctgroup=arg.ctgroup ;
		this.school=arg.school ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.mapid=arg.mapid ;
		this.xpos=arg.xpos ;
		this.ypos=arg.ypos ;
		this.excurs=arg.excurs ;
		this.israndompos=arg.israndompos ;
		this.itemid=arg.itemid ;
		this.hanhua=arg.hanhua ;
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
	public int xpos  = 0  ;
	
	public int getXpos(){
		return this.xpos;
	}
	
	public void setXpos(int v){
		this.xpos=v;
	}
	
	/**
	 * 
	 */
	public int ypos  = 0  ;
	
	public int getYpos(){
		return this.ypos;
	}
	
	public void setYpos(int v){
		this.ypos=v;
	}
	
	/**
	 * 
	 */
	public int excurs  = 0  ;
	
	public int getExcurs(){
		return this.excurs;
	}
	
	public void setExcurs(int v){
		this.excurs=v;
	}
	
	/**
	 * 
	 */
	public int israndompos  = 0  ;
	
	public int getIsrandompos(){
		return this.israndompos;
	}
	
	public void setIsrandompos(int v){
		this.israndompos=v;
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
	public int hanhua  = 0  ;
	
	public int getHanhua(){
		return this.hanhua;
	}
	
	public void setHanhua(int v){
		this.hanhua=v;
	}
	
	
};