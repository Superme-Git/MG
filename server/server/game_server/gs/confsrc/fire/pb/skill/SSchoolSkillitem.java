package fire.pb.skill;


public class SSchoolSkillitem implements mytools.ConvMain.Checkable ,Comparable<SSchoolSkillitem>{

	public int compareTo(SSchoolSkillitem o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSchoolSkillitem(){
		super();
	}
	public SSchoolSkillitem(SSchoolSkillitem arg){
		this.id=arg.id ;
		this.effectid=arg.effectid ;
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
	public int effectid  = 0  ;
	
	public int getEffectid(){
		return this.effectid;
	}
	
	public void setEffectid(int v){
		this.effectid=v;
	}
	
	
};