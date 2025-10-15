package fire.pb.skill;


public class SActionTime implements mytools.ConvMain.Checkable ,Comparable<SActionTime>{

	public int compareTo(SActionTime o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SActionTime(){
		super();
	}
	public SActionTime(SActionTime arg){
		this.id=arg.id ;
		this.actiontime=arg.actiontime ;
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
	public int actiontime  = 0  ;
	
	public int getActiontime(){
		return this.actiontime;
	}
	
	public void setActiontime(int v){
		this.actiontime=v;
	}
	
	
};