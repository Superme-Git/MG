package fire.pb.mission;


public class SGuideCourseDianKa  extends GuideCourse {

	public int compareTo(SGuideCourseDianKa o){
		return this.id-o.id;
	}

	
	public SGuideCourseDianKa(GuideCourse arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SGuideCourseDianKa(){
		super();
	}
	public SGuideCourseDianKa(SGuideCourseDianKa arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};