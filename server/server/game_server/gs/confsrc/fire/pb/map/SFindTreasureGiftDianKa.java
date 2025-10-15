package fire.pb.map;


public class SFindTreasureGiftDianKa  extends FindTreasureGift {

	public int compareTo(SFindTreasureGiftDianKa o){
		return this.id-o.id;
	}

	
	public SFindTreasureGiftDianKa(FindTreasureGift arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFindTreasureGiftDianKa(){
		super();
	}
	public SFindTreasureGiftDianKa(SFindTreasureGiftDianKa arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};