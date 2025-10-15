package fire.pb.fushi;


public class SMonthCardConfigDayPay implements mytools.ConvMain.Checkable ,Comparable<SMonthCardConfigDayPay>{

	public int compareTo(SMonthCardConfigDayPay o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SMonthCardConfigDayPay(){
		super();
	}
	public SMonthCardConfigDayPay(SMonthCardConfigDayPay arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.rewardid=arg.rewardid ;
		this.itemid=arg.itemid ;
		this.itemnum=arg.itemnum ;
		this.type=arg.type ;
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
	public int rewardid  = 0  ;
	
	public int getRewardid(){
		return this.rewardid;
	}
	
	public void setRewardid(int v){
		this.rewardid=v;
	}
	
	/**
	 * 
	 */
	public int itemid  = 0  ;
	
	public int getItemid(){
		return this.itemid;
	}
	
	public void setItemid(int v){
		this.itemid=v;
	}
	
	/**
	 * 
	 */
	public int itemnum  = 0  ;
	
	public int getItemnum(){
		return this.itemnum;
	}
	
	public void setItemnum(int v){
		this.itemnum=v;
	}
	
	/**
	 * 
	 */
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	
};