package fire.pb.pet;


public class PetLearnSkillRateConfig implements mytools.ConvMain.Checkable ,Comparable<PetLearnSkillRateConfig>{

	public int compareTo(PetLearnSkillRateConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PetLearnSkillRateConfig(){
		super();
	}
	public PetLearnSkillRateConfig(PetLearnSkillRateConfig arg){
		this.id=arg.id ;
		this.rate=arg.rate ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 当前技能数量
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
	public double rate  = 0.0  ;
	
	public double getRate(){
		return this.rate;
	}
	
	public void setRate(double v){
		this.rate=v;
	}
	
	
};