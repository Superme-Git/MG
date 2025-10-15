package fire.pb.game;


public class SPointCardImperialExamProvinceRepo  extends ImperialExamProvinceRepo {

	public int compareTo(SPointCardImperialExamProvinceRepo o){
		return this.id-o.id;
	}

	
	public SPointCardImperialExamProvinceRepo(ImperialExamProvinceRepo arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPointCardImperialExamProvinceRepo(){
		super();
	}
	public SPointCardImperialExamProvinceRepo(SPointCardImperialExamProvinceRepo arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};