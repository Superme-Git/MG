package fire.pb.item;


public class STimingReward implements mytools.ConvMain.Checkable ,Comparable<STimingReward>{

	public int compareTo(STimingReward o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STimingReward(){
		super();
	}
	public STimingReward(STimingReward arg){
		this.id=arg.id ;
		this.timewait=arg.timewait ;
		this.itemid_1=arg.itemid_1 ;
		this.itemnum_1=arg.itemnum_1 ;
		this.itembind_1=arg.itembind_1 ;
		this.itemid_2=arg.itemid_2 ;
		this.itemnum_2=arg.itemnum_2 ;
		this.itembind_2=arg.itembind_2 ;
		this.itemid_3=arg.itemid_3 ;
		this.itemnum_3=arg.itemnum_3 ;
		this.itembind_3=arg.itembind_3 ;
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
	public int timewait  = 0  ;
	
	public int getTimewait(){
		return this.timewait;
	}
	
	public void setTimewait(int v){
		this.timewait=v;
	}
	
	/**
	 * 
	 */
	public int itemid_1  = 0  ;
	
	public int getItemid_1(){
		return this.itemid_1;
	}
	
	public void setItemid_1(int v){
		this.itemid_1=v;
	}
	
	/**
	 * 
	 */
	public int itemnum_1  = 0  ;
	
	public int getItemnum_1(){
		return this.itemnum_1;
	}
	
	public void setItemnum_1(int v){
		this.itemnum_1=v;
	}
	
	/**
	 * 
	 */
	public int itembind_1  = 0  ;
	
	public int getItembind_1(){
		return this.itembind_1;
	}
	
	public void setItembind_1(int v){
		this.itembind_1=v;
	}
	
	/**
	 * 
	 */
	public int itemid_2  = 0  ;
	
	public int getItemid_2(){
		return this.itemid_2;
	}
	
	public void setItemid_2(int v){
		this.itemid_2=v;
	}
	
	/**
	 * 
	 */
	public int itemnum_2  = 0  ;
	
	public int getItemnum_2(){
		return this.itemnum_2;
	}
	
	public void setItemnum_2(int v){
		this.itemnum_2=v;
	}
	
	/**
	 * 
	 */
	public int itembind_2  = 0  ;
	
	public int getItembind_2(){
		return this.itembind_2;
	}
	
	public void setItembind_2(int v){
		this.itembind_2=v;
	}
	
	/**
	 * 
	 */
	public int itemid_3  = 0  ;
	
	public int getItemid_3(){
		return this.itemid_3;
	}
	
	public void setItemid_3(int v){
		this.itemid_3=v;
	}
	
	/**
	 * 
	 */
	public int itemnum_3  = 0  ;
	
	public int getItemnum_3(){
		return this.itemnum_3;
	}
	
	public void setItemnum_3(int v){
		this.itemnum_3=v;
	}
	
	/**
	 * 
	 */
	public int itembind_3  = 0  ;
	
	public int getItembind_3(){
		return this.itembind_3;
	}
	
	public void setItembind_3(int v){
		this.itembind_3=v;
	}
	
	
};