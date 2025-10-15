package fire.pb.skill;


public class SPetSkillitem implements mytools.ConvMain.Checkable ,Comparable<SPetSkillitem>{

	public int compareTo(SPetSkillitem o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPetSkillitem(){
		super();
	}
	public SPetSkillitem(SPetSkillitem arg){
		this.id=arg.id ;
		this.effectid=arg.effectid ;
		this.score=arg.score ;
		this.color=arg.color ;
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
	
	/**
	 * 
	 */
	public int score  = 0  ;
	
	public int getScore(){
		return this.score;
	}
	
	public void setScore(int v){
		this.score=v;
	}
	
	/**
	 * 
	 */
	public int color  = 0  ;
	
	public int getColor(){
		return this.color;
	}
	
	public void setColor(int v){
		this.color=v;
	}
	
	
};