package fire.pb.item;


public class SPetSkillSuccessConfig implements mytools.ConvMain.Checkable ,Comparable<SPetSkillSuccessConfig>{

	public int compareTo(SPetSkillSuccessConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPetSkillSuccessConfig(){
		super();
	}
	public SPetSkillSuccessConfig(SPetSkillSuccessConfig arg){
		this.id=arg.id ;
		this.odds=arg.odds ;
		this.basenumber=arg.basenumber ;
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
	public int odds  = 0  ;
	
	public int getOdds(){
		return this.odds;
	}
	
	public void setOdds(int v){
		this.odds=v;
	}
	
	/**
	 * 
	 */
	public int basenumber  = 0  ;
	
	public int getBasenumber(){
		return this.basenumber;
	}
	
	public void setBasenumber(int v){
		this.basenumber=v;
	}
	
	
};