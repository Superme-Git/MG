package fire.pb.fushi;


public class ChargeConfig implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public ChargeConfig(){
		super();
	}
	public ChargeConfig(ChargeConfig arg){
		this.id=arg.id ;
		this.serverid=arg.serverid ;
		this.roofid=arg.roofid ;
		this.sellpricenum=arg.sellpricenum ;
		this.sellnum=arg.sellnum ;
		this.sellnummore=arg.sellnummore ;
		this.kind=arg.kind ;
		this.name=arg.name ;
		this.gameshow=arg.gameshow ;
		this.productid=arg.productid ;
		this.productstr=arg.productstr ;
		this.chargecount=arg.chargecount ;
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
	public int serverid  = 0  ;
	
	public int getServerid(){
		return this.serverid;
	}
	
	public void setServerid(int v){
		this.serverid=v;
	}
	
	/**
	 * 
	 */
	public String roofid  = null  ;
	
	public String getRoofid(){
		return this.roofid;
	}
	
	public void setRoofid(String v){
		this.roofid=v;
	}
	
	/**
	 * 
	 */
	public int sellpricenum  = 0  ;
	
	public int getSellpricenum(){
		return this.sellpricenum;
	}
	
	public void setSellpricenum(int v){
		this.sellpricenum=v;
	}
	
	/**
	 * 
	 */
	public int sellnum  = 0  ;
	
	public int getSellnum(){
		return this.sellnum;
	}
	
	public void setSellnum(int v){
		this.sellnum=v;
	}
	
	/**
	 * 
	 */
	public int sellnummore  = 0  ;
	
	public int getSellnummore(){
		return this.sellnummore;
	}
	
	public void setSellnummore(int v){
		this.sellnummore=v;
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
	public int gameshow  = 0  ;
	
	public int getGameshow(){
		return this.gameshow;
	}
	
	public void setGameshow(int v){
		this.gameshow=v;
	}
	
	/**
	 * 
	 */
	public String productid  = null  ;
	
	public String getProductid(){
		return this.productid;
	}
	
	public void setProductid(String v){
		this.productid=v;
	}
	
	/**
	 * 
	 */
	public String productstr  = null  ;
	
	public String getProductstr(){
		return this.productstr;
	}
	
	public void setProductstr(String v){
		this.productstr=v;
	}
	
	/**
	 * 
	 */
	public int chargecount  = 0  ;
	
	public int getChargecount(){
		return this.chargecount;
	}
	
	public void setChargecount(int v){
		this.chargecount=v;
	}
	
	
};