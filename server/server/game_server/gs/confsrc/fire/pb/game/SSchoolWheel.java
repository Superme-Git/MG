package fire.pb.game;


public class SSchoolWheel implements mytools.ConvMain.Checkable ,Comparable<SSchoolWheel>{

	public int compareTo(SSchoolWheel o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSchoolWheel(){
		super();
	}
	public SSchoolWheel(SSchoolWheel arg){
		this.id=arg.id ;
		this.items=arg.items ;
		this.mustitem=arg.mustitem ;
		this.mustnum=arg.mustnum ;
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
	public java.util.ArrayList<String> items  ;
	
	public java.util.ArrayList<String> getItems(){
		return this.items;
	}
	
	public void setItems(java.util.ArrayList<String> v){
		this.items=v;
	}
	
	/**
	 * 
	 */
	public int mustitem  = 0  ;
	
	public int getMustitem(){
		return this.mustitem;
	}
	
	public void setMustitem(int v){
		this.mustitem=v;
	}
	
	/**
	 * 
	 */
	public int mustnum  = 0  ;
	
	public int getMustnum(){
		return this.mustnum;
	}
	
	public void setMustnum(int v){
		this.mustnum=v;
	}
	
	
};