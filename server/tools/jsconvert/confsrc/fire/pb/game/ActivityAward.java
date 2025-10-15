package fire.pb.game;


public class ActivityAward implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public ActivityAward(){
		super();
	}
	public ActivityAward(ActivityAward arg){
		this.id=arg.id ;
		this.money=arg.money ;
		this.gold=arg.gold ;
		this.credit=arg.credit ;
		this.creditValue=arg.creditValue ;
		this.SchoolContribute=arg.SchoolContribute ;
		this.FestivalCredit=arg.FestivalCredit ;
		this.GangContribution=arg.GangContribution ;
		this.GangTaskContribution=arg.GangTaskContribution ;
		this.GangTaskProfit=arg.GangTaskProfit ;
		this.exp=arg.exp ;
		this.shengwang=arg.shengwang ;
		this.petExp=arg.petExp ;
		this.bind=arg.bind ;
		this.msgitemid=arg.msgitemid ;
		this.msgid=arg.msgid ;
		this.teammsg=arg.teammsg ;
		this.equipGenWay=arg.equipGenWay ;
		this.activityid=arg.activityid ;
		this.itemLimitString=arg.itemLimitString ;
		this.cleartype=arg.cleartype ;
		this.msg2itemid=arg.msg2itemid ;
		this.msg2id=arg.msg2id ;
		this.msg2type=arg.msg2type ;
		this.firstClassAward=arg.firstClassAward ;
		this.secondClassAward=arg.secondClassAward ;
		this.secondClassAwardProb=arg.secondClassAwardProb ;
		this.secondClassDenominator=arg.secondClassDenominator ;
		this.randomType=arg.randomType ;
		this.totalProb=arg.totalProb ;
		this.tempBag=arg.tempBag ;
		this.drop1timesprob=arg.drop1timesprob ;
		this.drop2timesprob=arg.drop2timesprob ;
		this.drop3timesprob=arg.drop3timesprob ;
		this.MergeServer=arg.MergeServer ;
		this.shared=arg.shared ;
		this.yingfu=arg.yingfu ;
		this.expweaken=arg.expweaken ;
		this.expaddition=arg.expaddition ;
		this.shengwangtips=arg.shengwangtips ;
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
	public String money  = null  ;
	
	public String getMoney(){
		return this.money;
	}
	
	public void setMoney(String v){
		this.money=v;
	}
	
	/**
	 * 
	 */
	public String gold  = null  ;
	
	public String getGold(){
		return this.gold;
	}
	
	public void setGold(String v){
		this.gold=v;
	}
	
	/**
	 * 发金币时根据比例发信用值
	 */
	public int credit  = 0  ;
	
	public int getCredit(){
		return this.credit;
	}
	
	public void setCredit(int v){
		this.credit=v;
	}
	
	/**
	 * 发金币时根据比例和确定值发信用值
	 */
	public String creditValue  = null  ;
	
	public String getCreditValue(){
		return this.creditValue;
	}
	
	public void setCreditValue(String v){
		this.creditValue=v;
	}
	
	/**
	 * 
	 */
	public String SchoolContribute  = null  ;
	
	public String getSchoolContribute(){
		return this.SchoolContribute;
	}
	
	public void setSchoolContribute(String v){
		this.SchoolContribute=v;
	}
	
	/**
	 * 
	 */
	public String FestivalCredit  = null  ;
	
	public String getFestivalCredit(){
		return this.FestivalCredit;
	}
	
	public void setFestivalCredit(String v){
		this.FestivalCredit=v;
	}
	
	/**
	 * 
	 */
	public String GangContribution  = null  ;
	
	public String getGangContribution(){
		return this.GangContribution;
	}
	
	public void setGangContribution(String v){
		this.GangContribution=v;
	}
	
	/**
	 * 
	 */
	public String GangTaskContribution  = null  ;
	
	public String getGangTaskContribution(){
		return this.GangTaskContribution;
	}
	
	public void setGangTaskContribution(String v){
		this.GangTaskContribution=v;
	}
	
	/**
	 * 
	 */
	public String GangTaskProfit  = null  ;
	
	public String getGangTaskProfit(){
		return this.GangTaskProfit;
	}
	
	public void setGangTaskProfit(String v){
		this.GangTaskProfit=v;
	}
	
	/**
	 * 
	 */
	public String exp  = null  ;
	
	public String getExp(){
		return this.exp;
	}
	
	public void setExp(String v){
		this.exp=v;
	}
	
	/**
	 * 
	 */
	public String shengwang  = null  ;
	
	public String getShengwang(){
		return this.shengwang;
	}
	
	public void setShengwang(String v){
		this.shengwang=v;
	}
	
	/**
	 * 
	 */
	public String petExp  = null  ;
	
	public String getPetExp(){
		return this.petExp;
	}
	
	public void setPetExp(String v){
		this.petExp=v;
	}
	
	/**
	 * 
	 */
	public int bind  = 0  ;
	
	public int getBind(){
		return this.bind;
	}
	
	public void setBind(int v){
		this.bind=v;
	}
	
	/**
	 * 会发公告的物品id
	 */
	public String msgitemid  = null  ;
	
	public String getMsgitemid(){
		return this.msgitemid;
	}
	
	public void setMsgitemid(String v){
		this.msgitemid=v;
	}
	
	/**
	 * 公告msg的id
	 */
	public int msgid  = 0  ;
	
	public int getMsgid(){
		return this.msgid;
	}
	
	public void setMsgid(int v){
		this.msgid=v;
	}
	
	/**
	 * 是否发队伍提示
	 */
	public int teammsg  = 0  ;
	
	public int getTeammsg(){
		return this.teammsg;
	}
	
	public void setTeammsg(int v){
		this.teammsg=v;
	}
	
	/**
	 * 
	 */
	public int equipGenWay  = 0  ;
	
	public int getEquipGenWay(){
		return this.equipGenWay;
	}
	
	public void setEquipGenWay(int v){
		this.equipGenWay=v;
	}
	
	/**
	 * 
	 */
	public int activityid  = 0  ;
	
	public int getActivityid(){
		return this.activityid;
	}
	
	public void setActivityid(int v){
		this.activityid=v;
	}
	
	/**
	 * 
	 */
	public String itemLimitString  = null  ;
	
	public String getItemLimitString(){
		return this.itemLimitString;
	}
	
	public void setItemLimitString(String v){
		this.itemLimitString=v;
	}
	
	/**
	 * 
	 */
	public int cleartype  = 0  ;
	
	public int getCleartype(){
		return this.cleartype;
	}
	
	public void setCleartype(int v){
		this.cleartype=v;
	}
	
	/**
	 * 
	 */
	public String msg2itemid  = null  ;
	
	public String getMsg2itemid(){
		return this.msg2itemid;
	}
	
	public void setMsg2itemid(String v){
		this.msg2itemid=v;
	}
	
	/**
	 * 
	 */
	public int msg2id  = 0  ;
	
	public int getMsg2id(){
		return this.msg2id;
	}
	
	public void setMsg2id(int v){
		this.msg2id=v;
	}
	
	/**
	 * 
	 */
	public int msg2type  = 0  ;
	
	public int getMsg2type(){
		return this.msg2type;
	}
	
	public void setMsg2type(int v){
		this.msg2type=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> firstClassAward  ;
	
	public java.util.ArrayList<Integer> getFirstClassAward(){
		return this.firstClassAward;
	}
	
	public void setFirstClassAward(java.util.ArrayList<Integer> v){
		this.firstClassAward=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> secondClassAward  ;
	
	public java.util.ArrayList<Integer> getSecondClassAward(){
		return this.secondClassAward;
	}
	
	public void setSecondClassAward(java.util.ArrayList<Integer> v){
		this.secondClassAward=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> secondClassAwardProb  ;
	
	public java.util.ArrayList<String> getSecondClassAwardProb(){
		return this.secondClassAwardProb;
	}
	
	public void setSecondClassAwardProb(java.util.ArrayList<String> v){
		this.secondClassAwardProb=v;
	}
	
	/**
	 * 二类奖励的总概率
	 */
	public int secondClassDenominator  = 0  ;
	
	public int getSecondClassDenominator(){
		return this.secondClassDenominator;
	}
	
	public void setSecondClassDenominator(int v){
		this.secondClassDenominator=v;
	}
	
	/**
	 * 二类奖励的随机方式,0为关联随机,1为不关联随机
	 */
	public int randomType  = 0  ;
	
	public int getRandomType(){
		return this.randomType;
	}
	
	public void setRandomType(int v){
		this.randomType=v;
	}
	
	/**
	 * 是否给予物品奖励
	 */
	public String totalProb  = null  ;
	
	public String getTotalProb(){
		return this.totalProb;
	}
	
	public void setTotalProb(String v){
		this.totalProb=v;
	}
	
	/**
	 * 是否放入临时包裹,0不放,1放
	 */
	public int tempBag  = 0  ;
	
	public int getTempBag(){
		return this.tempBag;
	}
	
	public void setTempBag(int v){
		this.tempBag=v;
	}
	
	/**
	 * 
	 */
	public int drop1timesprob  = 0  ;
	
	public int getDrop1timesprob(){
		return this.drop1timesprob;
	}
	
	public void setDrop1timesprob(int v){
		this.drop1timesprob=v;
	}
	
	/**
	 * 
	 */
	public int drop2timesprob  = 0  ;
	
	public int getDrop2timesprob(){
		return this.drop2timesprob;
	}
	
	public void setDrop2timesprob(int v){
		this.drop2timesprob=v;
	}
	
	/**
	 * 
	 */
	public int drop3timesprob  = 0  ;
	
	public int getDrop3timesprob(){
		return this.drop3timesprob;
	}
	
	public void setDrop3timesprob(int v){
		this.drop3timesprob=v;
	}
	
	/**
	 * 合服buff是否启用，0 不启用，1启用
	 */
	public int MergeServer  = 0  ;
	
	public int getMergeServer(){
		return this.MergeServer;
	}
	
	public void setMergeServer(int v){
		this.MergeServer=v;
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
	 * 0 不触发, 1 触发
	 */
	public int yingfu  = 0  ;
	
	public int getYingfu(){
		return this.yingfu;
	}
	
	public void setYingfu(int v){
		this.yingfu=v;
	}
	
	/**
	 * 0 不衰减, 1 衰减
	 */
	public int expweaken  = 0  ;
	
	public int getExpweaken(){
		return this.expweaken;
	}
	
	public void setExpweaken(int v){
		this.expweaken=v;
	}
	
	/**
	 * 0 不加成, 1 加成
	 */
	public int expaddition  = 0  ;
	
	public int getExpaddition(){
		return this.expaddition;
	}
	
	public void setExpaddition(int v){
		this.expaddition=v;
	}
	
	/**
	 * 
	 */
	public int shengwangtips  = 0  ;
	
	public int getShengwangtips(){
		return this.shengwangtips;
	}
	
	public void setShengwangtips(int v){
		this.shengwangtips=v;
	}
	
	
};