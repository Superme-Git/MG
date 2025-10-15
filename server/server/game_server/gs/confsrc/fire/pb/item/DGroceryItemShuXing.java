package fire.pb.item;


public class DGroceryItemShuXing  extends GroceryItemShuXing {

	public int compareTo(DGroceryItemShuXing o){
		return this.id-o.id;
	}

	
	public DGroceryItemShuXing(GroceryItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DGroceryItemShuXing(){
		super();
	}
	public DGroceryItemShuXing(DGroceryItemShuXing arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};