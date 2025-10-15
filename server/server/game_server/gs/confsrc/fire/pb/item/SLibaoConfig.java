package fire.pb.item;


public class SLibaoConfig  extends LibaoConfig {

	public int compareTo(SLibaoConfig o){
		return this.id-o.id;
	}

	
	public SLibaoConfig(LibaoConfig arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLibaoConfig(){
		super();
	}
	public SLibaoConfig(SLibaoConfig arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};