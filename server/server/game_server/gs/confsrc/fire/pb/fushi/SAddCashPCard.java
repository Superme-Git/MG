package fire.pb.fushi;


public class SAddCashPCard  extends ChargeConfig {

	public int compareTo(SAddCashPCard o){
		return this.id-o.id;
	}

	
	public SAddCashPCard(ChargeConfig arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAddCashPCard(){
		super();
	}
	public SAddCashPCard(SAddCashPCard arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};