package fire.pb.timer;


public class SScheculedActivityDianKa  extends ScheculedActivity {

	public int compareTo(SScheculedActivityDianKa o){
		return this.id-o.id;
	}

	
	public SScheculedActivityDianKa(ScheculedActivity arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SScheculedActivityDianKa(){
		super();
	}
	public SScheculedActivityDianKa(SScheculedActivityDianKa arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};