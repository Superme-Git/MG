package fire.pb.shop;


public class SGoods implements mytools.ConvMain.Checkable ,Comparable<SGoods>{

	public int compareTo(SGoods o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SGoods(){
		super();
	}
	public SGoods(SGoods arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.type=arg.type ;
		this.itemId=arg.itemId ;
		this.currencys=arg.currencys ;
		this.prices=arg.prices ;
		this.oldprices=arg.oldprices ;
		this.limitType=arg.limitType ;
		this.limitNum=arg.limitNum ;
		this.limitSaleNum=arg.limitSaleNum ;
		this.limitLookLv=arg.limitLookLv ;
		this.lvMin=arg.lvMin ;
		this.lvMax=arg.lvMax ;
		this.floatingRisePrice=arg.floatingRisePrice ;
		this.floatingDepreciatePrice=arg.floatingDepreciatePrice ;
		this.floatingmax=arg.floatingmax ;
		this.floatingmin=arg.floatingmin ;
		this.costItemId=arg.costItemId ;
		this.costItemNum=arg.costItemNum ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 商品ID
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 商品名备注
	 */
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 商品类型
	 */
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 类型对应
	 */
	public int itemId  = 0  ;
	
	public int getItemId(){
		return this.itemId;
	}
	
	public void setItemId(int v){
		this.itemId=v;
	}
	
	/**
	 * 货币1,货币2
	 */
	public java.util.ArrayList<Integer> currencys  ;
	
	public java.util.ArrayList<Integer> getCurrencys(){
		return this.currencys;
	}
	
	public void setCurrencys(java.util.ArrayList<Integer> v){
		this.currencys=v;
	}
	
	/**
	 * 现价1,现价2
	 */
	public java.util.ArrayList<Integer> prices  ;
	
	public java.util.ArrayList<Integer> getPrices(){
		return this.prices;
	}
	
	public void setPrices(java.util.ArrayList<Integer> v){
		this.prices=v;
	}
	
	/**
	 * 价格1,价格2
	 */
	public java.util.ArrayList<Integer> oldprices  ;
	
	public java.util.ArrayList<Integer> getOldprices(){
		return this.oldprices;
	}
	
	public void setOldprices(java.util.ArrayList<Integer> v){
		this.oldprices=v;
	}
	
	/**
	 * 限购类型
	 */
	public int limitType  = 0  ;
	
	public int getLimitType(){
		return this.limitType;
	}
	
	public void setLimitType(int v){
		this.limitType=v;
	}
	
	/**
	 * 限购数量
	 */
	public int limitNum  = 0  ;
	
	public int getLimitNum(){
		return this.limitNum;
	}
	
	public void setLimitNum(int v){
		this.limitNum=v;
	}
	
	/**
	 * 限售数量
	 */
	public int limitSaleNum  = 0  ;
	
	public int getLimitSaleNum(){
		return this.limitSaleNum;
	}
	
	public void setLimitSaleNum(int v){
		this.limitSaleNum=v;
	}
	
	/**
	 * 可见等级
	 */
	public int limitLookLv  = 0  ;
	
	public int getLimitLookLv(){
		return this.limitLookLv;
	}
	
	public void setLimitLookLv(int v){
		this.limitLookLv=v;
	}
	
	/**
	 * 可购买等级下限
	 */
	public int lvMin  = 0  ;
	
	public int getLvMin(){
		return this.lvMin;
	}
	
	public void setLvMin(int v){
		this.lvMin=v;
	}
	
	/**
	 * 可购买等级上限
	 */
	public int lvMax  = 0  ;
	
	public int getLvMax(){
		return this.lvMax;
	}
	
	public void setLvMax(int v){
		this.lvMax=v;
	}
	
	/**
	 * 价格上涨浮动
	 */
	public double floatingRisePrice  = 0.0  ;
	
	public double getFloatingRisePrice(){
		return this.floatingRisePrice;
	}
	
	public void setFloatingRisePrice(double v){
		this.floatingRisePrice=v;
	}
	
	/**
	 * 价格下降浮动
	 */
	public double floatingDepreciatePrice  = 0.0  ;
	
	public double getFloatingDepreciatePrice(){
		return this.floatingDepreciatePrice;
	}
	
	public void setFloatingDepreciatePrice(double v){
		this.floatingDepreciatePrice=v;
	}
	
	/**
	 * 价格上限(对于原价的系数)
	 */
	public double floatingmax  = 0.0  ;
	
	public double getFloatingmax(){
		return this.floatingmax;
	}
	
	public void setFloatingmax(double v){
		this.floatingmax=v;
	}
	
	/**
	 * 价格下限(对于原价的系数)
	 */
	public double floatingmin  = 0.0  ;
	
	public double getFloatingmin(){
		return this.floatingmin;
	}
	
	public void setFloatingmin(double v){
		this.floatingmin=v;
	}
	
	/**
	 * 道具消耗id
	 */
	public int costItemId  = 0  ;
	
	public int getCostItemId(){
		return this.costItemId;
	}
	
	public void setCostItemId(int v){
		this.costItemId=v;
	}
	
	/**
	 * 道具消耗数量
	 */
	public int costItemNum  = 0  ;
	
	public int getCostItemNum(){
		return this.costItemNum;
	}
	
	public void setCostItemNum(int v){
		this.costItemNum=v;
	}
	
	
};