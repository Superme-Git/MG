package fire.pb.circletask;


public class CircTaskMailNpc implements mytools.ConvMain.Checkable ,Comparable<CircTaskMailNpc>{

	public int compareTo(CircTaskMailNpc o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskMailNpc(){
		super();
	}
	public CircTaskMailNpc(CircTaskMailNpc arg){
		this.id=arg.id ;
		this.ctgroup=arg.ctgroup ;
		this.school=arg.school ;
		this.npc=arg.npc ;
		this.serviceid=arg.serviceid ;
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
	public int ctgroup  = 0  ;
	
	public int getCtgroup(){
		return this.ctgroup;
	}
	
	public void setCtgroup(int v){
		this.ctgroup=v;
	}
	
	/**
	 * 
	 */
	public int school  = 0  ;
	
	public int getSchool(){
		return this.school;
	}
	
	public void setSchool(int v){
		this.school=v;
	}
	
	/**
	 * 
	 */
	public String npc  = null  ;
	
	public String getNpc(){
		return this.npc;
	}
	
	public void setNpc(String v){
		this.npc=v;
	}
	
	/**
	 * 
	 */
	public int serviceid  = 0  ;
	
	public int getServiceid(){
		return this.serviceid;
	}
	
	public void setServiceid(int v){
		this.serviceid=v;
	}
	
	
};