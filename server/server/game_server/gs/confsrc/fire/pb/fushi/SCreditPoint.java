package fire.pb.fushi;


public class SCreditPoint implements mytools.ConvMain.Checkable ,Comparable<SCreditPoint>{

	public int compareTo(SCreditPoint o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCreditPoint(){
		super();
	}
	public SCreditPoint(SCreditPoint arg){
		this.id=arg.id ;
		this.eventvalue=arg.eventvalue ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 
	 */
	public double eventvalue  = 0.0  ;
	
	public double getEventvalue(){
		return this.eventvalue;
	}
	
	public void setEventvalue(double v){
		this.eventvalue=v;
	}
	
	
};