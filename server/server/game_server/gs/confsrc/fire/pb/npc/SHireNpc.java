package fire.pb.npc;


public class SHireNpc implements mytools.ConvMain.Checkable ,Comparable<SHireNpc>{

	public int compareTo(SHireNpc o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SHireNpc(){
		super();
	}
	public SHireNpc(SHireNpc arg){
		this.id=arg.id ;
		this.BuffID=arg.BuffID ;
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
	public int BuffID  = 0  ;
	
	public int getBuffID(){
		return this.BuffID;
	}
	
	public void setBuffID(int v){
		this.BuffID=v;
	}
	
	
};