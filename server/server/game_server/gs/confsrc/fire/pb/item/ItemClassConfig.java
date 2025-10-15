package fire.pb.item;


public class ItemClassConfig implements mytools.ConvMain.Checkable ,Comparable<ItemClassConfig>{

	public int compareTo(ItemClassConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public ItemClassConfig(){
		super();
	}
	public ItemClassConfig(ItemClassConfig arg){
		this.id=arg.id ;
		this.classname=arg.classname ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
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
	public String classname  = null  ;
	
	public String getClassname(){
		return this.classname;
	}
	
	public void setClassname(String v){
		this.classname=v;
	}
	
	
};