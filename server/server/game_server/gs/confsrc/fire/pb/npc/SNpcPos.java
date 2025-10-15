package fire.pb.npc;


public class SNpcPos implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNpcPos(){
		super();
	}
	public SNpcPos(SNpcPos arg){
		this.npcid=arg.npcid ;
		this.npcName=arg.npcName ;
		this.npcMap=arg.npcMap ;
		this.npcPosx=arg.npcPosx ;
		this.npcPosy=arg.npcPosy ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int npcid  = 0  ;
	
	public int getNpcid(){
		return this.npcid;
	}
	
	public void setNpcid(int v){
		this.npcid=v;
	}
	
	/**
	 * 
	 */
	public String npcName  = null  ;
	
	public String getNpcName(){
		return this.npcName;
	}
	
	public void setNpcName(String v){
		this.npcName=v;
	}
	
	/**
	 * 
	 */
	public int npcMap  = 0  ;
	
	public int getNpcMap(){
		return this.npcMap;
	}
	
	public void setNpcMap(int v){
		this.npcMap=v;
	}
	
	/**
	 * 
	 */
	public int npcPosx  = 0  ;
	
	public int getNpcPosx(){
		return this.npcPosx;
	}
	
	public void setNpcPosx(int v){
		this.npcPosx=v;
	}
	
	/**
	 * 
	 */
	public int npcPosy  = 0  ;
	
	public int getNpcPosy(){
		return this.npcPosy;
	}
	
	public void setNpcPosy(int v){
		this.npcPosy=v;
	}
	
	
};