package fire.pb.map;


public class SFindTreasureResult  extends FindTreasureResult {

	public int compareTo(SFindTreasureResult o){
		return this.id-o.id;
	}

	
	public SFindTreasureResult(FindTreasureResult arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFindTreasureResult(){
		super();
	}
	public SFindTreasureResult(SFindTreasureResult arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};