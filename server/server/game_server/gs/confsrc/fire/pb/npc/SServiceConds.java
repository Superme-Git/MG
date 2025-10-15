package fire.pb.npc;


public class SServiceConds implements mytools.ConvMain.Checkable ,Comparable<SServiceConds>{

	public int compareTo(SServiceConds o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SServiceConds(){
		super();
	}
	public SServiceConds(SServiceConds arg){
		this.id=arg.id ;
		this.conditionids=arg.conditionids ;
		this.childservice=arg.childservice ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 服务ID
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
	public java.util.ArrayList<fire.pb.npc.SNpcCond> conditionids  ;
	
	public java.util.ArrayList<fire.pb.npc.SNpcCond> getConditionids(){
		return this.conditionids;
	}
	
	public void setConditionids(java.util.ArrayList<fire.pb.npc.SNpcCond> v){
		this.conditionids=v;
	}
	
	/**
	 * 子服务
	 */
	public java.util.ArrayList<Integer> childservice  ;
	
	public java.util.ArrayList<Integer> getChildservice(){
		return this.childservice;
	}
	
	public void setChildservice(java.util.ArrayList<Integer> v){
		this.childservice=v;
	}
	
	
};