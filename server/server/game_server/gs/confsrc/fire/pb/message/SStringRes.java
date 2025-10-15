package fire.pb.message;


public class SStringRes implements mytools.ConvMain.Checkable ,Comparable<SStringRes>{

	public int compareTo(SStringRes o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SStringRes(){
		super();
	}
	public SStringRes(SStringRes arg){
		this.id=arg.id ;
		this.msg=arg.msg ;
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
	public String msg  = null  ;
	
	public String getMsg(){
		return this.msg;
	}
	
	public void setMsg(String v){
		this.msg=v;
	}
	
	
};