package fire.pb.buff;


public class SCBuffConfig implements mytools.ConvMain.Checkable ,Comparable<SCBuffConfig>{

	public int compareTo(SCBuffConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCBuffConfig(){
		super();
	}
	public SCBuffConfig(SCBuffConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.classname=arg.classname ;
		this.limitOperations=arg.limitOperations ;
		this.initTime=arg.initTime ;
		this.initCount=arg.initCount ;
		this.initAmount=arg.initAmount ;
		this.clearType=arg.clearType ;
		this.storeToDisk=arg.storeToDisk ;
		this.time=arg.time ;
		this.buffclass=arg.buffclass ;
		this.Anticlass=arg.Anticlass ;
		this.sendtoclient=arg.sendtoclient ;
		this.showScale=arg.showScale ;
		this.inBattleScript=arg.inBattleScript ;
		this.effects=arg.effects ;
		this.overrideSelfType=arg.overrideSelfType ;
		this.overrideAttr=arg.overrideAttr ;
		this.instantbuff=arg.instantbuff ;
		this.instantbuffeffect=arg.instantbuffeffect ;
		this.scenestateId=arg.scenestateId ;
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
	public String classname  = null  ;
	
	public String getClassname(){
		return this.classname;
	}
	
	public void setClassname(String v){
		this.classname=v;
	}
	
	/**
	 * 
	 */
	public String limitOperations  = null  ;
	
	public String getLimitOperations(){
		return this.limitOperations;
	}
	
	public void setLimitOperations(String v){
		this.limitOperations=v;
	}
	
	/**
	 * 
	 */
	public long initTime  = 0L  ;
	
	public long getInitTime(){
		return this.initTime;
	}
	
	public void setInitTime(long v){
		this.initTime=v;
	}
	
	/**
	 * 
	 */
	public int initCount  = 0  ;
	
	public int getInitCount(){
		return this.initCount;
	}
	
	public void setInitCount(int v){
		this.initCount=v;
	}
	
	/**
	 * 
	 */
	public long initAmount  = 0L  ;
	
	public long getInitAmount(){
		return this.initAmount;
	}
	
	public void setInitAmount(long v){
		this.initAmount=v;
	}
	
	/**
	 * 
	 */
	public int clearType  = 0  ;
	
	public int getClearType(){
		return this.clearType;
	}
	
	public void setClearType(int v){
		this.clearType=v;
	}
	
	/**
	 * 
	 */
	public boolean storeToDisk  = false  ;
	
	public boolean getStoreToDisk(){
		return this.storeToDisk;
	}
	
	public void setStoreToDisk(boolean v){
		this.storeToDisk=v;
	}
	
	/**
	 * 
	 */
	public long time  = 0L  ;
	
	public long getTime(){
		return this.time;
	}
	
	public void setTime(long v){
		this.time=v;
	}
	
	/**
	 * 
	 */
	public int buffclass  = 0  ;
	
	public int getBuffclass(){
		return this.buffclass;
	}
	
	public void setBuffclass(int v){
		this.buffclass=v;
	}
	
	/**
	 * 
	 */
	public String Anticlass  = null  ;
	
	public String getAnticlass(){
		return this.Anticlass;
	}
	
	public void setAnticlass(String v){
		this.Anticlass=v;
	}
	
	/**
	 * 
	 */
	public int sendtoclient  = 0  ;
	
	public int getSendtoclient(){
		return this.sendtoclient;
	}
	
	public void setSendtoclient(int v){
		this.sendtoclient=v;
	}
	
	/**
	 * 
	 */
	public int showScale  = 0  ;
	
	public int getShowScale(){
		return this.showScale;
	}
	
	public void setShowScale(int v){
		this.showScale=v;
	}
	
	/**
	 * 
	 */
	public boolean inBattleScript  = false  ;
	
	public boolean getInBattleScript(){
		return this.inBattleScript;
	}
	
	public void setInBattleScript(boolean v){
		this.inBattleScript=v;
	}
	
	/**
	 * 
	 */
	public String effects  = null  ;
	
	public String getEffects(){
		return this.effects;
	}
	
	public void setEffects(String v){
		this.effects=v;
	}
	
	/**
	 * 
	 */
	public int overrideSelfType  = 0  ;
	
	public int getOverrideSelfType(){
		return this.overrideSelfType;
	}
	
	public void setOverrideSelfType(int v){
		this.overrideSelfType=v;
	}
	
	/**
	 * 
	 */
	public String overrideAttr  = null  ;
	
	public String getOverrideAttr(){
		return this.overrideAttr;
	}
	
	public void setOverrideAttr(String v){
		this.overrideAttr=v;
	}
	
	/**
	 * 
	 */
	public int instantbuff  = 0  ;
	
	public int getInstantbuff(){
		return this.instantbuff;
	}
	
	public void setInstantbuff(int v){
		this.instantbuff=v;
	}
	
	/**
	 * 
	 */
	public String instantbuffeffect  = null  ;
	
	public String getInstantbuffeffect(){
		return this.instantbuffeffect;
	}
	
	public void setInstantbuffeffect(String v){
		this.instantbuffeffect=v;
	}
	
	/**
	 * 
	 */
	public String scenestateId  = null  ;
	
	public String getScenestateId(){
		return this.scenestateId;
	}
	
	public void setScenestateId(String v){
		this.scenestateId=v;
	}
	
	
};