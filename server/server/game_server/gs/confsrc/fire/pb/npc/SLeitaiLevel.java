package fire.pb.npc;


public class SLeitaiLevel implements mytools.ConvMain.Checkable ,Comparable<SLeitaiLevel>{

	public int compareTo(SLeitaiLevel o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLeitaiLevel(){
		super();
	}
	public SLeitaiLevel(SLeitaiLevel arg){
		this.id=arg.id ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
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
	public int levelmin  = 0  ;
	
	public int getLevelmin(){
		return this.levelmin;
	}
	
	public void setLevelmin(int v){
		this.levelmin=v;
	}
	
	/**
	 * 
	 */
	public int levelmax  = 0  ;
	
	public int getLevelmax(){
		return this.levelmax;
	}
	
	public void setLevelmax(int v){
		this.levelmax=v;
	}
	
	
};