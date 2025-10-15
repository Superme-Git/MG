package fire.pb.pet;


public class PetSynthesizeSkillRateConfig implements mytools.ConvMain.Checkable ,Comparable<PetSynthesizeSkillRateConfig>{

	public int compareTo(PetSynthesizeSkillRateConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PetSynthesizeSkillRateConfig(){
		super();
	}
	public PetSynthesizeSkillRateConfig(PetSynthesizeSkillRateConfig arg){
		this.id=arg.id ;
		this.value=arg.value ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 技能数量
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
	public int value  = 0  ;
	
	public int getValue(){
		return this.value;
	}
	
	public void setValue(int v){
		this.value=v;
	}
	
	
};