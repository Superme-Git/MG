package fire.pb.pet;


public class PetAttr implements mytools.ConvMain.Checkable ,Comparable<PetAttr>{

	public int compareTo(PetAttr o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PetAttr(){
		super();
	}
	public PetAttr(PetAttr arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.colour=arg.colour ;
		this.quality=arg.quality ;
		this.unusualid=arg.unusualid ;
		this.uselevel=arg.uselevel ;
		this.takelevel=arg.takelevel ;
		this.initlevel=arg.initlevel ;
		this.kind=arg.kind ;
		this.life=arg.life ;
		this.shape=arg.shape ;
		this.SellPetIcon=arg.SellPetIcon ;
		this.attackaptmin=arg.attackaptmin ;
		this.attackaptmax=arg.attackaptmax ;
		this.defendaptmin=arg.defendaptmin ;
		this.defendaptmax=arg.defendaptmax ;
		this.phyforceaptmin=arg.phyforceaptmin ;
		this.phyforceaptmax=arg.phyforceaptmax ;
		this.magicaptmin=arg.magicaptmin ;
		this.magicaptmax=arg.magicaptmax ;
		this.speedaptmin=arg.speedaptmin ;
		this.speedaptmax=arg.speedaptmax ;
		this.attackaptconst=arg.attackaptconst ;
		this.defendaptconst=arg.defendaptconst ;
		this.phyforceaptconst=arg.phyforceaptconst ;
		this.magicaptconst=arg.magicaptconst ;
		this.speedaptconst=arg.speedaptconst ;
		this.healgrow=arg.healgrow ;
		this.ctrlhitgrow=arg.ctrlhitgrow ;
		this.ctrlresistgrow=arg.ctrlresistgrow ;
		this.growrate=arg.growrate ;
		this.growrateconst=arg.growrateconst ;
		this.addpoint=arg.addpoint ;
		this.skills=arg.skills ;
		this.initPoint=arg.initPoint ;
		this.initPointAssignType=arg.initPointAssignType ;
		this.washitemid=arg.washitemid ;
		this.washitemnum=arg.washitemnum ;
		this.washnewpetid=arg.washnewpetid ;
		this.certificationcost=arg.certificationcost ;
		this.cancelcertificationcost=arg.cancelcertificationcost ;
		this.extAi=arg.extAi ;
		this.treasureScore=arg.treasureScore ;
		this.treasureskillnums=arg.treasureskillnums ;
		this.sellPrice=arg.sellPrice ;
		this.washcount=arg.washcount ;
		this.colorselect=arg.colorselect ;
		this.marketfreezetime=arg.marketfreezetime ;
		this.isbindskill1=arg.isbindskill1 ;
		this.isbindskill2=arg.isbindskill2 ;
		this.isbindskill3=arg.isbindskill3 ;
		this.isbindskill4=arg.isbindskill4 ;
		this.isbindskill5=arg.isbindskill5 ;
		this.recovercost=arg.recovercost ;
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
	public String colour  = null  ;
	
	public String getColour(){
		return this.colour;
	}
	
	public void setColour(String v){
		this.colour=v;
	}
	
	/**
	 * 
	 */
	public int quality  = 0  ;
	
	public int getQuality(){
		return this.quality;
	}
	
	public void setQuality(int v){
		this.quality=v;
	}
	
	/**
	 * 
	 */
	public int unusualid  = 0  ;
	
	public int getUnusualid(){
		return this.unusualid;
	}
	
	public void setUnusualid(int v){
		this.unusualid=v;
	}
	
	/**
	 * 
	 */
	public int uselevel  = 0  ;
	
	public int getUselevel(){
		return this.uselevel;
	}
	
	public void setUselevel(int v){
		this.uselevel=v;
	}
	
	/**
	 * 
	 */
	public int takelevel  = 0  ;
	
	public int getTakelevel(){
		return this.takelevel;
	}
	
	public void setTakelevel(int v){
		this.takelevel=v;
	}
	
	/**
	 * 
	 */
	public int initlevel  = 0  ;
	
	public int getInitlevel(){
		return this.initlevel;
	}
	
	public void setInitlevel(int v){
		this.initlevel=v;
	}
	
	/**
	 * 
	 */
	public int kind  = 0  ;
	
	public int getKind(){
		return this.kind;
	}
	
	public void setKind(int v){
		this.kind=v;
	}
	
	/**
	 * 
	 */
	public int life  = 0  ;
	
	public int getLife(){
		return this.life;
	}
	
	public void setLife(int v){
		this.life=v;
	}
	
	/**
	 * 
	 */
	public int shape  = 0  ;
	
	public int getShape(){
		return this.shape;
	}
	
	public void setShape(int v){
		this.shape=v;
	}
	
	/**
	 * 
	 */
	public int SellPetIcon  = 0  ;
	
	public int getSellPetIcon(){
		return this.SellPetIcon;
	}
	
	public void setSellPetIcon(int v){
		this.SellPetIcon=v;
	}
	
	/**
	 * 
	 */
	public int attackaptmin  = 0  ;
	
	public int getAttackaptmin(){
		return this.attackaptmin;
	}
	
	public void setAttackaptmin(int v){
		this.attackaptmin=v;
	}
	
	/**
	 * 
	 */
	public int attackaptmax  = 0  ;
	
	public int getAttackaptmax(){
		return this.attackaptmax;
	}
	
	public void setAttackaptmax(int v){
		this.attackaptmax=v;
	}
	
	/**
	 * 
	 */
	public int defendaptmin  = 0  ;
	
	public int getDefendaptmin(){
		return this.defendaptmin;
	}
	
	public void setDefendaptmin(int v){
		this.defendaptmin=v;
	}
	
	/**
	 * 
	 */
	public int defendaptmax  = 0  ;
	
	public int getDefendaptmax(){
		return this.defendaptmax;
	}
	
	public void setDefendaptmax(int v){
		this.defendaptmax=v;
	}
	
	/**
	 * 
	 */
	public int phyforceaptmin  = 0  ;
	
	public int getPhyforceaptmin(){
		return this.phyforceaptmin;
	}
	
	public void setPhyforceaptmin(int v){
		this.phyforceaptmin=v;
	}
	
	/**
	 * 
	 */
	public int phyforceaptmax  = 0  ;
	
	public int getPhyforceaptmax(){
		return this.phyforceaptmax;
	}
	
	public void setPhyforceaptmax(int v){
		this.phyforceaptmax=v;
	}
	
	/**
	 * 
	 */
	public int magicaptmin  = 0  ;
	
	public int getMagicaptmin(){
		return this.magicaptmin;
	}
	
	public void setMagicaptmin(int v){
		this.magicaptmin=v;
	}
	
	/**
	 * 
	 */
	public int magicaptmax  = 0  ;
	
	public int getMagicaptmax(){
		return this.magicaptmax;
	}
	
	public void setMagicaptmax(int v){
		this.magicaptmax=v;
	}
	
	/**
	 * 
	 */
	public int speedaptmin  = 0  ;
	
	public int getSpeedaptmin(){
		return this.speedaptmin;
	}
	
	public void setSpeedaptmin(int v){
		this.speedaptmin=v;
	}
	
	/**
	 * 
	 */
	public int speedaptmax  = 0  ;
	
	public int getSpeedaptmax(){
		return this.speedaptmax;
	}
	
	public void setSpeedaptmax(int v){
		this.speedaptmax=v;
	}
	
	/**
	 * 
	 */
	public int attackaptconst  = 0  ;
	
	public int getAttackaptconst(){
		return this.attackaptconst;
	}
	
	public void setAttackaptconst(int v){
		this.attackaptconst=v;
	}
	
	/**
	 * 
	 */
	public int defendaptconst  = 0  ;
	
	public int getDefendaptconst(){
		return this.defendaptconst;
	}
	
	public void setDefendaptconst(int v){
		this.defendaptconst=v;
	}
	
	/**
	 * 
	 */
	public int phyforceaptconst  = 0  ;
	
	public int getPhyforceaptconst(){
		return this.phyforceaptconst;
	}
	
	public void setPhyforceaptconst(int v){
		this.phyforceaptconst=v;
	}
	
	/**
	 * 
	 */
	public int magicaptconst  = 0  ;
	
	public int getMagicaptconst(){
		return this.magicaptconst;
	}
	
	public void setMagicaptconst(int v){
		this.magicaptconst=v;
	}
	
	/**
	 * 
	 */
	public int speedaptconst  = 0  ;
	
	public int getSpeedaptconst(){
		return this.speedaptconst;
	}
	
	public void setSpeedaptconst(int v){
		this.speedaptconst=v;
	}
	
	/**
	 * 
	 */
	public double healgrow  = 0.0  ;
	
	public double getHealgrow(){
		return this.healgrow;
	}
	
	public void setHealgrow(double v){
		this.healgrow=v;
	}
	
	/**
	 * 
	 */
	public double ctrlhitgrow  = 0.0  ;
	
	public double getCtrlhitgrow(){
		return this.ctrlhitgrow;
	}
	
	public void setCtrlhitgrow(double v){
		this.ctrlhitgrow=v;
	}
	
	/**
	 * 
	 */
	public double ctrlresistgrow  = 0.0  ;
	
	public double getCtrlresistgrow(){
		return this.ctrlresistgrow;
	}
	
	public void setCtrlresistgrow(double v){
		this.ctrlresistgrow=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> growrate  ;
	
	public java.util.ArrayList<Integer> getGrowrate(){
		return this.growrate;
	}
	
	public void setGrowrate(java.util.ArrayList<Integer> v){
		this.growrate=v;
	}
	
	/**
	 * 
	 */
	public int growrateconst  = 0  ;
	
	public int getGrowrateconst(){
		return this.growrateconst;
	}
	
	public void setGrowrateconst(int v){
		this.growrateconst=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> addpoint  ;
	
	public java.util.ArrayList<Integer> getAddpoint(){
		return this.addpoint;
	}
	
	public void setAddpoint(java.util.ArrayList<Integer> v){
		this.addpoint=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<SkillRate> skills  ;
	
	public java.util.ArrayList<SkillRate> getSkills(){
		return this.skills;
	}
	
	public void setSkills(java.util.ArrayList<SkillRate> v){
		this.skills=v;
	}
	
	/**
	 * 
	 */
	public int initPoint  = 0  ;
	
	public int getInitPoint(){
		return this.initPoint;
	}
	
	public void setInitPoint(int v){
		this.initPoint=v;
	}
	
	/**
	 * 
	 */
	public int initPointAssignType  = 0  ;
	
	public int getInitPointAssignType(){
		return this.initPointAssignType;
	}
	
	public void setInitPointAssignType(int v){
		this.initPointAssignType=v;
	}
	
	/**
	 * 
	 */
	public int washitemid  = 0  ;
	
	public int getWashitemid(){
		return this.washitemid;
	}
	
	public void setWashitemid(int v){
		this.washitemid=v;
	}
	
	/**
	 * 
	 */
	public int washitemnum  = 0  ;
	
	public int getWashitemnum(){
		return this.washitemnum;
	}
	
	public void setWashitemnum(int v){
		this.washitemnum=v;
	}
	
	/**
	 * 洗练后的新宠ID
	 */
	public String washnewpetid  = null  ;
	
	public String getWashnewpetid(){
		return this.washnewpetid;
	}
	
	public void setWashnewpetid(String v){
		this.washnewpetid=v;
	}
	
	/**
	 * 
	 */
	public int certificationcost  = 0  ;
	
	public int getCertificationcost(){
		return this.certificationcost;
	}
	
	public void setCertificationcost(int v){
		this.certificationcost=v;
	}
	
	/**
	 * 
	 */
	public int cancelcertificationcost  = 0  ;
	
	public int getCancelcertificationcost(){
		return this.cancelcertificationcost;
	}
	
	public void setCancelcertificationcost(int v){
		this.cancelcertificationcost=v;
	}
	
	/**
	 * 
	 */
	public int extAi  = 0  ;
	
	public int getExtAi(){
		return this.extAi;
	}
	
	public void setExtAi(int v){
		this.extAi=v;
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
	
	/**
	 * 
	 */
	public int treasureskillnums  = 0  ;
	
	public int getTreasureskillnums(){
		return this.treasureskillnums;
	}
	
	public void setTreasureskillnums(int v){
		this.treasureskillnums=v;
	}
	
	/**
	 * 
	 */
	public int sellPrice  = 0  ;
	
	public int getSellPrice(){
		return this.sellPrice;
	}
	
	public void setSellPrice(int v){
		this.sellPrice=v;
	}
	
	/**
	 * 
	 */
	public int washcount  = 0  ;
	
	public int getWashcount(){
		return this.washcount;
	}
	
	public void setWashcount(int v){
		this.washcount=v;
	}
	
	/**
	 * 
	 */
	public String colorselect  = null  ;
	
	public String getColorselect(){
		return this.colorselect;
	}
	
	public void setColorselect(String v){
		this.colorselect=v;
	}
	
	/**
	 * 
	 */
	public int marketfreezetime  = 0  ;
	
	public int getMarketfreezetime(){
		return this.marketfreezetime;
	}
	
	public void setMarketfreezetime(int v){
		this.marketfreezetime=v;
	}
	
	/**
	 * 
	 */
	public int isbindskill1  = 0  ;
	
	public int getIsbindskill1(){
		return this.isbindskill1;
	}
	
	public void setIsbindskill1(int v){
		this.isbindskill1=v;
	}
	
	/**
	 * 
	 */
	public int isbindskill2  = 0  ;
	
	public int getIsbindskill2(){
		return this.isbindskill2;
	}
	
	public void setIsbindskill2(int v){
		this.isbindskill2=v;
	}
	
	/**
	 * 
	 */
	public int isbindskill3  = 0  ;
	
	public int getIsbindskill3(){
		return this.isbindskill3;
	}
	
	public void setIsbindskill3(int v){
		this.isbindskill3=v;
	}
	
	/**
	 * 
	 */
	public int isbindskill4  = 0  ;
	
	public int getIsbindskill4(){
		return this.isbindskill4;
	}
	
	public void setIsbindskill4(int v){
		this.isbindskill4=v;
	}
	
	/**
	 * 
	 */
	public int isbindskill5  = 0  ;
	
	public int getIsbindskill5(){
		return this.isbindskill5;
	}
	
	public void setIsbindskill5(int v){
		this.isbindskill5=v;
	}
	
	/**
	 * 
	 */
	public int recovercost  = 0  ;
	
	public int getRecovercost(){
		return this.recovercost;
	}
	
	public void setRecovercost(int v){
		this.recovercost=v;
	}
	
	
};