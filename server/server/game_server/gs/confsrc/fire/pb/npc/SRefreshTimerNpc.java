package fire.pb.npc;


public class SRefreshTimerNpc  extends RefreshTimerNpc {

	public int compareTo(SRefreshTimerNpc o){
		return this.id-o.id;
	}

	
	public SRefreshTimerNpc(RefreshTimerNpc arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRefreshTimerNpc(){
		super();
	}
	public SRefreshTimerNpc(SRefreshTimerNpc arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};