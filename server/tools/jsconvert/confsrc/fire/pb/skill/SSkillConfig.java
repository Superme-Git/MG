package fire.pb.skill;


public class SSkillConfig implements mytools.ConvMain.Checkable ,Comparable<SSkillConfig>{

	public int compareTo(SSkillConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSkillConfig(){
		super();
	}
	public SSkillConfig(SSkillConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.isActive=arg.isActive ;
		this.isprecountaim=arg.isprecountaim ;
		this.canDouble=arg.canDouble ;
		this.forbiddendefprt=arg.forbiddendefprt ;
		this.ignorehide=arg.ignorehide ;
		this.addautoskill=arg.addautoskill ;
		this.canAtackBack=arg.canAtackBack ;
		this.inOutBattle=arg.inOutBattle ;
		this.typeinserver=arg.typeinserver ;
		this.type=arg.type ;
		this.targetType=arg.targetType ;
		this.initSpeedBonus=arg.initSpeedBonus ;
		this.hpConsume=arg.hpConsume ;
		this.mpConsume=arg.mpConsume ;
		this.spConsume=arg.spConsume ;
		this.epConsume=arg.epConsume ;
		this.subskillIDs=arg.subskillIDs ;
		this.subskillstarttimes=arg.subskillstarttimes ;
		this.scriptName=arg.scriptName ;
		this.caneffectbyhate=arg.caneffectbyhate ;
		this.skillspecialtime=arg.skillspecialtime ;
		this.isonlypve=arg.isonlypve ;
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
	public boolean isActive  = false  ;
	
	public boolean getIsActive(){
		return this.isActive;
	}
	
	public void setIsActive(boolean v){
		this.isActive=v;
	}
	
	/**
	 * 
	 */
	public boolean isprecountaim  = false  ;
	
	public boolean getIsprecountaim(){
		return this.isprecountaim;
	}
	
	public void setIsprecountaim(boolean v){
		this.isprecountaim=v;
	}
	
	/**
	 * 
	 */
	public boolean canDouble  = false  ;
	
	public boolean getCanDouble(){
		return this.canDouble;
	}
	
	public void setCanDouble(boolean v){
		this.canDouble=v;
	}
	
	/**
	 * 
	 */
	public int forbiddendefprt  = 0  ;
	
	public int getForbiddendefprt(){
		return this.forbiddendefprt;
	}
	
	public void setForbiddendefprt(int v){
		this.forbiddendefprt=v;
	}
	
	/**
	 * 
	 */
	public int ignorehide  = 0  ;
	
	public int getIgnorehide(){
		return this.ignorehide;
	}
	
	public void setIgnorehide(int v){
		this.ignorehide=v;
	}
	
	/**
	 * 
	 */
	public int addautoskill  = 0  ;
	
	public int getAddautoskill(){
		return this.addautoskill;
	}
	
	public void setAddautoskill(int v){
		this.addautoskill=v;
	}
	
	/**
	 * 
	 */
	public boolean canAtackBack  = false  ;
	
	public boolean getCanAtackBack(){
		return this.canAtackBack;
	}
	
	public void setCanAtackBack(boolean v){
		this.canAtackBack=v;
	}
	
	/**
	 * 
	 */
	public boolean inOutBattle  = false  ;
	
	public boolean getInOutBattle(){
		return this.inOutBattle;
	}
	
	public void setInOutBattle(boolean v){
		this.inOutBattle=v;
	}
	
	/**
	 * 
	 */
	public int typeinserver  = 0  ;
	
	public int getTypeinserver(){
		return this.typeinserver;
	}
	
	public void setTypeinserver(int v){
		this.typeinserver=v;
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
	public int targetType  = 0  ;
	
	public int getTargetType(){
		return this.targetType;
	}
	
	public void setTargetType(int v){
		this.targetType=v;
	}
	
	/**
	 * 
	 */
	public String initSpeedBonus  = null  ;
	
	public String getInitSpeedBonus(){
		return this.initSpeedBonus;
	}
	
	public void setInitSpeedBonus(String v){
		this.initSpeedBonus=v;
	}
	
	/**
	 * 
	 */
	public String hpConsume  = null  ;
	
	public String getHpConsume(){
		return this.hpConsume;
	}
	
	public void setHpConsume(String v){
		this.hpConsume=v;
	}
	
	/**
	 * 
	 */
	public String mpConsume  = null  ;
	
	public String getMpConsume(){
		return this.mpConsume;
	}
	
	public void setMpConsume(String v){
		this.mpConsume=v;
	}
	
	/**
	 * 
	 */
	public String spConsume  = null  ;
	
	public String getSpConsume(){
		return this.spConsume;
	}
	
	public void setSpConsume(String v){
		this.spConsume=v;
	}
	
	/**
	 * 
	 */
	public String epConsume  = null  ;
	
	public String getEpConsume(){
		return this.epConsume;
	}
	
	public void setEpConsume(String v){
		this.epConsume=v;
	}
	
	/**
	 * 
	 */
	public String subskillIDs  = null  ;
	
	public String getSubskillIDs(){
		return this.subskillIDs;
	}
	
	public void setSubskillIDs(String v){
		this.subskillIDs=v;
	}
	
	/**
	 * 
	 */
	public String subskillstarttimes  = null  ;
	
	public String getSubskillstarttimes(){
		return this.subskillstarttimes;
	}
	
	public void setSubskillstarttimes(String v){
		this.subskillstarttimes=v;
	}
	
	/**
	 * 
	 */
	public String scriptName  = null  ;
	
	public String getScriptName(){
		return this.scriptName;
	}
	
	public void setScriptName(String v){
		this.scriptName=v;
	}
	
	/**
	 * 
	 */
	public int caneffectbyhate  = 0  ;
	
	public int getCaneffectbyhate(){
		return this.caneffectbyhate;
	}
	
	public void setCaneffectbyhate(int v){
		this.caneffectbyhate=v;
	}
	
	/**
	 * 
	 */
	public int skillspecialtime  = 0  ;
	
	public int getSkillspecialtime(){
		return this.skillspecialtime;
	}
	
	public void setSkillspecialtime(int v){
		this.skillspecialtime=v;
	}
	
	/**
	 * 
	 */
	public int isonlypve  = 0  ;
	
	public int getIsonlypve(){
		return this.isonlypve;
	}
	
	public void setIsonlypve(int v){
		this.isonlypve=v;
	}
	
	
};