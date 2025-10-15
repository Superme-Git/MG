package fire.pb.skill;


public class SInbornInfo implements mytools.ConvMain.Checkable ,Comparable<SInbornInfo>{

	public int compareTo(SInbornInfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInbornInfo(){
		super();
	}
	public SInbornInfo(SInbornInfo arg){
		this.id=arg.id ;
		this.skillname=arg.skillname ;
		this.school=arg.school ;
		this.isMain=arg.isMain ;
		this.isnbskill=arg.isnbskill ;
		this.maxLevel=arg.maxLevel ;
		this.dependInborn=arg.dependInborn ;
		this.dependLevel=arg.dependLevel ;
		this.nbskilldependLevel=arg.nbskilldependLevel ;
		this.consumerule=arg.consumerule ;
		this.pointToSkillList=arg.pointToSkillList ;
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
	public String skillname  = null  ;
	
	public String getSkillname(){
		return this.skillname;
	}
	
	public void setSkillname(String v){
		this.skillname=v;
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
	public boolean isMain  = false  ;
	
	public boolean getIsMain(){
		return this.isMain;
	}
	
	public void setIsMain(boolean v){
		this.isMain=v;
	}
	
	/**
	 * 
	 */
	public boolean isnbskill  = false  ;
	
	public boolean getIsnbskill(){
		return this.isnbskill;
	}
	
	public void setIsnbskill(boolean v){
		this.isnbskill=v;
	}
	
	/**
	 * 
	 */
	public int maxLevel  = 0  ;
	
	public int getMaxLevel(){
		return this.maxLevel;
	}
	
	public void setMaxLevel(int v){
		this.maxLevel=v;
	}
	
	/**
	 * 
	 */
	public String dependInborn  = null  ;
	
	public String getDependInborn(){
		return this.dependInborn;
	}
	
	public void setDependInborn(String v){
		this.dependInborn=v;
	}
	
	/**
	 * 
	 */
	public int dependLevel  = 0  ;
	
	public int getDependLevel(){
		return this.dependLevel;
	}
	
	public void setDependLevel(int v){
		this.dependLevel=v;
	}
	
	/**
	 * 
	 */
	public String nbskilldependLevel  = null  ;
	
	public String getNbskilldependLevel(){
		return this.nbskilldependLevel;
	}
	
	public void setNbskilldependLevel(String v){
		this.nbskilldependLevel=v;
	}
	
	/**
	 * 
	 */
	public int consumerule  = 0  ;
	
	public int getConsumerule(){
		return this.consumerule;
	}
	
	public void setConsumerule(int v){
		this.consumerule=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> pointToSkillList  ;
	
	public java.util.ArrayList<Integer> getPointToSkillList(){
		return this.pointToSkillList;
	}
	
	public void setPointToSkillList(java.util.ArrayList<Integer> v){
		this.pointToSkillList=v;
	}
	
	
};