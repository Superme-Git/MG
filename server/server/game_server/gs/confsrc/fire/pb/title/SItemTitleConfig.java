package fire.pb.title;


public class SItemTitleConfig implements mytools.ConvMain.Checkable ,Comparable<SItemTitleConfig>{

	public int compareTo(SItemTitleConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SItemTitleConfig(){
		super();
	}
	public SItemTitleConfig(SItemTitleConfig arg){
		this.id=arg.id ;
		this.titleID=arg.titleID ;
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
	public int titleID  = 0  ;
	
	public int getTitleID(){
		return this.titleID;
	}
	
	public void setTitleID(int v){
		this.titleID=v;
	}
	
	
};