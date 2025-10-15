package fire.pb.map;


public class SFindTreasureEventDianKa  extends FindTreasureEvent {

	public int compareTo(SFindTreasureEventDianKa o){
		return this.id-o.id;
	}

	
	public SFindTreasureEventDianKa(FindTreasureEvent arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFindTreasureEventDianKa(){
		super();
	}
	public SFindTreasureEventDianKa(SFindTreasureEventDianKa arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};