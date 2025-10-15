package fire.pb.buff;


public class SIBuffConfig implements mytools.ConvMain.Checkable ,Comparable<SIBuffConfig>{

	public int compareTo(SIBuffConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SIBuffConfig(){
		super();
	}
	public SIBuffConfig(SIBuffConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.bufftype=arg.bufftype ;
		this.classname=arg.classname ;
		this.buffclass=arg.buffclass ;
		this.targetBuffs=arg.targetBuffs ;
		this.effects=arg.effects ;
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
	public int bufftype  = 0  ;
	
	public int getBufftype(){
		return this.bufftype;
	}
	
	public void setBufftype(int v){
		this.bufftype=v;
	}
	
	/**
	 * 
	 */
	public String classname  = null  ;
	
	public String getClassname(){
		return this.classname;
	}
	
	public void setClassname(String v){
		this.classname=v;
	}
	
	/**
	 * 
	 */
	public int buffclass  = 0  ;
	
	public int getBuffclass(){
		return this.buffclass;
	}
	
	public void setBuffclass(int v){
		this.buffclass=v;
	}
	
	/**
	 * 
	 */
	public String targetBuffs  = null  ;
	
	public String getTargetBuffs(){
		return this.targetBuffs;
	}
	
	public void setTargetBuffs(String v){
		this.targetBuffs=v;
	}
	
	/**
	 * 
	 */
	public String effects  = null  ;
	
	public String getEffects(){
		return this.effects;
	}
	
	public void setEffects(String v){
		this.effects=v;
	}
	
	
};