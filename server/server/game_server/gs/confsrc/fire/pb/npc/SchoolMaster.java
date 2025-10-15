package fire.pb.npc;


public class SchoolMaster implements mytools.ConvMain.Checkable ,Comparable<SchoolMaster>{

	public int compareTo(SchoolMaster o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SchoolMaster(){
		super();
	}
	public SchoolMaster(SchoolMaster arg){
		this.id=arg.id ;
		this.masterid=arg.masterid ;
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
	public int masterid  = 0  ;
	
	public int getMasterid(){
		return this.masterid;
	}
	
	public void setMasterid(int v){
		this.masterid=v;
	}
	
	
};