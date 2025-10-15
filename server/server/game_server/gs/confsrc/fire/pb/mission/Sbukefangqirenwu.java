package fire.pb.mission;


public class Sbukefangqirenwu implements mytools.ConvMain.Checkable ,Comparable<Sbukefangqirenwu>{

	public int compareTo(Sbukefangqirenwu o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Sbukefangqirenwu(){
		super();
	}
	public Sbukefangqirenwu(Sbukefangqirenwu arg){
		this.id=arg.id ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 不可放弃的任务的id
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	
};