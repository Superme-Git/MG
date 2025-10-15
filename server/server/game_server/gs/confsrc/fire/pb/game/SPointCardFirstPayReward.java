package fire.pb.game;


public class SPointCardFirstPayReward  extends SFirstPayReward {

	public int compareTo(SPointCardFirstPayReward o){
		return this.id-o.id;
	}

	
	public SPointCardFirstPayReward(SFirstPayReward arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPointCardFirstPayReward(){
		super();
	}
	public SPointCardFirstPayReward(SPointCardFirstPayReward arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};