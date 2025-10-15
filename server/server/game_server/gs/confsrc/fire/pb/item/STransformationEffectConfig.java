package fire.pb.item;


public class STransformationEffectConfig implements mytools.ConvMain.Checkable ,Comparable<STransformationEffectConfig>{

	public int compareTo(STransformationEffectConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STransformationEffectConfig(){
		super();
	}
	public STransformationEffectConfig(STransformationEffectConfig arg){
		this.id=arg.id ;
		this.shapeid=arg.shapeid ;
		this.color=arg.color ;
		this.skills=arg.skills ;
		this.skillfactors=arg.skillfactors ;
		this.skillconstants=arg.skillconstants ;
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
	public int shapeid  = 0  ;
	
	public int getShapeid(){
		return this.shapeid;
	}
	
	public void setShapeid(int v){
		this.shapeid=v;
	}
	
	/**
	 * 
	 */
	public String color  = null  ;
	
	public String getColor(){
		return this.color;
	}
	
	public void setColor(String v){
		this.color=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> skills  ;
	
	public java.util.ArrayList<Integer> getSkills(){
		return this.skills;
	}
	
	public void setSkills(java.util.ArrayList<Integer> v){
		this.skills=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> skillfactors  ;
	
	public java.util.ArrayList<Integer> getSkillfactors(){
		return this.skillfactors;
	}
	
	public void setSkillfactors(java.util.ArrayList<Integer> v){
		this.skillfactors=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> skillconstants  ;
	
	public java.util.ArrayList<Integer> getSkillconstants(){
		return this.skillconstants;
	}
	
	public void setSkillconstants(java.util.ArrayList<Integer> v){
		this.skillconstants=v;
	}
	
	
};