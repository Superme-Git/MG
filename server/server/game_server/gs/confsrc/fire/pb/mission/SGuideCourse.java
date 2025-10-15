package fire.pb.mission;


public class SGuideCourse  extends GuideCourse {

	public int compareTo(SGuideCourse o){
		return this.id-o.id;
	}

	
	public SGuideCourse(GuideCourse arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SGuideCourse(){
		super();
	}
	public SGuideCourse(SGuideCourse arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};