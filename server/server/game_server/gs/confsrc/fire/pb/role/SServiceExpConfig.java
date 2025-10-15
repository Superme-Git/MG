package fire.pb.role;


public class SServiceExpConfig implements mytools.ConvMain.Checkable ,Comparable<SServiceExpConfig>{

	public int compareTo(SServiceExpConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SServiceExpConfig(){
		super();
	}
	public SServiceExpConfig(SServiceExpConfig arg){
		this.id=arg.id ;
		this.midlevel=arg.midlevel ;
		this.bili=arg.bili ;
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
	public int midlevel  = 0  ;
	
	public int getMidlevel(){
		return this.midlevel;
	}
	
	public void setMidlevel(int v){
		this.midlevel=v;
	}
	
	/**
	 * 
	 */
	public double bili  = 0.0  ;
	
	public double getBili(){
		return this.bili;
	}
	
	public void setBili(double v){
		this.bili=v;
	}
	
	
};