package fire.pb.fushi;


public class SSpotCardConfig implements mytools.ConvMain.Checkable ,Comparable<SSpotCardConfig>{

	public int compareTo(SSpotCardConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSpotCardConfig(){
		super();
	}
	public SSpotCardConfig(SSpotCardConfig arg){
		this.id=arg.id ;
		this.buynummin=arg.buynummin ;
		this.buynummax=arg.buynummax ;
		this.buypricemin=arg.buypricemin ;
		this.buypricemax=arg.buypricemax ;
		this.sellnummin=arg.sellnummin ;
		this.sellnummax=arg.sellnummax ;
		this.sellpricemin=arg.sellpricemin ;
		this.sellpricemax=arg.sellpricemax ;
		this.tradingmoney=arg.tradingmoney ;
		this.tradingshui=arg.tradingshui ;
		this.livetime=arg.livetime ;
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
	public int buynummin  = 0  ;
	
	public int getBuynummin(){
		return this.buynummin;
	}
	
	public void setBuynummin(int v){
		this.buynummin=v;
	}
	
	/**
	 * 
	 */
	public int buynummax  = 0  ;
	
	public int getBuynummax(){
		return this.buynummax;
	}
	
	public void setBuynummax(int v){
		this.buynummax=v;
	}
	
	/**
	 * 
	 */
	public int buypricemin  = 0  ;
	
	public int getBuypricemin(){
		return this.buypricemin;
	}
	
	public void setBuypricemin(int v){
		this.buypricemin=v;
	}
	
	/**
	 * 
	 */
	public int buypricemax  = 0  ;
	
	public int getBuypricemax(){
		return this.buypricemax;
	}
	
	public void setBuypricemax(int v){
		this.buypricemax=v;
	}
	
	/**
	 * 
	 */
	public int sellnummin  = 0  ;
	
	public int getSellnummin(){
		return this.sellnummin;
	}
	
	public void setSellnummin(int v){
		this.sellnummin=v;
	}
	
	/**
	 * 
	 */
	public int sellnummax  = 0  ;
	
	public int getSellnummax(){
		return this.sellnummax;
	}
	
	public void setSellnummax(int v){
		this.sellnummax=v;
	}
	
	/**
	 * 
	 */
	public int sellpricemin  = 0  ;
	
	public int getSellpricemin(){
		return this.sellpricemin;
	}
	
	public void setSellpricemin(int v){
		this.sellpricemin=v;
	}
	
	/**
	 * 
	 */
	public int sellpricemax  = 0  ;
	
	public int getSellpricemax(){
		return this.sellpricemax;
	}
	
	public void setSellpricemax(int v){
		this.sellpricemax=v;
	}
	
	/**
	 * 
	 */
	public double tradingmoney  = 0.0  ;
	
	public double getTradingmoney(){
		return this.tradingmoney;
	}
	
	public void setTradingmoney(double v){
		this.tradingmoney=v;
	}
	
	/**
	 * 
	 */
	public double tradingshui  = 0.0  ;
	
	public double getTradingshui(){
		return this.tradingshui;
	}
	
	public void setTradingshui(double v){
		this.tradingshui=v;
	}
	
	/**
	 * 
	 */
	public int livetime  = 0  ;
	
	public int getLivetime(){
		return this.livetime;
	}
	
	public void setLivetime(int v){
		this.livetime=v;
	}
	
	
};