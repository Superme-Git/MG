package fire.pb.skill;


public class SSceneSkillConfig implements mytools.ConvMain.Checkable ,Comparable<SSceneSkillConfig>{

	public int compareTo(SSceneSkillConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSceneSkillConfig(){
		super();
	}
	public SSceneSkillConfig(SSceneSkillConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.type=arg.type ;
		this.targetType=arg.targetType ;
		this.hpConsume=arg.hpConsume ;
		this.mpConsume=arg.mpConsume ;
		this.spConsume=arg.spConsume ;
		this.epConsume=arg.epConsume ;
		this.subskillIDs=arg.subskillIDs ;
		this.subskillstarttimes=arg.subskillstarttimes ;
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
	
	
};