package fire.pb.map;


public class SEventTimerNpcDianKa  extends EventTimerNpc {

	public int compareTo(SEventTimerNpcDianKa o){
		return this.id-o.id;
	}

	
	public SEventTimerNpcDianKa(EventTimerNpc arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEventTimerNpcDianKa(){
		super();
	}
	public SEventTimerNpcDianKa(SEventTimerNpcDianKa arg){
		super(arg);
		this.shoulie=arg.shoulie ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int shoulie  = 0  ;
	
	public int getShoulie(){
		return this.shoulie;
	}
	
	public void setShoulie(int v){
		this.shoulie=v;
	}
	
	
};