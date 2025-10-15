package fire.pb.battle;


public class SCondition implements mytools.ConvMain.Checkable ,Comparable<SCondition>{

	public int compareTo(SCondition o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCondition(){
		super();
	}
	public SCondition(SCondition arg){
		this.id=arg.id ;
		this.battleround=arg.battleround ;
		this.conditionscript=arg.conditionscript ;
		this.bounusnum=arg.bounusnum ;
		this.fighterround=arg.fighterround ;
		this.odds=arg.odds ;
		this.targetfilters=arg.targetfilters ;
		this.skillids=arg.skillids ;
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
	public String battleround  = null  ;
	
	public String getBattleround(){
		return this.battleround;
	}
	
	public void setBattleround(String v){
		this.battleround=v;
	}
	
	/**
	 * 
	 */
	public String conditionscript  = null  ;
	
	public String getConditionscript(){
		return this.conditionscript;
	}
	
	public void setConditionscript(String v){
		this.conditionscript=v;
	}
	
	/**
	 * 
	 */
	public int bounusnum  = 0  ;
	
	public int getBounusnum(){
		return this.bounusnum;
	}
	
	public void setBounusnum(int v){
		this.bounusnum=v;
	}
	
	/**
	 * 
	 */
	public String fighterround  = null  ;
	
	public String getFighterround(){
		return this.fighterround;
	}
	
	public void setFighterround(String v){
		this.fighterround=v;
	}
	
	/**
	 * 
	 */
	public String odds  = null  ;
	
	public String getOdds(){
		return this.odds;
	}
	
	public void setOdds(String v){
		this.odds=v;
	}
	
	/**
	 * 
	 */
	public String targetfilters  = null  ;
	
	public String getTargetfilters(){
		return this.targetfilters;
	}
	
	public void setTargetfilters(String v){
		this.targetfilters=v;
	}
	
	/**
	 * 
	 */
	public String skillids  = null  ;
	
	public String getSkillids(){
		return this.skillids;
	}
	
	public void setSkillids(String v){
		this.skillids=v;
	}
	
	
};