package fire.pb.mission;


public class SActiveChestConfig4D implements mytools.ConvMain.Checkable ,Comparable<SActiveChestConfig4D>{

	public int compareTo(SActiveChestConfig4D o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SActiveChestConfig4D(){
		super();
	}
	public SActiveChestConfig4D(SActiveChestConfig4D arg){
		this.id=arg.id ;
		this.activeness=arg.activeness ;
		this.itemid=arg.itemid ;
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
	public int activeness  = 0  ;
	
	public int getActiveness(){
		return this.activeness;
	}
	
	public void setActiveness(int v){
		this.activeness=v;
	}
	
	/**
	 * 
	 */
	public int itemid  = 0  ;
	
	public int getItemid(){
		return this.itemid;
	}
	
	public void setItemid(int v){
		this.itemid=v;
	}
	
	
};