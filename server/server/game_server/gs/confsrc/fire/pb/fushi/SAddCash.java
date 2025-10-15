package fire.pb.fushi;


public class SAddCash  extends ChargeConfig {

	public int compareTo(SAddCash o){
		return this.id-o.id;
	}

	
	public SAddCash(ChargeConfig arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAddCash(){
		super();
	}
	public SAddCash(SAddCash arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};