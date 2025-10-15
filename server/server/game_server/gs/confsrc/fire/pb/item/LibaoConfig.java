package fire.pb.item;


public class LibaoConfig implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public LibaoConfig(){
		super();
	}
	public LibaoConfig(LibaoConfig arg){
		this.id=arg.id ;
		this.libaoid=arg.libaoid ;
		this.roletype=arg.roletype ;
		this.school=arg.school ;
		this.sextype=arg.sextype ;
		this.opennotice=arg.opennotice ;
		this.awardtableid=arg.awardtableid ;
		this.moneyaward1=arg.moneyaward1 ;
		this.moneyaward2=arg.moneyaward2 ;
		this.vipexpaward=arg.vipexpaward ;
		this.currencytype=arg.currencytype ;
		this.currencyvalue=arg.currencyvalue ;
		this.itemids=arg.itemids ;
		this.itemnums=arg.itemnums ;
		this.itembinds=arg.itembinds ;
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
	public int libaoid  = 0  ;
	
	public int getLibaoid(){
		return this.libaoid;
	}
	
	public void setLibaoid(int v){
		this.libaoid=v;
	}
	
	/**
	 * 
	 */
	public int roletype  = 0  ;
	
	public int getRoletype(){
		return this.roletype;
	}
	
	public void setRoletype(int v){
		this.roletype=v;
	}
	
	/**
	 * 
	 */
	public int school  = 0  ;
	
	public int getSchool(){
		return this.school;
	}
	
	public void setSchool(int v){
		this.school=v;
	}
	
	/**
	 * 
	 */
	public int sextype  = 0  ;
	
	public int getSextype(){
		return this.sextype;
	}
	
	public void setSextype(int v){
		this.sextype=v;
	}
	
	/**
	 * 
	 */
	public int opennotice  = 0  ;
	
	public int getOpennotice(){
		return this.opennotice;
	}
	
	public void setOpennotice(int v){
		this.opennotice=v;
	}
	
	/**
	 * 
	 */
	public int awardtableid  = 0  ;
	
	public int getAwardtableid(){
		return this.awardtableid;
	}
	
	public void setAwardtableid(int v){
		this.awardtableid=v;
	}
	
	/**
	 * 
	 */
	public long moneyaward1  = 0L  ;
	
	public long getMoneyaward1(){
		return this.moneyaward1;
	}
	
	public void setMoneyaward1(long v){
		this.moneyaward1=v;
	}
	
	/**
	 * 
	 */
	public int moneyaward2  = 0  ;
	
	public int getMoneyaward2(){
		return this.moneyaward2;
	}
	
	public void setMoneyaward2(int v){
		this.moneyaward2=v;
	}
	
	/**
	 * 
	 */
	public int vipexpaward  = 0  ;
	
	public int getVipexpaward(){
		return this.vipexpaward;
	}
	
	public void setVipexpaward(int v){
		this.vipexpaward=v;
	}
	
	/**
	 * 
	 */
	public int currencytype  = 0  ;
	
	public int getCurrencytype(){
		return this.currencytype;
	}
	
	public void setCurrencytype(int v){
		this.currencytype=v;
	}
	
	/**
	 * 
	 */
	public long currencyvalue  = 0L  ;
	
	public long getCurrencyvalue(){
		return this.currencyvalue;
	}
	
	public void setCurrencyvalue(long v){
		this.currencyvalue=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> itemids  ;
	
	public java.util.ArrayList<Integer> getItemids(){
		return this.itemids;
	}
	
	public void setItemids(java.util.ArrayList<Integer> v){
		this.itemids=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> itemnums  ;
	
	public java.util.ArrayList<Integer> getItemnums(){
		return this.itemnums;
	}
	
	public void setItemnums(java.util.ArrayList<Integer> v){
		this.itemnums=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> itembinds  ;
	
	public java.util.ArrayList<Integer> getItembinds(){
		return this.itembinds;
	}
	
	public void setItembinds(java.util.ArrayList<Integer> v){
		this.itembinds=v;
	}
	
	
};