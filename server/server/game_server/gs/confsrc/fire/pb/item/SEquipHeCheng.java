package fire.pb.item;


public class SEquipHeCheng implements mytools.ConvMain.Checkable ,Comparable<SEquipHeCheng>{

	public int compareTo(SEquipHeCheng o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipHeCheng(){
		super();
	}
	public SEquipHeCheng(SEquipHeCheng arg){
		this.id=arg.id ;
		this.itemname=arg.itemname ;
		this.money=arg.money ;
		this.moneytype=arg.moneytype ;
		this.nums=arg.nums ;
		this.nextid=arg.nextid ;
		this.needlevel=arg.needlevel ;
		this.namecolor=arg.namecolor ;
		this.hechengrate=arg.hechengrate ;
		this.hechengfailreturn=arg.hechengfailreturn ;
		this.failreturnnum=arg.failreturnnum ;
		this.hammerid=arg.hammerid ;
		this.hammernum=arg.hammernum ;
		this.hammerrate=arg.hammerrate ;
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
	public String itemname  = null  ;
	
	public String getItemname(){
		return this.itemname;
	}
	
	public void setItemname(String v){
		this.itemname=v;
	}
	
	/**
	 * 
	 */
	public int money  = 0  ;
	
	public int getMoney(){
		return this.money;
	}
	
	public void setMoney(int v){
		this.money=v;
	}
	
	/**
	 * 
	 */
	public int moneytype  = 0  ;
	
	public int getMoneytype(){
		return this.moneytype;
	}
	
	public void setMoneytype(int v){
		this.moneytype=v;
	}
	
	/**
	 * 
	 */
	public int nums  = 0  ;
	
	public int getNums(){
		return this.nums;
	}
	
	public void setNums(int v){
		this.nums=v;
	}
	
	/**
	 * 
	 */
	public int nextid  = 0  ;
	
	public int getNextid(){
		return this.nextid;
	}
	
	public void setNextid(int v){
		this.nextid=v;
	}
	
	/**
	 * 
	 */
	public int needlevel  = 0  ;
	
	public int getNeedlevel(){
		return this.needlevel;
	}
	
	public void setNeedlevel(int v){
		this.needlevel=v;
	}
	
	/**
	 * 
	 */
	public String namecolor  = null  ;
	
	public String getNamecolor(){
		return this.namecolor;
	}
	
	public void setNamecolor(String v){
		this.namecolor=v;
	}
	
	/**
	 * 
	 */
	public int hechengrate  = 0  ;
	
	public int getHechengrate(){
		return this.hechengrate;
	}
	
	public void setHechengrate(int v){
		this.hechengrate=v;
	}
	
	/**
	 * 
	 */
	public int hechengfailreturn  = 0  ;
	
	public int getHechengfailreturn(){
		return this.hechengfailreturn;
	}
	
	public void setHechengfailreturn(int v){
		this.hechengfailreturn=v;
	}
	
	/**
	 * 
	 */
	public int failreturnnum  = 0  ;
	
	public int getFailreturnnum(){
		return this.failreturnnum;
	}
	
	public void setFailreturnnum(int v){
		this.failreturnnum=v;
	}
	
	/**
	 * 
	 */
	public int hammerid  = 0  ;
	
	public int getHammerid(){
		return this.hammerid;
	}
	
	public void setHammerid(int v){
		this.hammerid=v;
	}
	
	/**
	 * 
	 */
	public int hammernum  = 0  ;
	
	public int getHammernum(){
		return this.hammernum;
	}
	
	public void setHammernum(int v){
		this.hammernum=v;
	}
	
	/**
	 * 
	 */
	public int hammerrate  = 0  ;
	
	public int getHammerrate(){
		return this.hammerrate;
	}
	
	public void setHammerrate(int v){
		this.hammerrate=v;
	}
	
	
};