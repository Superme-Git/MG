package fire.pb.map;


public class SMineArea implements mytools.ConvMain.Checkable ,Comparable<SMineArea>{

	public int compareTo(SMineArea o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SMineArea(){
		super();
	}
	public SMineArea(SMineArea arg){
		this.id=arg.id ;
		this.level=arg.level ;
		this.maxLevel=arg.maxLevel ;
		this.environment=arg.environment ;
		this.babyrate=arg.babyrate ;
		this.bossrate=arg.bossrate ;
		this.specialrate=arg.specialrate ;
		this.commonrate=arg.commonrate ;
		this.specialevents=arg.specialevents ;
		this.pet=arg.pet ;
		this.petrate=arg.petrate ;
		this.leader=arg.leader ;
		this.leaderrate=arg.leaderrate ;
		this.monster=arg.monster ;
		this.monsterrate=arg.monsterrate ;
		this.lootid=arg.lootid ;
		this.shared=arg.shared ;
		this.bossbattleid=arg.bossbattleid ;
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
	 * 暗雷区域等级
	 */
	public int level  = 0  ;
	
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int v){
		this.level=v;
	}
	
	/**
	 * 暗雷区域最高等级
	 */
	public int maxLevel  = 0  ;
	
	public int getMaxLevel(){
		return this.maxLevel;
	}
	
	public void setMaxLevel(int v){
		this.maxLevel=v;
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
	public String babyrate  = null  ;
	
	public String getBabyrate(){
		return this.babyrate;
	}
	
	public void setBabyrate(String v){
		this.babyrate=v;
	}
	
	/**
	 * 
	 */
	public String bossrate  = null  ;
	
	public String getBossrate(){
		return this.bossrate;
	}
	
	public void setBossrate(String v){
		this.bossrate=v;
	}
	
	/**
	 * 
	 */
	public String specialrate  = null  ;
	
	public String getSpecialrate(){
		return this.specialrate;
	}
	
	public void setSpecialrate(String v){
		this.specialrate=v;
	}
	
	/**
	 * 
	 */
	public String commonrate  = null  ;
	
	public String getCommonrate(){
		return this.commonrate;
	}
	
	public void setCommonrate(String v){
		this.commonrate=v;
	}
	
	/**
	 * 
	 */
	public String specialevents  = null  ;
	
	public String getSpecialevents(){
		return this.specialevents;
	}
	
	public void setSpecialevents(String v){
		this.specialevents=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> pet  ;
	
	public java.util.ArrayList<Integer> getPet(){
		return this.pet;
	}
	
	public void setPet(java.util.ArrayList<Integer> v){
		this.pet=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> petrate  ;
	
	public java.util.ArrayList<Integer> getPetrate(){
		return this.petrate;
	}
	
	public void setPetrate(java.util.ArrayList<Integer> v){
		this.petrate=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> leader  ;
	
	public java.util.ArrayList<Integer> getLeader(){
		return this.leader;
	}
	
	public void setLeader(java.util.ArrayList<Integer> v){
		this.leader=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> leaderrate  ;
	
	public java.util.ArrayList<Integer> getLeaderrate(){
		return this.leaderrate;
	}
	
	public void setLeaderrate(java.util.ArrayList<Integer> v){
		this.leaderrate=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> monster  ;
	
	public java.util.ArrayList<Integer> getMonster(){
		return this.monster;
	}
	
	public void setMonster(java.util.ArrayList<Integer> v){
		this.monster=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> monsterrate  ;
	
	public java.util.ArrayList<Integer> getMonsterrate(){
		return this.monsterrate;
	}
	
	public void setMonsterrate(java.util.ArrayList<Integer> v){
		this.monsterrate=v;
	}
	
	/**
	 * 
	 */
	public int lootid  = 0  ;
	
	public int getLootid(){
		return this.lootid;
	}
	
	public void setLootid(int v){
		this.lootid=v;
	}
	
	/**
	 * 
	 */
	public int shared  = 0  ;
	
	public int getShared(){
		return this.shared;
	}
	
	public void setShared(int v){
		this.shared=v;
	}
	
	/**
	 * 
	 */
	public int bossbattleid  = 0  ;
	
	public int getBossbattleid(){
		return this.bossbattleid;
	}
	
	public void setBossbattleid(int v){
		this.bossbattleid=v;
	}
	
	
};