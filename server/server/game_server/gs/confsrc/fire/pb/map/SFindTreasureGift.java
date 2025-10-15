package fire.pb.map;


public class SFindTreasureGift  extends FindTreasureGift {

	public int compareTo(SFindTreasureGift o){
		return this.id-o.id;
	}

	
	public SFindTreasureGift(FindTreasureGift arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFindTreasureGift(){
		super();
	}
	public SFindTreasureGift(SFindTreasureGift arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};