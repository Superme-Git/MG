package fire.pb.map;


public class SActivityAwardItems implements mytools.ConvMain.Checkable ,Comparable<SActivityAwardItems>{

	public int compareTo(SActivityAwardItems o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SActivityAwardItems(){
		super();
	}
	public SActivityAwardItems(SActivityAwardItems arg){
		this.id=arg.id ;
		this.items=arg.items ;
		this.itemsrate=arg.itemsrate ;
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
	public java.util.ArrayList<Integer> items  ;
	
	public java.util.ArrayList<Integer> getItems(){
		return this.items;
	}
	
	public void setItems(java.util.ArrayList<Integer> v){
		this.items=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> itemsrate  ;
	
	public java.util.ArrayList<Integer> getItemsrate(){
		return this.itemsrate;
	}
	
	public void setItemsrate(java.util.ArrayList<Integer> v){
		this.itemsrate=v;
	}
	
	
};