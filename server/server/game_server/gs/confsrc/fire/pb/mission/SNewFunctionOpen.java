package fire.pb.mission;


public class SNewFunctionOpen implements mytools.ConvMain.Checkable ,Comparable<SNewFunctionOpen>{

	public int compareTo(SNewFunctionOpen o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNewFunctionOpen(){
		super();
	}
	public SNewFunctionOpen(SNewFunctionOpen arg){
		this.id=arg.id ;
		this.lvtrig=arg.lvtrig ;
		this.taskfinish=arg.taskfinish ;
		this.triggertask=arg.triggertask ;
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
	public int lvtrig  = 0  ;
	
	public int getLvtrig(){
		return this.lvtrig;
	}
	
	public void setLvtrig(int v){
		this.lvtrig=v;
	}
	
	/**
	 * 
	 */
	public String taskfinish  = null  ;
	
	public String getTaskfinish(){
		return this.taskfinish;
	}
	
	public void setTaskfinish(String v){
		this.taskfinish=v;
	}
	
	/**
	 * 
	 */
	public int triggertask  = 0  ;
	
	public int getTriggertask(){
		return this.triggertask;
	}
	
	public void setTriggertask(int v){
		this.triggertask=v;
	}
	
	
};