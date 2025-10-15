package fire.pb.gm;


public class SGMStandard implements mytools.ConvMain.Checkable ,Comparable<SGMStandard>{

	public int compareTo(SGMStandard o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SGMStandard(){
		super();
	}
	public SGMStandard(SGMStandard arg){
		this.id=arg.id ;
		this.schoolname=arg.schoolname ;
		this.about=arg.about ;
		this.rolelv=arg.rolelv ;
		this.equipinfo=arg.equipinfo ;
		this.skillinfo=arg.skillinfo ;
		this.xiulianinfo=arg.xiulianinfo ;
		this.guildskillinfo=arg.guildskillinfo ;
		this.geminfo=arg.geminfo ;
		this.petinfo=arg.petinfo ;
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
	 * 职业
	 */
	public String schoolname  = null  ;
	
	public String getSchoolname(){
		return this.schoolname;
	}
	
	public void setSchoolname(String v){
		this.schoolname=v;
	}
	
	/**
	 * 策划备注
	 */
	public String about  = null  ;
	
	public String getAbout(){
		return this.about;
	}
	
	public void setAbout(String v){
		this.about=v;
	}
	
	/**
	 * 等级
	 */
	public int rolelv  = 0  ;
	
	public int getRolelv(){
		return this.rolelv;
	}
	
	public void setRolelv(int v){
		this.rolelv=v;
	}
	
	/**
	 * 装备
	 */
	public String equipinfo  = null  ;
	
	public String getEquipinfo(){
		return this.equipinfo;
	}
	
	public void setEquipinfo(String v){
		this.equipinfo=v;
	}
	
	/**
	 * 技能等级
	 */
	public String skillinfo  = null  ;
	
	public String getSkillinfo(){
		return this.skillinfo;
	}
	
	public void setSkillinfo(String v){
		this.skillinfo=v;
	}
	
	/**
	 * 修炼等级
	 */
	public String xiulianinfo  = null  ;
	
	public String getXiulianinfo(){
		return this.xiulianinfo;
	}
	
	public void setXiulianinfo(String v){
		this.xiulianinfo=v;
	}
	
	/**
	 * 公会技能
	 */
	public String guildskillinfo  = null  ;
	
	public String getGuildskillinfo(){
		return this.guildskillinfo;
	}
	
	public void setGuildskillinfo(String v){
		this.guildskillinfo=v;
	}
	
	/**
	 * 宝石等级
	 */
	public String geminfo  = null  ;
	
	public String getGeminfo(){
		return this.geminfo;
	}
	
	public void setGeminfo(String v){
		this.geminfo=v;
	}
	
	/**
	 * 宝宝等级
	 */
	public String petinfo  = null  ;
	
	public String getPetinfo(){
		return this.petinfo;
	}
	
	public void setPetinfo(String v){
		this.petinfo=v;
	}
	
	
};