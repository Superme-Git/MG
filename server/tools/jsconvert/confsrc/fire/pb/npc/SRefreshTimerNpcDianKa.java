package fire.pb.npc;


public class SRefreshTimerNpcDianKa  extends RefreshTimerNpc {

	public int compareTo(SRefreshTimerNpcDianKa o){
		return this.id-o.id;
	}

	
	public SRefreshTimerNpcDianKa(RefreshTimerNpc arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRefreshTimerNpcDianKa(){
		super();
	}
	public SRefreshTimerNpcDianKa(SRefreshTimerNpcDianKa arg){
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