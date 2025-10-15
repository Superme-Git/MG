package fire.pb.game;


public class SImperialExamStateRepo  extends ImperialExamStateRepo {

	public int compareTo(SImperialExamStateRepo o){
		return this.id-o.id;
	}

	
	public SImperialExamStateRepo(ImperialExamStateRepo arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SImperialExamStateRepo(){
		super();
	}
	public SImperialExamStateRepo(SImperialExamStateRepo arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};