package fire.pb.effect;


public class SPetAbilityScore implements mytools.ConvMain.Checkable ,Comparable<SPetAbilityScore>{

	public int compareTo(SPetAbilityScore o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPetAbilityScore(){
		super();
	}
	public SPetAbilityScore(SPetAbilityScore arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.score=arg.score ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 资质编号
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 资质名称
	 */
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 对应评分
	 */
	public double score  = 0.0  ;
	
	public double getScore(){
		return this.score;
	}
	
	public void setScore(double v){
		this.score=v;
	}
	
	
};