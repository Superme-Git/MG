package fire.pb.skill;


public class SLifeSkill implements mytools.ConvMain.Checkable ,Comparable<SLifeSkill>{

	public int compareTo(SLifeSkill o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLifeSkill(){
		super();
	}
	public SLifeSkill(SLifeSkill arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.icon=arg.icon ;
		this.skillType=arg.skillType ;
		this.needGuild=arg.needGuild ;
		this.skillLevelMax=arg.skillLevelMax ;
		this.studyLevelRule=arg.studyLevelRule ;
		this.studyCostRule=arg.studyCostRule ;
		this.strengthCostRule=arg.strengthCostRule ;
		this.skillId=arg.skillId ;
		this.isShow=arg.isShow ;
		this.guidetips=arg.guidetips ;
		this.description=arg.description ;
		this.brief=arg.brief ;
		this.effect=arg.effect ;
		this.effectnow=arg.effectnow ;
		this.ParaNum=arg.ParaNum ;
		this.ParamIndexList=arg.ParamIndexList ;
		this.needSkilled=arg.needSkilled ;
		this.guildDscription=arg.guildDscription ;
		this.enhanceitemid=arg.enhanceitemid ;
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
	public int icon  = 0  ;
	
	public int getIcon(){
		return this.icon;
	}
	
	public void setIcon(int v){
		this.icon=v;
	}
	
	/**
	 * 
	 */
	public int skillType  = 0  ;
	
	public int getSkillType(){
		return this.skillType;
	}
	
	public void setSkillType(int v){
		this.skillType=v;
	}
	
	/**
	 * 
	 */
	public int needGuild  = 0  ;
	
	public int getNeedGuild(){
		return this.needGuild;
	}
	
	public void setNeedGuild(int v){
		this.needGuild=v;
	}
	
	/**
	 * 
	 */
	public int skillLevelMax  = 0  ;
	
	public int getSkillLevelMax(){
		return this.skillLevelMax;
	}
	
	public void setSkillLevelMax(int v){
		this.skillLevelMax=v;
	}
	
	/**
	 * 
	 */
	public int studyLevelRule  = 0  ;
	
	public int getStudyLevelRule(){
		return this.studyLevelRule;
	}
	
	public void setStudyLevelRule(int v){
		this.studyLevelRule=v;
	}
	
	/**
	 * 
	 */
	public int studyCostRule  = 0  ;
	
	public int getStudyCostRule(){
		return this.studyCostRule;
	}
	
	public void setStudyCostRule(int v){
		this.studyCostRule=v;
	}
	
	/**
	 * 
	 */
	public int strengthCostRule  = 0  ;
	
	public int getStrengthCostRule(){
		return this.strengthCostRule;
	}
	
	public void setStrengthCostRule(int v){
		this.strengthCostRule=v;
	}
	
	/**
	 * 
	 */
	public int skillId  = 0  ;
	
	public int getSkillId(){
		return this.skillId;
	}
	
	public void setSkillId(int v){
		this.skillId=v;
	}
	
	/**
	 * 
	 */
	public int isShow  = 0  ;
	
	public int getIsShow(){
		return this.isShow;
	}
	
	public void setIsShow(int v){
		this.isShow=v;
	}
	
	/**
	 * 
	 */
	public String guidetips  = null  ;
	
	public String getGuidetips(){
		return this.guidetips;
	}
	
	public void setGuidetips(String v){
		this.guidetips=v;
	}
	
	/**
	 * 
	 */
	public String description  = null  ;
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String v){
		this.description=v;
	}
	
	/**
	 * 
	 */
	public String brief  = null  ;
	
	public String getBrief(){
		return this.brief;
	}
	
	public void setBrief(String v){
		this.brief=v;
	}
	
	/**
	 * 
	 */
	public String effect  = null  ;
	
	public String getEffect(){
		return this.effect;
	}
	
	public void setEffect(String v){
		this.effect=v;
	}
	
	/**
	 * 
	 */
	public String effectnow  = null  ;
	
	public String getEffectnow(){
		return this.effectnow;
	}
	
	public void setEffectnow(String v){
		this.effectnow=v;
	}
	
	/**
	 * 
	 */
	public int ParaNum  = 0  ;
	
	public int getParaNum(){
		return this.ParaNum;
	}
	
	public void setParaNum(int v){
		this.ParaNum=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> ParamIndexList  ;
	
	public java.util.ArrayList<Integer> getParamIndexList(){
		return this.ParamIndexList;
	}
	
	public void setParamIndexList(java.util.ArrayList<Integer> v){
		this.ParamIndexList=v;
	}
	
	/**
	 * 
	 */
	public int needSkilled  = 0  ;
	
	public int getNeedSkilled(){
		return this.needSkilled;
	}
	
	public void setNeedSkilled(int v){
		this.needSkilled=v;
	}
	
	/**
	 * 
	 */
	public String guildDscription  = null  ;
	
	public String getGuildDscription(){
		return this.guildDscription;
	}
	
	public void setGuildDscription(String v){
		this.guildDscription=v;
	}
	
	/**
	 * 
	 */
	public int enhanceitemid  = 0  ;
	
	public int getEnhanceitemid(){
		return this.enhanceitemid;
	}
	
	public void setEnhanceitemid(int v){
		this.enhanceitemid=v;
	}
	
	
};