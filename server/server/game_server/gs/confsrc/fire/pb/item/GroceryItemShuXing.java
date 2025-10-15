package fire.pb.item;


public class GroceryItemShuXing  extends ItemShuXing {

	public int compareTo(GroceryItemShuXing o){
		return this.id-o.id;
	}

	
	public GroceryItemShuXing(ItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public GroceryItemShuXing(){
		super();
	}
	public GroceryItemShuXing(GroceryItemShuXing arg){
		super(arg);
		this.period=arg.period ;
		this.lock=arg.lock ;
		this.effectdate=arg.effectdate ;
		this.overduedate=arg.overduedate ;
		this.needForgeLevel=arg.needForgeLevel ;
		this.needTailorLevel=arg.needTailorLevel ;
		this.needSmeltLevel=arg.needSmeltLevel ;
		this.specialType=arg.specialType ;
		this.treasure=arg.treasure ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * period
	 */
	public long period  = 0L  ;
	
	public long getPeriod(){
		return this.period;
	}
	
	public void setPeriod(long v){
		this.period=v;
	}
	
	/**
	 * 
	 */
	public int lock  = 0  ;
	
	public int getLock(){
		return this.lock;
	}
	
	public void setLock(int v){
		this.lock=v;
	}
	
	/**
	 * 
	 */
	public String effectdate  = null  ;
	
	public String getEffectdate(){
		return this.effectdate;
	}
	
	public void setEffectdate(String v){
		this.effectdate=v;
	}
	
	/**
	 * 
	 */
	public String overduedate  = null  ;
	
	public String getOverduedate(){
		return this.overduedate;
	}
	
	public void setOverduedate(String v){
		this.overduedate=v;
	}
	
	/**
	 * 
	 */
	public int needForgeLevel  = 0  ;
	
	public int getNeedForgeLevel(){
		return this.needForgeLevel;
	}
	
	public void setNeedForgeLevel(int v){
		this.needForgeLevel=v;
	}
	
	/**
	 * 
	 */
	public int needTailorLevel  = 0  ;
	
	public int getNeedTailorLevel(){
		return this.needTailorLevel;
	}
	
	public void setNeedTailorLevel(int v){
		this.needTailorLevel=v;
	}
	
	/**
	 * 
	 */
	public int needSmeltLevel  = 0  ;
	
	public int getNeedSmeltLevel(){
		return this.needSmeltLevel;
	}
	
	public void setNeedSmeltLevel(int v){
		this.needSmeltLevel=v;
	}
	
	/**
	 * 
	 */
	public int specialType  = 0  ;
	
	public int getSpecialType(){
		return this.specialType;
	}
	
	public void setSpecialType(int v){
		this.specialType=v;
	}
	
	/**
	 * 
	 */
	public int treasure  = 0  ;
	
	public int getTreasure(){
		return this.treasure;
	}
	
	public void setTreasure(int v){
		this.treasure=v;
	}
	
	
};