package fire.pb.item;


public class Sanquan implements mytools.ConvMain.Checkable ,Comparable<Sanquan>{

	public int compareTo(Sanquan o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Sanquan(){
		super();
	}
	public Sanquan(Sanquan arg){
		this.id=arg.id ;
		this.wpname=arg.wpname ;
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
	public String wpname  = null  ;
	
	public String getWpname(){
		return this.wpname;
	}
	
	public void setWpname(String v){
		this.wpname=v;
	}
	
	
};