package fire.pb.SysConfig;


public class SSysconfig implements mytools.ConvMain.Checkable ,Comparable<SSysconfig>{

	public int compareTo(SSysconfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSysconfig(){
		super();
	}
	public SSysconfig(SSysconfig arg){
		this.id=arg.id ;
		this.defval=arg.defval ;
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
	public int defval  = 0  ;
	
	public int getDefval(){
		return this.defval;
	}
	
	public void setDefval(int v){
		this.defval=v;
	}
	
	
};