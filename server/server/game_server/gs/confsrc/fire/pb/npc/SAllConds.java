package fire.pb.npc;


public class SAllConds implements mytools.ConvMain.Checkable ,Comparable<SAllConds>{

	public int compareTo(SAllConds o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAllConds(){
		super();
	}
	public SAllConds(SAllConds arg){
		this.id=arg.id ;
		this.condname=arg.condname ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 服务ID
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
	public String condname  = null  ;
	
	public String getCondname(){
		return this.condname;
	}
	
	public void setCondname(String v){
		this.condname=v;
	}
	
	
};