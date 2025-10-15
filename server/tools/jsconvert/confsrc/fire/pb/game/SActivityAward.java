package fire.pb.game;


public class SActivityAward  extends ActivityAward {

	public int compareTo(SActivityAward o){
		return this.id-o.id;
	}

	
	public SActivityAward(ActivityAward arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SActivityAward(){
		super();
	}
	public SActivityAward(SActivityAward arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};