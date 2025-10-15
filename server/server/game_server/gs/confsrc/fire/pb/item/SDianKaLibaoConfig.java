package fire.pb.item;


public class SDianKaLibaoConfig  extends LibaoConfig {

	public int compareTo(SDianKaLibaoConfig o){
		return this.id-o.id;
	}

	
	public SDianKaLibaoConfig(LibaoConfig arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SDianKaLibaoConfig(){
		super();
	}
	public SDianKaLibaoConfig(SDianKaLibaoConfig arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};