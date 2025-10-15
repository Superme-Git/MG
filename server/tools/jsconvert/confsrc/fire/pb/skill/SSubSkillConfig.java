package fire.pb.skill;


public class SSubSkillConfig implements mytools.ConvMain.Checkable ,Comparable<SSubSkillConfig>{

	public int compareTo(SSubSkillConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSubSkillConfig(){
		super();
	}
	public SSubSkillConfig(SSubSkillConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.confilictStates=arg.confilictStates ;
		this.confilictStatesnotify=arg.confilictStatesnotify ;
		this.requiredBuffs=arg.requiredBuffs ;
		this.confilictBuffs=arg.confilictBuffs ;
		this.relevanceSkillID=arg.relevanceSkillID ;
		this.actionCondition=arg.actionCondition ;
		this.aimCampType=arg.aimCampType ;
		this.aimRaceType=arg.aimRaceType ;
		this.aimRelationType=arg.aimRelationType ;
		this.jobid=arg.jobid ;
		this.monsterid=arg.monsterid ;
		this.targetType=arg.targetType ;
		this.targetNum=arg.targetNum ;
		this.sortArg=arg.sortArg ;
		this.targetRate=arg.targetRate ;
		this.minmagicdamage=arg.minmagicdamage ;
		this.mainbuffId=arg.mainbuffId ;
		this.buff0_id=arg.buff0_id ;
		this.buff0_rate=arg.buff0_rate ;
		this.buff0_round=arg.buff0_round ;
		this.buff0_values=arg.buff0_values ;
		this.buff0_confilictStates=arg.buff0_confilictStates ;
		this.buff0_requiredBuffs=arg.buff0_requiredBuffs ;
		this.buff0_confilictBuffs=arg.buff0_confilictBuffs ;
		this.buff0_odds=arg.buff0_odds ;
		this.buff1_id=arg.buff1_id ;
		this.buff1_rate=arg.buff1_rate ;
		this.buff1_round=arg.buff1_round ;
		this.buff1_values=arg.buff1_values ;
		this.buff1_confilictStates=arg.buff1_confilictStates ;
		this.buff1_requiredBuffs=arg.buff1_requiredBuffs ;
		this.buff1_confilictBuffs=arg.buff1_confilictBuffs ;
		this.buff1_odds=arg.buff1_odds ;
		this.buff2_id=arg.buff2_id ;
		this.buff2_rate=arg.buff2_rate ;
		this.buff2_round=arg.buff2_round ;
		this.buff2_values=arg.buff2_values ;
		this.buff2_confilictStates=arg.buff2_confilictStates ;
		this.buff2_requiredBuffs=arg.buff2_requiredBuffs ;
		this.buff2_confilictBuffs=arg.buff2_confilictBuffs ;
		this.buff2_odds=arg.buff2_odds ;
		this.buff3_id=arg.buff3_id ;
		this.buff3_rate=arg.buff3_rate ;
		this.buff3_round=arg.buff3_round ;
		this.buff3_values=arg.buff3_values ;
		this.buff3_confilictStates=arg.buff3_confilictStates ;
		this.buff3_requiredBuffs=arg.buff3_requiredBuffs ;
		this.buff3_confilictBuffs=arg.buff3_confilictBuffs ;
		this.buff3_odds=arg.buff3_odds ;
		this.scriptName=arg.scriptName ;
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
	public String confilictStates  = null  ;
	
	public String getConfilictStates(){
		return this.confilictStates;
	}
	
	public void setConfilictStates(String v){
		this.confilictStates=v;
	}
	
	/**
	 * 
	 */
	public int confilictStatesnotify  = 0  ;
	
	public int getConfilictStatesnotify(){
		return this.confilictStatesnotify;
	}
	
	public void setConfilictStatesnotify(int v){
		this.confilictStatesnotify=v;
	}
	
	/**
	 * 
	 */
	public String requiredBuffs  = null  ;
	
	public String getRequiredBuffs(){
		return this.requiredBuffs;
	}
	
	public void setRequiredBuffs(String v){
		this.requiredBuffs=v;
	}
	
	/**
	 * 
	 */
	public String confilictBuffs  = null  ;
	
	public String getConfilictBuffs(){
		return this.confilictBuffs;
	}
	
	public void setConfilictBuffs(String v){
		this.confilictBuffs=v;
	}
	
	/**
	 * 
	 */
	public int relevanceSkillID  = 0  ;
	
	public int getRelevanceSkillID(){
		return this.relevanceSkillID;
	}
	
	public void setRelevanceSkillID(int v){
		this.relevanceSkillID=v;
	}
	
	/**
	 * 
	 */
	public String actionCondition  = null  ;
	
	public String getActionCondition(){
		return this.actionCondition;
	}
	
	public void setActionCondition(String v){
		this.actionCondition=v;
	}
	
	/**
	 * 
	 */
	public int aimCampType  = 0  ;
	
	public int getAimCampType(){
		return this.aimCampType;
	}
	
	public void setAimCampType(int v){
		this.aimCampType=v;
	}
	
	/**
	 * 
	 */
	public String aimRaceType  = null  ;
	
	public String getAimRaceType(){
		return this.aimRaceType;
	}
	
	public void setAimRaceType(String v){
		this.aimRaceType=v;
	}
	
	/**
	 * 
	 */
	public String aimRelationType  = null  ;
	
	public String getAimRelationType(){
		return this.aimRelationType;
	}
	
	public void setAimRelationType(String v){
		this.aimRelationType=v;
	}
	
	/**
	 * 
	 */
	public String jobid  = null  ;
	
	public String getJobid(){
		return this.jobid;
	}
	
	public void setJobid(String v){
		this.jobid=v;
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
	public String targetType  = null  ;
	
	public String getTargetType(){
		return this.targetType;
	}
	
	public void setTargetType(String v){
		this.targetType=v;
	}
	
	/**
	 * 
	 */
	public String targetNum  = null  ;
	
	public String getTargetNum(){
		return this.targetNum;
	}
	
	public void setTargetNum(String v){
		this.targetNum=v;
	}
	
	/**
	 * 
	 */
	public String sortArg  = null  ;
	
	public String getSortArg(){
		return this.sortArg;
	}
	
	public void setSortArg(String v){
		this.sortArg=v;
	}
	
	/**
	 * 
	 */
	public String targetRate  = null  ;
	
	public String getTargetRate(){
		return this.targetRate;
	}
	
	public void setTargetRate(String v){
		this.targetRate=v;
	}
	
	/**
	 * 
	 */
	public String minmagicdamage  = null  ;
	
	public String getMinmagicdamage(){
		return this.minmagicdamage;
	}
	
	public void setMinmagicdamage(String v){
		this.minmagicdamage=v;
	}
	
	/**
	 * 
	 */
	public int mainbuffId  = 0  ;
	
	public int getMainbuffId(){
		return this.mainbuffId;
	}
	
	public void setMainbuffId(int v){
		this.mainbuffId=v;
	}
	
	/**
	 * 
	 */
	public String buff0_id  = null  ;
	
	public String getBuff0_id(){
		return this.buff0_id;
	}
	
	public void setBuff0_id(String v){
		this.buff0_id=v;
	}
	
	/**
	 * 
	 */
	public String buff0_rate  = null  ;
	
	public String getBuff0_rate(){
		return this.buff0_rate;
	}
	
	public void setBuff0_rate(String v){
		this.buff0_rate=v;
	}
	
	/**
	 * 
	 */
	public String buff0_round  = null  ;
	
	public String getBuff0_round(){
		return this.buff0_round;
	}
	
	public void setBuff0_round(String v){
		this.buff0_round=v;
	}
	
	/**
	 * 
	 */
	public String buff0_values  = null  ;
	
	public String getBuff0_values(){
		return this.buff0_values;
	}
	
	public void setBuff0_values(String v){
		this.buff0_values=v;
	}
	
	/**
	 * 
	 */
	public String buff0_confilictStates  = null  ;
	
	public String getBuff0_confilictStates(){
		return this.buff0_confilictStates;
	}
	
	public void setBuff0_confilictStates(String v){
		this.buff0_confilictStates=v;
	}
	
	/**
	 * 
	 */
	public String buff0_requiredBuffs  = null  ;
	
	public String getBuff0_requiredBuffs(){
		return this.buff0_requiredBuffs;
	}
	
	public void setBuff0_requiredBuffs(String v){
		this.buff0_requiredBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff0_confilictBuffs  = null  ;
	
	public String getBuff0_confilictBuffs(){
		return this.buff0_confilictBuffs;
	}
	
	public void setBuff0_confilictBuffs(String v){
		this.buff0_confilictBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff0_odds  = null  ;
	
	public String getBuff0_odds(){
		return this.buff0_odds;
	}
	
	public void setBuff0_odds(String v){
		this.buff0_odds=v;
	}
	
	/**
	 * 
	 */
	public String buff1_id  = null  ;
	
	public String getBuff1_id(){
		return this.buff1_id;
	}
	
	public void setBuff1_id(String v){
		this.buff1_id=v;
	}
	
	/**
	 * 
	 */
	public String buff1_rate  = null  ;
	
	public String getBuff1_rate(){
		return this.buff1_rate;
	}
	
	public void setBuff1_rate(String v){
		this.buff1_rate=v;
	}
	
	/**
	 * 
	 */
	public String buff1_round  = null  ;
	
	public String getBuff1_round(){
		return this.buff1_round;
	}
	
	public void setBuff1_round(String v){
		this.buff1_round=v;
	}
	
	/**
	 * 
	 */
	public String buff1_values  = null  ;
	
	public String getBuff1_values(){
		return this.buff1_values;
	}
	
	public void setBuff1_values(String v){
		this.buff1_values=v;
	}
	
	/**
	 * 
	 */
	public String buff1_confilictStates  = null  ;
	
	public String getBuff1_confilictStates(){
		return this.buff1_confilictStates;
	}
	
	public void setBuff1_confilictStates(String v){
		this.buff1_confilictStates=v;
	}
	
	/**
	 * 
	 */
	public String buff1_requiredBuffs  = null  ;
	
	public String getBuff1_requiredBuffs(){
		return this.buff1_requiredBuffs;
	}
	
	public void setBuff1_requiredBuffs(String v){
		this.buff1_requiredBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff1_confilictBuffs  = null  ;
	
	public String getBuff1_confilictBuffs(){
		return this.buff1_confilictBuffs;
	}
	
	public void setBuff1_confilictBuffs(String v){
		this.buff1_confilictBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff1_odds  = null  ;
	
	public String getBuff1_odds(){
		return this.buff1_odds;
	}
	
	public void setBuff1_odds(String v){
		this.buff1_odds=v;
	}
	
	/**
	 * 
	 */
	public String buff2_id  = null  ;
	
	public String getBuff2_id(){
		return this.buff2_id;
	}
	
	public void setBuff2_id(String v){
		this.buff2_id=v;
	}
	
	/**
	 * 
	 */
	public String buff2_rate  = null  ;
	
	public String getBuff2_rate(){
		return this.buff2_rate;
	}
	
	public void setBuff2_rate(String v){
		this.buff2_rate=v;
	}
	
	/**
	 * 
	 */
	public String buff2_round  = null  ;
	
	public String getBuff2_round(){
		return this.buff2_round;
	}
	
	public void setBuff2_round(String v){
		this.buff2_round=v;
	}
	
	/**
	 * 
	 */
	public String buff2_values  = null  ;
	
	public String getBuff2_values(){
		return this.buff2_values;
	}
	
	public void setBuff2_values(String v){
		this.buff2_values=v;
	}
	
	/**
	 * 
	 */
	public String buff2_confilictStates  = null  ;
	
	public String getBuff2_confilictStates(){
		return this.buff2_confilictStates;
	}
	
	public void setBuff2_confilictStates(String v){
		this.buff2_confilictStates=v;
	}
	
	/**
	 * 
	 */
	public String buff2_requiredBuffs  = null  ;
	
	public String getBuff2_requiredBuffs(){
		return this.buff2_requiredBuffs;
	}
	
	public void setBuff2_requiredBuffs(String v){
		this.buff2_requiredBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff2_confilictBuffs  = null  ;
	
	public String getBuff2_confilictBuffs(){
		return this.buff2_confilictBuffs;
	}
	
	public void setBuff2_confilictBuffs(String v){
		this.buff2_confilictBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff2_odds  = null  ;
	
	public String getBuff2_odds(){
		return this.buff2_odds;
	}
	
	public void setBuff2_odds(String v){
		this.buff2_odds=v;
	}
	
	/**
	 * 
	 */
	public String buff3_id  = null  ;
	
	public String getBuff3_id(){
		return this.buff3_id;
	}
	
	public void setBuff3_id(String v){
		this.buff3_id=v;
	}
	
	/**
	 * 
	 */
	public String buff3_rate  = null  ;
	
	public String getBuff3_rate(){
		return this.buff3_rate;
	}
	
	public void setBuff3_rate(String v){
		this.buff3_rate=v;
	}
	
	/**
	 * 
	 */
	public String buff3_round  = null  ;
	
	public String getBuff3_round(){
		return this.buff3_round;
	}
	
	public void setBuff3_round(String v){
		this.buff3_round=v;
	}
	
	/**
	 * 
	 */
	public String buff3_values  = null  ;
	
	public String getBuff3_values(){
		return this.buff3_values;
	}
	
	public void setBuff3_values(String v){
		this.buff3_values=v;
	}
	
	/**
	 * 
	 */
	public String buff3_confilictStates  = null  ;
	
	public String getBuff3_confilictStates(){
		return this.buff3_confilictStates;
	}
	
	public void setBuff3_confilictStates(String v){
		this.buff3_confilictStates=v;
	}
	
	/**
	 * 
	 */
	public String buff3_requiredBuffs  = null  ;
	
	public String getBuff3_requiredBuffs(){
		return this.buff3_requiredBuffs;
	}
	
	public void setBuff3_requiredBuffs(String v){
		this.buff3_requiredBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff3_confilictBuffs  = null  ;
	
	public String getBuff3_confilictBuffs(){
		return this.buff3_confilictBuffs;
	}
	
	public void setBuff3_confilictBuffs(String v){
		this.buff3_confilictBuffs=v;
	}
	
	/**
	 * 
	 */
	public String buff3_odds  = null  ;
	
	public String getBuff3_odds(){
		return this.buff3_odds;
	}
	
	public void setBuff3_odds(String v){
		this.buff3_odds=v;
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
	
	
};