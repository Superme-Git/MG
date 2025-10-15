package fire.pb.item;


public class STransformationConfig implements mytools.ConvMain.Checkable ,Comparable<STransformationConfig>{

	public int compareTo(STransformationConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STransformationConfig(){
		super();
	}
	public STransformationConfig(STransformationConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.time=arg.time ;
		this.effectid=arg.effectid ;
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
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 
	 */
	public int time  = 0  ;
	
	public int getTime(){
		return this.time;
	}
	
	public void setTime(int v){
		this.time=v;
	}
	
	/**
	 * 
	 */
	public int effectid  = 0  ;
	
	public int getEffectid(){
		return this.effectid;
	}
	
	public void setEffectid(int v){
		this.effectid=v;
	}
	
	
};