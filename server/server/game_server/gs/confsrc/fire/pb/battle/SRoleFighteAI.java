package fire.pb.battle;


public class SRoleFighteAI implements mytools.ConvMain.Checkable ,Comparable<SRoleFighteAI>{

	public int compareTo(SRoleFighteAI o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRoleFighteAI(){
		super();
	}
	public SRoleFighteAI(SRoleFighteAI arg){
		this.id=arg.id ;
		this.schoolid=arg.schoolid ;
		this.aiid=arg.aiid ;
		this.effectpoint=arg.effectpoint ;
		this.defaulthave=arg.defaulthave ;
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
	public int schoolid  = 0  ;
	
	public int getSchoolid(){
		return this.schoolid;
	}
	
	public void setSchoolid(int v){
		this.schoolid=v;
	}
	
	/**
	 * 
	 */
	public int aiid  = 0  ;
	
	public int getAiid(){
		return this.aiid;
	}
	
	public void setAiid(int v){
		this.aiid=v;
	}
	
	/**
	 * 
	 */
	public int effectpoint  = 0  ;
	
	public int getEffectpoint(){
		return this.effectpoint;
	}
	
	public void setEffectpoint(int v){
		this.effectpoint=v;
	}
	
	/**
	 * 
	 */
	public int defaulthave  = 0  ;
	
	public int getDefaulthave(){
		return this.defaulthave;
	}
	
	public void setDefaulthave(int v){
		this.defaulthave=v;
	}
	
	
};