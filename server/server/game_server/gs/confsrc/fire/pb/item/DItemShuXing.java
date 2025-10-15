package fire.pb.item;


public class DItemShuXing  extends ItemShuXing {

	public int compareTo(DItemShuXing o){
		return this.id-o.id;
	}

	
	public DItemShuXing(ItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DItemShuXing(){
		super();
	}
	public DItemShuXing(DItemShuXing arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};