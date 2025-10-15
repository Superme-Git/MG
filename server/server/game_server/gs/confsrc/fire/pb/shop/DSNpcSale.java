package fire.pb.shop;


public class DSNpcSale  extends SNpcSale {

	public int compareTo(DSNpcSale o){
		return this.id-o.id;
	}

	
	public DSNpcSale(SNpcSale arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DSNpcSale(){
		super();
	}
	public DSNpcSale(DSNpcSale arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};