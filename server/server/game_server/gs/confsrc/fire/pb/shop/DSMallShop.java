package fire.pb.shop;


public class DSMallShop  extends SMallShop {

	public int compareTo(DSMallShop o){
		return this.id-o.id;
	}

	
	public DSMallShop(SMallShop arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DSMallShop(){
		super();
	}
	public DSMallShop(DSMallShop arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};