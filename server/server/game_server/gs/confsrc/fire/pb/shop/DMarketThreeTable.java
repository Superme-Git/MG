package fire.pb.shop;


public class DMarketThreeTable  extends MarketThreeTable {

	public int compareTo(DMarketThreeTable o){
		return this.id-o.id;
	}

	
	public DMarketThreeTable(MarketThreeTable arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DMarketThreeTable(){
		super();
	}
	public DMarketThreeTable(DMarketThreeTable arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};