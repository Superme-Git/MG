package fire.pb.item;


public class ItemShuXing implements mytools.ConvMain.Checkable ,Comparable<ItemShuXing>{

	public int compareTo(ItemShuXing o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public ItemShuXing(){
		super();
	}
	public ItemShuXing(ItemShuXing arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.unit=arg.unit ;
		this.typeid=arg.typeid ;
		this.level=arg.level ;
		this.pickupbind=arg.pickupbind ;
		this.maxstack=arg.maxstack ;
		this.maxown=arg.maxown ;
		this.inbattle=arg.inbattle ;
		this.outbattle=arg.outbattle ;
		this.inbattleuseto=arg.inbattleuseto ;
		this.useup=arg.useup ;
		this.needlevel=arg.needlevel ;
		this.offlineclear=arg.offlineclear ;
		this.bindable=arg.bindable ;
		this.destroy=arg.destroy ;
		this.validtime=arg.validtime ;
		this.sortid=arg.sortid ;
		this.cansale=arg.cansale ;
		this.canstore=arg.canstore ;
		this.recycletime=arg.recycletime ;
		this.recovercost=arg.recovercost ;
		this.salefreezetime=arg.salefreezetime ;
		this.namecolor=arg.namecolor ;
		this.nquality=arg.nquality ;
		this.rare=arg.rare ;
		this.dcansale=arg.dcansale ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			do{
				int tmprefvalue=typeid;
				
				if(tmprefvalue < 1) throw new RuntimeException("ItemShuXing.typeid="+tmprefvalue+",所以不满足条件 ItemShuXing.typeid < 1");
			}while(false);
			do{
				int tmprefvalue=maxstack;
				
				if(tmprefvalue < 1) throw new RuntimeException("ItemShuXing.maxstack="+tmprefvalue+",所以不满足条件 ItemShuXing.maxstack < 1");
				if(tmprefvalue > 9999) throw new RuntimeException("ItemShuXing.maxstack="+tmprefvalue+",所以不满足条件 ItemShuXing.maxstack > 9999");
			}while(false);
			do{
				int tmprefvalue=needlevel;
				
				if(tmprefvalue < 1) throw new RuntimeException("ItemShuXing.needlevel="+tmprefvalue+",所以不满足条件 ItemShuXing.needlevel < 1");
			}while(false);
			do{
				int tmprefvalue=validtime;
				
				if(tmprefvalue < 0) throw new RuntimeException("ItemShuXing.validtime="+tmprefvalue+",所以不满足条件 ItemShuXing.validtime < 0");
			}while(false);
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
	public String unit  = null  ;
	
	public String getUnit(){
		return this.unit;
	}
	
	public void setUnit(String v){
		this.unit=v;
	}
	
	/**
	 * 
	 */
	public int typeid  = 0  ;
	
	public int getTypeid(){
		return this.typeid;
	}
	
	public void setTypeid(int v){
		this.typeid=v;
	}
	
	/**
	 * 等级
	 */
	public int level  = 0  ;
	
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int v){
		this.level=v;
	}
	
	/**
	 * 
	 */
	public boolean pickupbind  = false  ;
	
	public boolean getPickupbind(){
		return this.pickupbind;
	}
	
	public void setPickupbind(boolean v){
		this.pickupbind=v;
	}
	
	/**
	 * 
	 */
	public int maxstack  = 0  ;
	
	public int getMaxstack(){
		return this.maxstack;
	}
	
	public void setMaxstack(int v){
		this.maxstack=v;
	}
	
	/**
	 * 
	 */
	public int maxown  = 0  ;
	
	public int getMaxown(){
		return this.maxown;
	}
	
	public void setMaxown(int v){
		this.maxown=v;
	}
	
	/**
	 * 
	 */
	public int inbattle  = 0  ;
	
	public int getInbattle(){
		return this.inbattle;
	}
	
	public void setInbattle(int v){
		this.inbattle=v;
	}
	
	/**
	 * 
	 */
	public int outbattle  = 0  ;
	
	public int getOutbattle(){
		return this.outbattle;
	}
	
	public void setOutbattle(int v){
		this.outbattle=v;
	}
	
	/**
	 * 
	 */
	public int inbattleuseto  = 0  ;
	
	public int getInbattleuseto(){
		return this.inbattleuseto;
	}
	
	public void setInbattleuseto(int v){
		this.inbattleuseto=v;
	}
	
	/**
	 * 
	 */
	public int useup  = 0  ;
	
	public int getUseup(){
		return this.useup;
	}
	
	public void setUseup(int v){
		this.useup=v;
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
	public boolean offlineclear  = false  ;
	
	public boolean getOfflineclear(){
		return this.offlineclear;
	}
	
	public void setOfflineclear(boolean v){
		this.offlineclear=v;
	}
	
	/**
	 * 
	 */
	public boolean bindable  = false  ;
	
	public boolean getBindable(){
		return this.bindable;
	}
	
	public void setBindable(boolean v){
		this.bindable=v;
	}
	
	/**
	 * 
	 */
	public boolean destroy  = false  ;
	
	public boolean getDestroy(){
		return this.destroy;
	}
	
	public void setDestroy(boolean v){
		this.destroy=v;
	}
	
	/**
	 * 
	 */
	public int validtime  = 0  ;
	
	public int getValidtime(){
		return this.validtime;
	}
	
	public void setValidtime(int v){
		this.validtime=v;
	}
	
	/**
	 * 
	 */
	public int sortid  = 0  ;
	
	public int getSortid(){
		return this.sortid;
	}
	
	public void setSortid(int v){
		this.sortid=v;
	}
	
	/**
	 * 
	 */
	public int cansale  = 0  ;
	
	public int getCansale(){
		return this.cansale;
	}
	
	public void setCansale(int v){
		this.cansale=v;
	}
	
	/**
	 * 
	 */
	public int canstore  = 0  ;
	
	public int getCanstore(){
		return this.canstore;
	}
	
	public void setCanstore(int v){
		this.canstore=v;
	}
	
	/**
	 * 
	 */
	public int recycletime  = 0  ;
	
	public int getRecycletime(){
		return this.recycletime;
	}
	
	public void setRecycletime(int v){
		this.recycletime=v;
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
	
	/**
	 * 
	 */
	public int salefreezetime  = 0  ;
	
	public int getSalefreezetime(){
		return this.salefreezetime;
	}
	
	public void setSalefreezetime(int v){
		this.salefreezetime=v;
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
	public int nquality  = 0  ;
	
	public int getNquality(){
		return this.nquality;
	}
	
	public void setNquality(int v){
		this.nquality=v;
	}
	
	/**
	 * 
	 */
	public int rare  = 0  ;
	
	public int getRare(){
		return this.rare;
	}
	
	public void setRare(int v){
		this.rare=v;
	}
	
	/**
	 * 
	 */
	public int dcansale  = 0  ;
	
	public int getDcansale(){
		return this.dcansale;
	}
	
	public void setDcansale(int v){
		this.dcansale=v;
	}
	
	
};