package fire.pb.game;


public class SPointCardImperialExamVillageRepo  extends ImperialExamVillageRepo {

	public int compareTo(SPointCardImperialExamVillageRepo o){
		return this.id-o.id;
	}

	
	public SPointCardImperialExamVillageRepo(ImperialExamVillageRepo arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPointCardImperialExamVillageRepo(){
		super();
	}
	public SPointCardImperialExamVillageRepo(SPointCardImperialExamVillageRepo arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};