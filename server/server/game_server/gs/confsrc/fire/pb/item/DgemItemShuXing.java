package fire.pb.item;


public class DgemItemShuXing  extends gemItemShuXing {

	public int compareTo(DgemItemShuXing o){
		return this.id-o.id;
	}

	
	public DgemItemShuXing(gemItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DgemItemShuXing(){
		super();
	}
	public DgemItemShuXing(DgemItemShuXing arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};