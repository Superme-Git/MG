package fire.pb.npc;


public class SMonsterSkill implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SMonsterSkill(){
		super();
	}
	public SMonsterSkill(SMonsterSkill arg){
		this.skillid=arg.skillid ;
		this.showrate=arg.showrate ;
		this.castrate=arg.castrate ;
		this.levelfactor=arg.levelfactor ;
		this.levelconstant=arg.levelconstant ;
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
	public int showrate  = 0  ;
	
	public int getShowrate(){
		return this.showrate;
	}
	
	public void setShowrate(int v){
		this.showrate=v;
	}
	
	/**
	 * 
	 */
	public int castrate  = 0  ;
	
	public int getCastrate(){
		return this.castrate;
	}
	
	public void setCastrate(int v){
		this.castrate=v;
	}
	
	/**
	 * 
	 */
	public double levelfactor  = 0.0  ;
	
	public double getLevelfactor(){
		return this.levelfactor;
	}
	
	public void setLevelfactor(double v){
		this.levelfactor=v;
	}
	
	/**
	 * 
	 */
	public double levelconstant  = 0.0  ;
	
	public double getLevelconstant(){
		return this.levelconstant;
	}
	
	public void setLevelconstant(double v){
		this.levelconstant=v;
	}
	
	
};