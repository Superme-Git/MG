package fire.pb.skill;


public class SParticeSkillLevelup implements mytools.ConvMain.Checkable ,Comparable<SParticeSkillLevelup>{

	public int compareTo(SParticeSkillLevelup o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SParticeSkillLevelup(){
		super();
	}
	public SParticeSkillLevelup(SParticeSkillLevelup arg){
		this.id=arg.id ;
		this.vecskillexp=arg.vecskillexp ;
		this.playerlevel=arg.playerlevel ;
		this.factionlevel=arg.factionlevel ;
		this.maxcon=arg.maxcon ;
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
	public java.util.ArrayList<Integer> vecskillexp  ;
	
	public java.util.ArrayList<Integer> getVecskillexp(){
		return this.vecskillexp;
	}
	
	public void setVecskillexp(java.util.ArrayList<Integer> v){
		this.vecskillexp=v;
	}
	
	/**
	 * 
	 */
	public int playerlevel  = 0  ;
	
	public int getPlayerlevel(){
		return this.playerlevel;
	}
	
	public void setPlayerlevel(int v){
		this.playerlevel=v;
	}
	
	/**
	 * 
	 */
	public int factionlevel  = 0  ;
	
	public int getFactionlevel(){
		return this.factionlevel;
	}
	
	public void setFactionlevel(int v){
		this.factionlevel=v;
	}
	
	/**
	 * 
	 */
	public int maxcon  = 0  ;
	
	public int getMaxcon(){
		return this.maxcon;
	}
	
	public void setMaxcon(int v){
		this.maxcon=v;
	}
	
	
};