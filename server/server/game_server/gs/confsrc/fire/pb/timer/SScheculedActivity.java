package fire.pb.timer;


public class SScheculedActivity  extends ScheculedActivity {

	public int compareTo(SScheculedActivity o){
		return this.id-o.id;
	}

	
	public SScheculedActivity(ScheculedActivity arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SScheculedActivity(){
		super();
	}
	public SScheculedActivity(SScheculedActivity arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};