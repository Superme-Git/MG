package fire.pb.game;


public class STransferTable implements mytools.ConvMain.Checkable ,Comparable<STransferTable>{

	public int compareTo(STransferTable o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STransferTable(){
		super();
	}
	public STransferTable(STransferTable arg){
		this.id=arg.id ;
		this.tableName=arg.tableName ;
		this.valueType=arg.valueType ;
		this.keyType=arg.keyType ;
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
	public String tableName  = null  ;
	
	public String getTableName(){
		return this.tableName;
	}
	
	public void setTableName(String v){
		this.tableName=v;
	}
	
	/**
	 * 
	 */
	public String valueType  = null  ;
	
	public String getValueType(){
		return this.valueType;
	}
	
	public void setValueType(String v){
		this.valueType=v;
	}
	
	/**
	 * 
	 */
	public String keyType  = null  ;
	
	public String getKeyType(){
		return this.keyType;
	}
	
	public void setKeyType(String v){
		this.keyType=v;
	}
	
	
};