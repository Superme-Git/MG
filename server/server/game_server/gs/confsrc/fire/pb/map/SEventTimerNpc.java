package fire.pb.map;


public class SEventTimerNpc  extends EventTimerNpc {

	public int compareTo(SEventTimerNpc o){
		return this.id-o.id;
	}

	
	public SEventTimerNpc(EventTimerNpc arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEventTimerNpc(){
		super();
	}
	public SEventTimerNpc(SEventTimerNpc arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};