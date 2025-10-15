package fire.pb.circletask;


public class CircleTaskAutoAccept implements mytools.ConvMain.Checkable ,Comparable<CircleTaskAutoAccept>{

	public int compareTo(CircleTaskAutoAccept o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircleTaskAutoAccept(){
		super();
	}
	public CircleTaskAutoAccept(CircleTaskAutoAccept arg){
		this.id=arg.id ;
		this.circletype=arg.circletype ;
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
	public int circletype  = 0  ;
	
	public int getCircletype(){
		return this.circletype;
	}
	
	public void setCircletype(int v){
		this.circletype=v;
	}
	
	
};