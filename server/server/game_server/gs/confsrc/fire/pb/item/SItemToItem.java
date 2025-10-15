package fire.pb.item;


public class SItemToItem implements mytools.ConvMain.Checkable ,Comparable<SItemToItem>{

	public int compareTo(SItemToItem o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SItemToItem(){
		super();
	}
	public SItemToItem(SItemToItem arg){
		this.id=arg.id ;
		this.itemsid=arg.itemsid ;
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
	public java.util.ArrayList<Integer> itemsid  ;
	
	public java.util.ArrayList<Integer> getItemsid(){
		return this.itemsid;
	}
	
	public void setItemsid(java.util.ArrayList<Integer> v){
		this.itemsid=v;
	}
	
	
};