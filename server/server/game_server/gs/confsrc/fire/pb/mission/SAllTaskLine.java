package fire.pb.mission;


public class SAllTaskLine implements mytools.ConvMain.Checkable ,Comparable<SAllTaskLine>{

	public int compareTo(SAllTaskLine o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAllTaskLine(){
		super();
	}
	public SAllTaskLine(SAllTaskLine arg){
		this.id=arg.id ;
		this.职业=arg.职业 ;
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
	public int 职业  = 0  ;
	
	public int get职业(){
		return this.职业;
	}
	
	public void set职业(int v){
		this.职业=v;
	}
	
	
};