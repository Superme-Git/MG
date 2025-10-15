package fire.pb.npc;


public class MonsterConfig implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public MonsterConfig(){
		super();
	}
	public MonsterConfig(MonsterConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.fightnpctype=arg.fightnpctype ;
		this.monstertype=arg.monstertype ;
		this.specialtype=arg.specialtype ;
		this.school=arg.school ;
		this.pet=arg.pet ;
		this.colorid=arg.colorid ;
		this.bodytype=arg.bodytype ;
		this.orbinding=arg.orbinding ;
		this.title=arg.title ;
		this.shape=arg.shape ;
		this.randomShapes=arg.randomShapes ;
		this.levelType=arg.levelType ;
		this.level=arg.level ;
		this.minlevellimit=arg.minlevellimit ;
		this.maxlevellimit=arg.maxlevellimit ;
		this.canCatch=arg.canCatch ;
		this.catchRate=arg.catchRate ;
		this.runRate=arg.runRate ;
		this.aiIds=arg.aiIds ;
		this.immunebuffid=arg.immunebuffid ;
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
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 
	 */
	public int fightnpctype  = 0  ;
	
	public int getFightnpctype(){
		return this.fightnpctype;
	}
	
	public void setFightnpctype(int v){
		this.fightnpctype=v;
	}
	
	/**
	 * 
	 */
	public int monstertype  = 0  ;
	
	public int getMonstertype(){
		return this.monstertype;
	}
	
	public void setMonstertype(int v){
		this.monstertype=v;
	}
	
	/**
	 * 
	 */
	public int specialtype  = 0  ;
	
	public int getSpecialtype(){
		return this.specialtype;
	}
	
	public void setSpecialtype(int v){
		this.specialtype=v;
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
	public int pet  = 0  ;
	
	public int getPet(){
		return this.pet;
	}
	
	public void setPet(int v){
		this.pet=v;
	}
	
	/**
	 * 
	 */
	public int colorid  = 0  ;
	
	public int getColorid(){
		return this.colorid;
	}
	
	public void setColorid(int v){
		this.colorid=v;
	}
	
	/**
	 * 
	 */
	public double bodytype  = 0.0  ;
	
	public double getBodytype(){
		return this.bodytype;
	}
	
	public void setBodytype(double v){
		this.bodytype=v;
	}
	
	/**
	 * 
	 */
	public int orbinding  = 0  ;
	
	public int getOrbinding(){
		return this.orbinding;
	}
	
	public void setOrbinding(int v){
		this.orbinding=v;
	}
	
	/**
	 * 
	 */
	public String title  = null  ;
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String v){
		this.title=v;
	}
	
	/**
	 * 
	 */
	public int shape  = 0  ;
	
	public int getShape(){
		return this.shape;
	}
	
	public void setShape(int v){
		this.shape=v;
	}
	
	/**
	 * 
	 */
	public String randomShapes  = null  ;
	
	public String getRandomShapes(){
		return this.randomShapes;
	}
	
	public void setRandomShapes(String v){
		this.randomShapes=v;
	}
	
	/**
	 * 
	 */
	public int levelType  = 0  ;
	
	public int getLevelType(){
		return this.levelType;
	}
	
	public void setLevelType(int v){
		this.levelType=v;
	}
	
	/**
	 * 
	 */
	public int level  = 0  ;
	
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int v){
		this.level=v;
	}
	
	/**
	 * 
	 */
	public int minlevellimit  = 0  ;
	
	public int getMinlevellimit(){
		return this.minlevellimit;
	}
	
	public void setMinlevellimit(int v){
		this.minlevellimit=v;
	}
	
	/**
	 * 
	 */
	public int maxlevellimit  = 0  ;
	
	public int getMaxlevellimit(){
		return this.maxlevellimit;
	}
	
	public void setMaxlevellimit(int v){
		this.maxlevellimit=v;
	}
	
	/**
	 * 
	 */
	public int canCatch  = 0  ;
	
	public int getCanCatch(){
		return this.canCatch;
	}
	
	public void setCanCatch(int v){
		this.canCatch=v;
	}
	
	/**
	 * 
	 */
	public int catchRate  = 0  ;
	
	public int getCatchRate(){
		return this.catchRate;
	}
	
	public void setCatchRate(int v){
		this.catchRate=v;
	}
	
	/**
	 * 
	 */
	public int runRate  = 0  ;
	
	public int getRunRate(){
		return this.runRate;
	}
	
	public void setRunRate(int v){
		this.runRate=v;
	}
	
	/**
	 * 
	 */
	public String aiIds  = null  ;
	
	public String getAiIds(){
		return this.aiIds;
	}
	
	public void setAiIds(String v){
		this.aiIds=v;
	}
	
	/**
	 * 
	 */
	public String immunebuffid  = null  ;
	
	public String getImmunebuffid(){
		return this.immunebuffid;
	}
	
	public void setImmunebuffid(String v){
		this.immunebuffid=v;
	}
	
	
};