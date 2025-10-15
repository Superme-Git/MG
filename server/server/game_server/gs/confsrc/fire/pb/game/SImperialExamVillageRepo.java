package fire.pb.game;


public class SImperialExamVillageRepo  extends ImperialExamVillageRepo {

	public int compareTo(SImperialExamVillageRepo o){
		return this.id-o.id;
	}

	
	public SImperialExamVillageRepo(ImperialExamVillageRepo arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SImperialExamVillageRepo(){
		super();
	}
	public SImperialExamVillageRepo(SImperialExamVillageRepo arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};