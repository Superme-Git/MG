package fire.pb.shop;


public class DMarketFirstTable  extends MarketFirstTable {

	public int compareTo(DMarketFirstTable o){
		return this.id-o.id;
	}

	
	public DMarketFirstTable(MarketFirstTable arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DMarketFirstTable(){
		super();
	}
	public DMarketFirstTable(DMarketFirstTable arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};