package fire.pb.npc;


public class SActivityNpcPos implements mytools.ConvMain.Checkable ,Comparable<SActivityNpcPos>{

	public int compareTo(SActivityNpcPos o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SActivityNpcPos(){
		super();
	}
	public SActivityNpcPos(SActivityNpcPos arg){
		this.id=arg.id ;
		this.npcPoses=arg.npcPoses ;
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
	public java.util.ArrayList<fire.pb.npc.SNpcPos> npcPoses  ;
	
	public java.util.ArrayList<fire.pb.npc.SNpcPos> getNpcPoses(){
		return this.npcPoses;
	}
	
	public void setNpcPoses(java.util.ArrayList<fire.pb.npc.SNpcPos> v){
		this.npcPoses=v;
	}
	
	
};