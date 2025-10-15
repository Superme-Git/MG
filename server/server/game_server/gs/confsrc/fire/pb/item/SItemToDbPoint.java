package fire.pb.item;


public class SItemToDbPoint implements mytools.ConvMain.Checkable ,Comparable<SItemToDbPoint>{

	public int compareTo(SItemToDbPoint o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SItemToDbPoint(){
		super();
	}
	public SItemToDbPoint(SItemToDbPoint arg){
		this.id=arg.id ;
		this.dbpoint=arg.dbpoint ;
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
	public int dbpoint  = 0  ;
	
	public int getDbpoint(){
		return this.dbpoint;
	}
	
	public void setDbpoint(int v){
		this.dbpoint=v;
	}
	
	
};