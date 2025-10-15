package fire.pb.game;


public class STransferRoleTable implements mytools.ConvMain.Checkable ,Comparable<STransferRoleTable>{

	public int compareTo(STransferRoleTable o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STransferRoleTable(){
		super();
	}
	public STransferRoleTable(STransferRoleTable arg){
		this.id=arg.id ;
		this.tableName=arg.tableName ;
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
	
	
};