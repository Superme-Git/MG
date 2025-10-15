package fire.pb.battle;


public class SBattleAI implements mytools.ConvMain.Checkable ,Comparable<SBattleAI>{

	public int compareTo(SBattleAI o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBattleAI(){
		super();
	}
	public SBattleAI(SBattleAI arg){
		this.id=arg.id ;
		this.trigger=arg.trigger ;
		this.conditions=arg.conditions ;
		this.actionId=arg.actionId ;
		this.count=arg.count ;
		this.enableAI=arg.enableAI ;
		this.extraAI=arg.extraAI ;
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
	public int trigger  = 0  ;
	
	public int getTrigger(){
		return this.trigger;
	}
	
	public void setTrigger(int v){
		this.trigger=v;
	}
	
	/**
	 * 
	 */
	public String conditions  = null  ;
	
	public String getConditions(){
		return this.conditions;
	}
	
	public void setConditions(String v){
		this.conditions=v;
	}
	
	/**
	 * 
	 */
	public String actionId  = null  ;
	
	public String getActionId(){
		return this.actionId;
	}
	
	public void setActionId(String v){
		this.actionId=v;
	}
	
	/**
	 * 
	 */
	public String count  = null  ;
	
	public String getCount(){
		return this.count;
	}
	
	public void setCount(String v){
		this.count=v;
	}
	
	/**
	 * 
	 */
	public String enableAI  = null  ;
	
	public String getEnableAI(){
		return this.enableAI;
	}
	
	public void setEnableAI(String v){
		this.enableAI=v;
	}
	
	/**
	 * 
	 */
	public String extraAI  = null  ;
	
	public String getExtraAI(){
		return this.extraAI;
	}
	
	public void setExtraAI(String v){
		this.extraAI=v;
	}
	
	
};