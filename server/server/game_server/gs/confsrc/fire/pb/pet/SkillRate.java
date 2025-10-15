package fire.pb.pet;


public class SkillRate implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SkillRate(){
		super();
	}
	public SkillRate(SkillRate arg){
		this.skillid=arg.skillid ;
		this.rate=arg.rate ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int skillid  = 0  ;
	
	public int getSkillid(){
		return this.skillid;
	}
	
	public void setSkillid(int v){
		this.skillid=v;
	}
	
	/**
	 * 
	 */
	public int rate  = 0  ;
	
	public int getRate(){
		return this.rate;
	}
	
	public void setRate(int v){
		this.rate=v;
	}
	
	
};