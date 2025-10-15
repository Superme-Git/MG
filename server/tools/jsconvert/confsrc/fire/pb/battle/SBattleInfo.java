package fire.pb.battle;


public class SBattleInfo implements mytools.ConvMain.Checkable ,Comparable<SBattleInfo>{

	public int compareTo(SBattleInfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBattleInfo(){
		super();
	}
	public SBattleInfo(SBattleInfo arg){
		this.id=arg.id ;
		this.fightAItype=arg.fightAItype ;
		this.scene=arg.scene ;
		this.bgm=arg.bgm ;
		this.cameratype=arg.cameratype ;
		this.battlelevel=arg.battlelevel ;
		this.environment=arg.environment ;
		this.battleAI=arg.battleAI ;
		this.award=arg.award ;
		this.victorycondition=arg.victorycondition ;
		this.isNotDecEndure=arg.isNotDecEndure ;
		this.xiezhan=arg.xiezhan ;
		this.deathinfo=arg.deathinfo ;
		this.monsterNumScript=arg.monsterNumScript ;
		this.positions=arg.positions ;
		this.randompos=arg.randompos ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
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
	public int fightAItype  = 0  ;
	
	public int getFightAItype(){
		return this.fightAItype;
	}
	
	public void setFightAItype(int v){
		this.fightAItype=v;
	}
	
	/**
	 * 
	 */
	public int scene  = 0  ;
	
	public int getScene(){
		return this.scene;
	}
	
	public void setScene(int v){
		this.scene=v;
	}
	
	/**
	 * 
	 */
	public int bgm  = 0  ;
	
	public int getBgm(){
		return this.bgm;
	}
	
	public void setBgm(int v){
		this.bgm=v;
	}
	
	/**
	 * 
	 */
	public int cameratype  = 0  ;
	
	public int getCameratype(){
		return this.cameratype;
	}
	
	public void setCameratype(int v){
		this.cameratype=v;
	}
	
	/**
	 * 
	 */
	public String battlelevel  = null  ;
	
	public String getBattlelevel(){
		return this.battlelevel;
	}
	
	public void setBattlelevel(String v){
		this.battlelevel=v;
	}
	
	/**
	 * 
	 */
	public int environment  = 0  ;
	
	public int getEnvironment(){
		return this.environment;
	}
	
	public void setEnvironment(int v){
		this.environment=v;
	}
	
	/**
	 * 
	 */
	public String battleAI  = null  ;
	
	public String getBattleAI(){
		return this.battleAI;
	}
	
	public void setBattleAI(String v){
		this.battleAI=v;
	}
	
	/**
	 * 
	 */
	public int award  = 0  ;
	
	public int getAward(){
		return this.award;
	}
	
	public void setAward(int v){
		this.award=v;
	}
	
	/**
	 * 
	 */
	public int victorycondition  = 0  ;
	
	public int getVictorycondition(){
		return this.victorycondition;
	}
	
	public void setVictorycondition(int v){
		this.victorycondition=v;
	}
	
	/**
	 * 
	 */
	public int isNotDecEndure  = 0  ;
	
	public int getIsNotDecEndure(){
		return this.isNotDecEndure;
	}
	
	public void setIsNotDecEndure(int v){
		this.isNotDecEndure=v;
	}
	
	/**
	 * 
	 */
	public int xiezhan  = 0  ;
	
	public int getXiezhan(){
		return this.xiezhan;
	}
	
	public void setXiezhan(int v){
		this.xiezhan=v;
	}
	
	/**
	 * 
	 */
	public int deathinfo  = 0  ;
	
	public int getDeathinfo(){
		return this.deathinfo;
	}
	
	public void setDeathinfo(int v){
		this.deathinfo=v;
	}
	
	/**
	 * 
	 */
	public String monsterNumScript  = null  ;
	
	public String getMonsterNumScript(){
		return this.monsterNumScript;
	}
	
	public void setMonsterNumScript(String v){
		this.monsterNumScript=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> positions  ;
	
	public java.util.ArrayList<String> getPositions(){
		return this.positions;
	}
	
	public void setPositions(java.util.ArrayList<String> v){
		this.positions=v;
	}
	
	/**
	 * 
	 */
	public String randompos  = null  ;
	
	public String getRandompos(){
		return this.randompos;
	}
	
	public void setRandompos(String v){
		this.randompos=v;
	}
	
	
};