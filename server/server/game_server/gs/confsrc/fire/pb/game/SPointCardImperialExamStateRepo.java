package fire.pb.game;


public class SPointCardImperialExamStateRepo  extends ImperialExamStateRepo {

	public int compareTo(SPointCardImperialExamStateRepo o){
		return this.id-o.id;
	}

	
	public SPointCardImperialExamStateRepo(ImperialExamStateRepo arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPointCardImperialExamStateRepo(){
		super();
	}
	public SPointCardImperialExamStateRepo(SPointCardImperialExamStateRepo arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};