package fire.pb.skill;


public class SLifeSkillCost implements mytools.ConvMain.Checkable ,Comparable<SLifeSkillCost>{

	public int compareTo(SLifeSkillCost o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLifeSkillCost(){
		super();
	}
	public SLifeSkillCost(SLifeSkillCost arg){
		this.id=arg.id ;
		this.needLevelList=arg.needLevelList ;
		this.silverCostList=arg.silverCostList ;
		this.guildContributeCostList=arg.guildContributeCostList ;
		this.strengthCostList=arg.strengthCostList ;
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
	public java.util.ArrayList<Integer> needLevelList  ;
	
	public java.util.ArrayList<Integer> getNeedLevelList(){
		return this.needLevelList;
	}
	
	public void setNeedLevelList(java.util.ArrayList<Integer> v){
		this.needLevelList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> silverCostList  ;
	
	public java.util.ArrayList<Integer> getSilverCostList(){
		return this.silverCostList;
	}
	
	public void setSilverCostList(java.util.ArrayList<Integer> v){
		this.silverCostList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> guildContributeCostList  ;
	
	public java.util.ArrayList<Integer> getGuildContributeCostList(){
		return this.guildContributeCostList;
	}
	
	public void setGuildContributeCostList(java.util.ArrayList<Integer> v){
		this.guildContributeCostList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> strengthCostList  ;
	
	public java.util.ArrayList<Integer> getStrengthCostList(){
		return this.strengthCostList;
	}
	
	public void setStrengthCostList(java.util.ArrayList<Integer> v){
		this.strengthCostList=v;
	}
	
	
};