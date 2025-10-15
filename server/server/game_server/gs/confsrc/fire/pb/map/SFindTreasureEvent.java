package fire.pb.map;


public class SFindTreasureEvent  extends FindTreasureEvent {

	public int compareTo(SFindTreasureEvent o){
		return this.id-o.id;
	}

	
	public SFindTreasureEvent(FindTreasureEvent arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFindTreasureEvent(){
		super();
	}
	public SFindTreasureEvent(SFindTreasureEvent arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};