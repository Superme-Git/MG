package fire.pb.pet;


public class PetColumnConfig implements mytools.ConvMain.Checkable ,Comparable<PetColumnConfig>{

	public int compareTo(PetColumnConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PetColumnConfig(){
		super();
	}
	public PetColumnConfig(PetColumnConfig arg){
		this.id=arg.id ;
		this.initsize=arg.initsize ;
		this.tablename=arg.tablename ;
		this.maxsize=arg.maxsize ;
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
	public int initsize  = 0  ;
	
	public int getInitsize(){
		return this.initsize;
	}
	
	public void setInitsize(int v){
		this.initsize=v;
	}
	
	/**
	 * 数据库表名
	 */
	public String tablename  = null  ;
	
	public String getTablename(){
		return this.tablename;
	}
	
	public void setTablename(String v){
		this.tablename=v;
	}
	
	/**
	 * 
	 */
	public int maxsize  = 0  ;
	
	public int getMaxsize(){
		return this.maxsize;
	}
	
	public void setMaxsize(int v){
		this.maxsize=v;
	}
	
	
};