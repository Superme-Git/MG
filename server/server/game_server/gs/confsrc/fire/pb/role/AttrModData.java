package fire.pb.role;


public class AttrModData implements mytools.ConvMain.Checkable ,Comparable<AttrModData>{

	public int compareTo(AttrModData o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public AttrModData(){
		super();
	}
	public AttrModData(AttrModData arg){
		this.id=arg.id ;
		this.consfactor=arg.consfactor ;
		this.iqfactor=arg.iqfactor ;
		this.strfactor=arg.strfactor ;
		this.endufactor=arg.endufactor ;
		this.agifactor=arg.agifactor ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 二级属性ID
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
	public double consfactor  = 0.0  ;
	
	public double getConsfactor(){
		return this.consfactor;
	}
	
	public void setConsfactor(double v){
		this.consfactor=v;
	}
	
	/**
	 * 
	 */
	public double iqfactor  = 0.0  ;
	
	public double getIqfactor(){
		return this.iqfactor;
	}
	
	public void setIqfactor(double v){
		this.iqfactor=v;
	}
	
	/**
	 * 
	 */
	public double strfactor  = 0.0  ;
	
	public double getStrfactor(){
		return this.strfactor;
	}
	
	public void setStrfactor(double v){
		this.strfactor=v;
	}
	
	/**
	 * 
	 */
	public double endufactor  = 0.0  ;
	
	public double getEndufactor(){
		return this.endufactor;
	}
	
	public void setEndufactor(double v){
		this.endufactor=v;
	}
	
	/**
	 * 
	 */
	public double agifactor  = 0.0  ;
	
	public double getAgifactor(){
		return this.agifactor;
	}
	
	public void setAgifactor(double v){
		this.agifactor=v;
	}
	
	
};