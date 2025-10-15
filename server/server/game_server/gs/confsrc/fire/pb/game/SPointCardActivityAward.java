package fire.pb.game;


public class SPointCardActivityAward  extends ActivityAward {

	public int compareTo(SPointCardActivityAward o){
		return this.id-o.id;
	}

	
	public SPointCardActivityAward(ActivityAward arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPointCardActivityAward(){
		super();
	}
	public SPointCardActivityAward(SPointCardActivityAward arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};