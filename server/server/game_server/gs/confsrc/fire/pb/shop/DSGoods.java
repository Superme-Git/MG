package fire.pb.shop;


public class DSGoods  extends SGoods {

	public int compareTo(DSGoods o){
		return this.id-o.id;
	}

	
	public DSGoods(SGoods arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DSGoods(){
		super();
	}
	public DSGoods(DSGoods arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};