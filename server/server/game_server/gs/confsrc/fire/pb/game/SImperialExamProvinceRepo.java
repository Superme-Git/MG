package fire.pb.game;


public class SImperialExamProvinceRepo  extends ImperialExamProvinceRepo {

	public int compareTo(SImperialExamProvinceRepo o){
		return this.id-o.id;
	}

	
	public SImperialExamProvinceRepo(ImperialExamProvinceRepo arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SImperialExamProvinceRepo(){
		super();
	}
	public SImperialExamProvinceRepo(SImperialExamProvinceRepo arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};