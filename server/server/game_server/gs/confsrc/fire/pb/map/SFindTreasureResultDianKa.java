package fire.pb.map;


public class SFindTreasureResultDianKa  extends FindTreasureResult {

	public int compareTo(SFindTreasureResultDianKa o){
		return this.id-o.id;
	}

	
	public SFindTreasureResultDianKa(FindTreasureResult arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFindTreasureResultDianKa(){
		super();
	}
	public SFindTreasureResultDianKa(SFindTreasureResultDianKa arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};