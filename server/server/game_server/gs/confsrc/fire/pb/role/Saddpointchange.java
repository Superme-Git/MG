package fire.pb.role;


public class Saddpointchange implements mytools.ConvMain.Checkable ,Comparable<Saddpointchange>{

	public int compareTo(Saddpointchange o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Saddpointchange(){
		super();
	}
	public Saddpointchange(Saddpointchange arg){
		this.id=arg.id ;
		this.consume=arg.consume ;
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
	public int consume  = 0  ;
	
	public int getConsume(){
		return this.consume;
	}
	
	public void setConsume(int v){
		this.consume=v;
	}
	
	
};