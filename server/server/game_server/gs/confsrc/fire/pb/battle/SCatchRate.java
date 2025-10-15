package fire.pb.battle;


public class SCatchRate implements mytools.ConvMain.Checkable ,Comparable<SCatchRate>{

	public int compareTo(SCatchRate o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCatchRate(){
		super();
	}
	public SCatchRate(SCatchRate arg){
		this.id=arg.id ;
		this.upperlimit=arg.upperlimit ;
		this.lowerlimit=arg.lowerlimit ;
		this.catchfactor=arg.catchfactor ;
		this.catchconst=arg.catchconst ;
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
	public int upperlimit  = 0  ;
	
	public int getUpperlimit(){
		return this.upperlimit;
	}
	
	public void setUpperlimit(int v){
		this.upperlimit=v;
	}
	
	/**
	 * 
	 */
	public int lowerlimit  = 0  ;
	
	public int getLowerlimit(){
		return this.lowerlimit;
	}
	
	public void setLowerlimit(int v){
		this.lowerlimit=v;
	}
	
	/**
	 * 
	 */
	public double catchfactor  = 0.0  ;
	
	public double getCatchfactor(){
		return this.catchfactor;
	}
	
	public void setCatchfactor(double v){
		this.catchfactor=v;
	}
	
	/**
	 * 
	 */
	public double catchconst  = 0.0  ;
	
	public double getCatchconst(){
		return this.catchconst;
	}
	
	public void setCatchconst(double v){
		this.catchconst=v;
	}
	
	
};