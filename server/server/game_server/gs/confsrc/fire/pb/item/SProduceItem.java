package fire.pb.item;


public class SProduceItem implements mytools.ConvMain.Checkable ,Comparable<SProduceItem>{

	public int compareTo(SProduceItem o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SProduceItem(){
		super();
	}
	public SProduceItem(SProduceItem arg){
		this.id=arg.id ;
		this.level=arg.level ;
		this.itemList=arg.itemList ;
		this.itemNumList=arg.itemNumList ;
		this.resultItem=arg.resultItem ;
		this.resultItemRate=arg.resultItemRate ;
		this.money=arg.money ;
		this.itemNotEnoughMsg=arg.itemNotEnoughMsg ;
		this.notice=arg.notice ;
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
	public java.util.ArrayList<Integer> itemList  ;
	
	public java.util.ArrayList<Integer> getItemList(){
		return this.itemList;
	}
	
	public void setItemList(java.util.ArrayList<Integer> v){
		this.itemList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> itemNumList  ;
	
	public java.util.ArrayList<Integer> getItemNumList(){
		return this.itemNumList;
	}
	
	public void setItemNumList(java.util.ArrayList<Integer> v){
		this.itemNumList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> resultItem  ;
	
	public java.util.ArrayList<Integer> getResultItem(){
		return this.resultItem;
	}
	
	public void setResultItem(java.util.ArrayList<Integer> v){
		this.resultItem=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> resultItemRate  ;
	
	public java.util.ArrayList<Integer> getResultItemRate(){
		return this.resultItemRate;
	}
	
	public void setResultItemRate(java.util.ArrayList<Integer> v){
		this.resultItemRate=v;
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
	public int itemNotEnoughMsg  = 0  ;
	
	public int getItemNotEnoughMsg(){
		return this.itemNotEnoughMsg;
	}
	
	public void setItemNotEnoughMsg(int v){
		this.itemNotEnoughMsg=v;
	}
	
	/**
	 * 
	 */
	public int notice  = 0  ;
	
	public int getNotice(){
		return this.notice;
	}
	
	public void setNotice(int v){
		this.notice=v;
	}
	
	
};