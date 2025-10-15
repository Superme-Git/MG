package fire.pb.item;


public class SCanntInChangKu implements mytools.ConvMain.Checkable ,Comparable<SCanntInChangKu>{

	public int compareTo(SCanntInChangKu o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCanntInChangKu(){
		super();
	}
	public SCanntInChangKu(SCanntInChangKu arg){
		this.id=arg.id ;
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
	
	
};