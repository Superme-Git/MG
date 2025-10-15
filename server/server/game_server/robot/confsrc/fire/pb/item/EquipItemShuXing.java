package fire.pb.item;


public class EquipItemShuXing  extends ItemShuXing {

	public int compareTo(EquipItemShuXing o){
		return this.id-o.id;
	}

	
	public EquipItemShuXing(ItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public EquipItemShuXing(){
		super();
	}
	public EquipItemShuXing(EquipItemShuXing arg){
		super(arg);
		this.maxnaijiu=arg.maxnaijiu ;
		this.roleNeed=arg.roleNeed ;
		this.ptxlfailrate=arg.ptxlfailrate ;
		this.ptxlcailiaoid=arg.ptxlcailiaoid ;
		this.ptxlcailiaonum=arg.ptxlcailiaonum ;
		this.commonidlist=arg.commonidlist ;
		this.commonnumlist=arg.commonnumlist ;
		this.tsxlcailiaoid=arg.tsxlcailiaoid ;
		this.tsxlcailiaonum=arg.tsxlcailiaonum ;
		this.ptxlmoneynum=arg.ptxlmoneynum ;
		this.ptxlmoneytype=arg.ptxlmoneytype ;
		this.tsxlmoneynum=arg.tsxlmoneynum ;
		this.tsxlmoneytype=arg.tsxlmoneytype ;
		this.canGems=arg.canGems ;
		this.gems=arg.gems ;
		this.needSex=arg.needSex ;
		this.needSchool=arg.needSchool ;
		this.equipcolor=arg.equipcolor ;
		this.suiting=arg.suiting ;
		this.skillid=arg.skillid ;
		this.effectid=arg.effectid ;
		this.specialAttr=arg.specialAttr ;
		this.baseAttrId=arg.baseAttrId ;
		this.addAttrRate=arg.addAttrRate ;
		this.addAttrInfo=arg.addAttrInfo ;
		this.randomAttrId=arg.randomAttrId ;
		this.randomSkillId=arg.randomSkillId ;
		this.randomEffectId=arg.randomEffectId ;
		this.是否自动分解=arg.是否自动分解 ;
		this.分解获得银币=arg.分解获得银币 ;
		this.分解额外获得物品1=arg.分解额外获得物品1 ;
		this.分解额外获得物品2=arg.分解额外获得物品2 ;
		this.分解额外获得物品3=arg.分解额外获得物品3 ;
		this.分解额外获得物品4=arg.分解额外获得物品4 ;
		this.分解额外获得物品5=arg.分解额外获得物品5 ;
		this.treasureScore=arg.treasureScore ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int maxnaijiu  = 0  ;
	
	public int getMaxnaijiu(){
		return this.maxnaijiu;
	}
	
	public void setMaxnaijiu(int v){
		this.maxnaijiu=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> roleNeed  ;
	
	public java.util.ArrayList<Integer> getRoleNeed(){
		return this.roleNeed;
	}
	
	public void setRoleNeed(java.util.ArrayList<Integer> v){
		this.roleNeed=v;
	}
	
	/**
	 * 
	 */
	public int ptxlfailrate  = 0  ;
	
	public int getPtxlfailrate(){
		return this.ptxlfailrate;
	}
	
	public void setPtxlfailrate(int v){
		this.ptxlfailrate=v;
	}
	
	/**
	 * 
	 */
	public int ptxlcailiaoid  = 0  ;
	
	public int getPtxlcailiaoid(){
		return this.ptxlcailiaoid;
	}
	
	public void setPtxlcailiaoid(int v){
		this.ptxlcailiaoid=v;
	}
	
	/**
	 * 
	 */
	public int ptxlcailiaonum  = 0  ;
	
	public int getPtxlcailiaonum(){
		return this.ptxlcailiaonum;
	}
	
	public void setPtxlcailiaonum(int v){
		this.ptxlcailiaonum=v;
	}
	
	/**
	 * 普通修理材料id列表
	 */
	public java.util.ArrayList<Integer> commonidlist  ;
	
	public java.util.ArrayList<Integer> getCommonidlist(){
		return this.commonidlist;
	}
	
	public void setCommonidlist(java.util.ArrayList<Integer> v){
		this.commonidlist=v;
	}
	
	/**
	 * 普通修理材料数量列表
	 */
	public java.util.ArrayList<Integer> commonnumlist  ;
	
	public java.util.ArrayList<Integer> getCommonnumlist(){
		return this.commonnumlist;
	}
	
	public void setCommonnumlist(java.util.ArrayList<Integer> v){
		this.commonnumlist=v;
	}
	
	/**
	 * 
	 */
	public int tsxlcailiaoid  = 0  ;
	
	public int getTsxlcailiaoid(){
		return this.tsxlcailiaoid;
	}
	
	public void setTsxlcailiaoid(int v){
		this.tsxlcailiaoid=v;
	}
	
	/**
	 * 
	 */
	public int tsxlcailiaonum  = 0  ;
	
	public int getTsxlcailiaonum(){
		return this.tsxlcailiaonum;
	}
	
	public void setTsxlcailiaonum(int v){
		this.tsxlcailiaonum=v;
	}
	
	/**
	 * 
	 */
	public int ptxlmoneynum  = 0  ;
	
	public int getPtxlmoneynum(){
		return this.ptxlmoneynum;
	}
	
	public void setPtxlmoneynum(int v){
		this.ptxlmoneynum=v;
	}
	
	/**
	 * 
	 */
	public int ptxlmoneytype  = 0  ;
	
	public int getPtxlmoneytype(){
		return this.ptxlmoneytype;
	}
	
	public void setPtxlmoneytype(int v){
		this.ptxlmoneytype=v;
	}
	
	/**
	 * 
	 */
	public int tsxlmoneynum  = 0  ;
	
	public int getTsxlmoneynum(){
		return this.tsxlmoneynum;
	}
	
	public void setTsxlmoneynum(int v){
		this.tsxlmoneynum=v;
	}
	
	/**
	 * 
	 */
	public int tsxlmoneytype  = 0  ;
	
	public int getTsxlmoneytype(){
		return this.tsxlmoneytype;
	}
	
	public void setTsxlmoneytype(int v){
		this.tsxlmoneytype=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> canGems  ;
	
	public java.util.ArrayList<Integer> getCanGems(){
		return this.canGems;
	}
	
	public void setCanGems(java.util.ArrayList<Integer> v){
		this.canGems=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> gems  ;
	
	public java.util.ArrayList<Integer> getGems(){
		return this.gems;
	}
	
	public void setGems(java.util.ArrayList<Integer> v){
		this.gems=v;
	}
	
	/**
	 * 
	 */
	public int needSex  = 0  ;
	
	public int getNeedSex(){
		return this.needSex;
	}
	
	public void setNeedSex(int v){
		this.needSex=v;
	}
	
	/**
	 * 
	 */
	public String needSchool  = null  ;
	
	public String getNeedSchool(){
		return this.needSchool;
	}
	
	public void setNeedSchool(String v){
		this.needSchool=v;
	}
	
	/**
	 * 
	 */
	public int equipcolor  = 0  ;
	
	public int getEquipcolor(){
		return this.equipcolor;
	}
	
	public void setEquipcolor(int v){
		this.equipcolor=v;
	}
	
	/**
	 * 
	 */
	public int suiting  = 0  ;
	
	public int getSuiting(){
		return this.suiting;
	}
	
	public void setSuiting(int v){
		this.suiting=v;
	}
	
	/**
	 * 
	 */
	public String skillid  = null  ;
	
	public String getSkillid(){
		return this.skillid;
	}
	
	public void setSkillid(String v){
		this.skillid=v;
	}
	
	/**
	 * 
	 */
	public String effectid  = null  ;
	
	public String getEffectid(){
		return this.effectid;
	}
	
	public void setEffectid(String v){
		this.effectid=v;
	}
	
	/**
	 * 
	 */
	public int specialAttr  = 0  ;
	
	public int getSpecialAttr(){
		return this.specialAttr;
	}
	
	public void setSpecialAttr(int v){
		this.specialAttr=v;
	}
	
	/**
	 * 
	 */
	public int baseAttrId  = 0  ;
	
	public int getBaseAttrId(){
		return this.baseAttrId;
	}
	
	public void setBaseAttrId(int v){
		this.baseAttrId=v;
	}
	
	/**
	 * 
	 */
	public int addAttrRate  = 0  ;
	
	public int getAddAttrRate(){
		return this.addAttrRate;
	}
	
	public void setAddAttrRate(int v){
		this.addAttrRate=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> addAttrInfo  ;
	
	public java.util.ArrayList<String> getAddAttrInfo(){
		return this.addAttrInfo;
	}
	
	public void setAddAttrInfo(java.util.ArrayList<String> v){
		this.addAttrInfo=v;
	}
	
	/**
	 * 
	 */
	public String randomAttrId  = null  ;
	
	public String getRandomAttrId(){
		return this.randomAttrId;
	}
	
	public void setRandomAttrId(String v){
		this.randomAttrId=v;
	}
	
	/**
	 * 
	 */
	public int randomSkillId  = 0  ;
	
	public int getRandomSkillId(){
		return this.randomSkillId;
	}
	
	public void setRandomSkillId(int v){
		this.randomSkillId=v;
	}
	
	/**
	 * 
	 */
	public int randomEffectId  = 0  ;
	
	public int getRandomEffectId(){
		return this.randomEffectId;
	}
	
	public void setRandomEffectId(int v){
		this.randomEffectId=v;
	}
	
	/**
	 * 
	 */
	public int 是否自动分解  = 0  ;
	
	public int get是否自动分解(){
		return this.是否自动分解;
	}
	
	public void set是否自动分解(int v){
		this.是否自动分解=v;
	}
	
	/**
	 * 
	 */
	public int 分解获得银币  = 0  ;
	
	public int get分解获得银币(){
		return this.分解获得银币;
	}
	
	public void set分解获得银币(int v){
		this.分解获得银币=v;
	}
	
	/**
	 * 
	 */
	public String 分解额外获得物品1  = null  ;
	
	public String get分解额外获得物品1(){
		return this.分解额外获得物品1;
	}
	
	public void set分解额外获得物品1(String v){
		this.分解额外获得物品1=v;
	}
	
	/**
	 * 
	 */
	public String 分解额外获得物品2  = null  ;
	
	public String get分解额外获得物品2(){
		return this.分解额外获得物品2;
	}
	
	public void set分解额外获得物品2(String v){
		this.分解额外获得物品2=v;
	}
	
	/**
	 * 
	 */
	public String 分解额外获得物品3  = null  ;
	
	public String get分解额外获得物品3(){
		return this.分解额外获得物品3;
	}
	
	public void set分解额外获得物品3(String v){
		this.分解额外获得物品3=v;
	}
	
	/**
	 * 
	 */
	public String 分解额外获得物品4  = null  ;
	
	public String get分解额外获得物品4(){
		return this.分解额外获得物品4;
	}
	
	public void set分解额外获得物品4(String v){
		this.分解额外获得物品4=v;
	}
	
	/**
	 * 
	 */
	public String 分解额外获得物品5  = null  ;
	
	public String get分解额外获得物品5(){
		return this.分解额外获得物品5;
	}
	
	public void set分解额外获得物品5(String v){
		this.分解额外获得物品5=v;
	}
	
	/**
	 * 
	 */
	public int treasureScore  = 0  ;
	
	public int getTreasureScore(){
		return this.treasureScore;
	}
	
	public void setTreasureScore(int v){
		this.treasureScore=v;
	}
	
	
};