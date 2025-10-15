package fire.pb.item;


public class DItemClassConfig  extends ItemClassConfig {

	public int compareTo(DItemClassConfig o){
		return this.id-o.id;
	}

	
	public DItemClassConfig(ItemClassConfig arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DItemClassConfig(){
		super();
	}
	public DItemClassConfig(DItemClassConfig arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};