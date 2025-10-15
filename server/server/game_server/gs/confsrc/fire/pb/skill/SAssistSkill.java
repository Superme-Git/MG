package fire.pb.skill;


public class SAssistSkill implements mytools.ConvMain.Checkable ,Comparable<SAssistSkill>{

	public int compareTo(SAssistSkill o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAssistSkill(){
		super();
	}
	public SAssistSkill(SAssistSkill arg){
		this.id=arg.id ;
		this.isNeedExp=arg.isNeedExp ;
		this.skillName=arg.skillName ;
		this.levelTotal=arg.levelTotal ;
		this.是否在技能学习人处显示=arg.是否在技能学习人处显示 ;
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
	public boolean isNeedExp  = false  ;
	
	public boolean getIsNeedExp(){
		return this.isNeedExp;
	}
	
	public void setIsNeedExp(boolean v){
		this.isNeedExp=v;
	}
	
	/**
	 * 
	 */
	public String skillName  = null  ;
	
	public String getSkillName(){
		return this.skillName;
	}
	
	public void setSkillName(String v){
		this.skillName=v;
	}
	
	/**
	 * 
	 */
	public int levelTotal  = 0  ;
	
	public int getLevelTotal(){
		return this.levelTotal;
	}
	
	public void setLevelTotal(int v){
		this.levelTotal=v;
	}
	
	/**
	 * 
	 */
	public int 是否在技能学习人处显示  = 0  ;
	
	public int get是否在技能学习人处显示(){
		return this.是否在技能学习人处显示;
	}
	
	public void set是否在技能学习人处显示(int v){
		this.是否在技能学习人处显示=v;
	}
	
	
};