package fire.pb.effect;


public class SAbilityScore implements mytools.ConvMain.Checkable ,Comparable<SAbilityScore>{

	public int compareTo(SAbilityScore o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAbilityScore(){
		super();
	}
	public SAbilityScore(SAbilityScore arg){
		this.id=arg.id ;
		this.ablilityName=arg.ablilityName ;
		this.coefficient=arg.coefficient ;
		this.parameter=arg.parameter ;
		this.formula=arg.formula ;
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
	 * 能力名称
	 */
	public String ablilityName  = null  ;
	
	public String getAblilityName(){
		return this.ablilityName;
	}
	
	public void setAblilityName(String v){
		this.ablilityName=v;
	}
	
	/**
	 * 系数
	 */
	public double coefficient  = 0.0  ;
	
	public double getCoefficient(){
		return this.coefficient;
	}
	
	public void setCoefficient(double v){
		this.coefficient=v;
	}
	
	/**
	 * 参数
	 */
	public String parameter  = null  ;
	
	public String getParameter(){
		return this.parameter;
	}
	
	public void setParameter(String v){
		this.parameter=v;
	}
	
	/**
	 * 公式
	 */
	public String formula  = null  ;
	
	public String getFormula(){
		return this.formula;
	}
	
	public void setFormula(String v){
		this.formula=v;
	}
	
	
};