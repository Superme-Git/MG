package fire.pb.shop;


public class MarketThreeTable implements mytools.ConvMain.Checkable ,Comparable<MarketThreeTable>{

	public int compareTo(MarketThreeTable o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public MarketThreeTable(){
		super();
	}
	public MarketThreeTable(MarketThreeTable arg){
		this.id=arg.id ;
		this.threeno=arg.threeno ;
		this.itemname=arg.itemname ;
		this.itemtype=arg.itemtype ;
		this.logictype=arg.logictype ;
		this.israrity=arg.israrity ;
		this.firstno=arg.firstno ;
		this.twono=arg.twono ;
		this.currency=arg.currency ;
		this.ranges=arg.ranges ;
		this.prices=arg.prices ;
		this.成交量=arg.成交量 ;
		this.挂单量=arg.挂单量 ;
		this.limitlooklv=arg.limitlooklv ;
		this.lvmin=arg.lvmin ;
		this.lvmax=arg.lvmax ;
		this.floatingmin=arg.floatingmin ;
		this.floatingmax=arg.floatingmax ;
		this.floatingprice=arg.floatingprice ;
		this.canstall=arg.canstall ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 道具id
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 序号id
	 */
	public int threeno  = 0  ;
	
	public int getThreeno(){
		return this.threeno;
	}
	
	public void setThreeno(int v){
		this.threeno=v;
	}
	
	/**
	 * 备注名
	 */
	public String itemname  = null  ;
	
	public String getItemname(){
		return this.itemname;
	}
	
	public void setItemname(String v){
		this.itemname=v;
	}
	
	/**
	 * 物品类型  1道具 2宠物 3装备  4特殊类型(用于标示价格浮动特殊处理)
	 */
	public int itemtype  = 0  ;
	
	public int getItemtype(){
		return this.itemtype;
	}
	
	public void setItemtype(int v){
		this.itemtype=v;
	}
	
	/**
	 * 逻辑类型 1等级 2物品id  3品质等级区间
	 */
	public int logictype  = 0  ;
	
	public int getLogictype(){
		return this.logictype;
	}
	
	public void setLogictype(int v){
		this.logictype=v;
	}
	
	/**
	 * 是否珍品 0非珍品 1是珍品
	 */
	public int israrity  = 0  ;
	
	public int getIsrarity(){
		return this.israrity;
	}
	
	public void setIsrarity(int v){
		this.israrity=v;
	}
	
	/**
	 * 1级菜单ID
	 */
	public int firstno  = 0  ;
	
	public int getFirstno(){
		return this.firstno;
	}
	
	public void setFirstno(int v){
		this.firstno=v;
	}
	
	/**
	 * 2级菜单ID
	 */
	public int twono  = 0  ;
	
	public int getTwono(){
		return this.twono;
	}
	
	public void setTwono(int v){
		this.twono=v;
	}
	
	/**
	 * 默认货币类型
	 */
	public int currency  = 0  ;
	
	public int getCurrency(){
		return this.currency;
	}
	
	public void setCurrency(int v){
		this.currency=v;
	}
	
	/**
	 * 区间
	 */
	public java.util.ArrayList<Integer> ranges  ;
	
	public java.util.ArrayList<Integer> getRanges(){
		return this.ranges;
	}
	
	public void setRanges(java.util.ArrayList<Integer> v){
		this.ranges=v;
	}
	
	/**
	 * 默认价格
	 */
	public java.util.ArrayList<Integer> prices  ;
	
	public java.util.ArrayList<Integer> getPrices(){
		return this.prices;
	}
	
	public void setPrices(java.util.ArrayList<Integer> v){
		this.prices=v;
	}
	
	/**
	 * 基准成交量
	 */
	public int 成交量  = 0  ;
	
	public int get成交量(){
		return this.成交量;
	}
	
	public void set成交量(int v){
		this.成交量=v;
	}
	
	/**
	 * 基准挂单量
	 */
	public int 挂单量  = 0  ;
	
	public int get挂单量(){
		return this.挂单量;
	}
	
	public void set挂单量(int v){
		this.挂单量=v;
	}
	
	/**
	 * 可见等级
	 */
	public int limitlooklv  = 0  ;
	
	public int getLimitlooklv(){
		return this.limitlooklv;
	}
	
	public void setLimitlooklv(int v){
		this.limitlooklv=v;
	}
	
	/**
	 * 可购买等级下限
	 */
	public int lvmin  = 0  ;
	
	public int getLvmin(){
		return this.lvmin;
	}
	
	public void setLvmin(int v){
		this.lvmin=v;
	}
	
	/**
	 * 可购买等级上限
	 */
	public int lvmax  = 0  ;
	
	public int getLvmax(){
		return this.lvmax;
	}
	
	public void setLvmax(int v){
		this.lvmax=v;
	}
	
	/**
	 * 价格下限
	 */
	public double floatingmin  = 0.0  ;
	
	public double getFloatingmin(){
		return this.floatingmin;
	}
	
	public void setFloatingmin(double v){
		this.floatingmin=v;
	}
	
	/**
	 * 价格上限
	 */
	public double floatingmax  = 0.0  ;
	
	public double getFloatingmax(){
		return this.floatingmax;
	}
	
	public void setFloatingmax(double v){
		this.floatingmax=v;
	}
	
	/**
	 * 浮动价格系数
	 */
	public double floatingprice  = 0.0  ;
	
	public double getFloatingprice(){
		return this.floatingprice;
	}
	
	public void setFloatingprice(double v){
		this.floatingprice=v;
	}
	
	/**
	 * 是否可以摆摊上架,0不可以 1可以
	 */
	public int canstall  = 0  ;
	
	public int getCanstall(){
		return this.canstall;
	}
	
	public void setCanstall(int v){
		this.canstall=v;
	}
	
	
};