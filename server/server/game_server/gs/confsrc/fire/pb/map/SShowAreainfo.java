package fire.pb.map;


public class SShowAreainfo implements mytools.ConvMain.Checkable ,Comparable<SShowAreainfo>{

	public int compareTo(SShowAreainfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SShowAreainfo(){
		super();
	}
	public SShowAreainfo(SShowAreainfo arg){
		this.id=arg.id ;
		this.environment=arg.environment ;
		this.typelevel=arg.typelevel ;
		this.maxnum=arg.maxnum ;
		this.monsters=arg.monsters ;
		this.lootid=arg.lootid ;
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
	public int environment  = 0  ;
	
	public int getEnvironment(){
		return this.environment;
	}
	
	public void setEnvironment(int v){
		this.environment=v;
	}
	
	/**
	 * 
	 */
	public int typelevel  = 0  ;
	
	public int getTypelevel(){
		return this.typelevel;
	}
	
	public void setTypelevel(int v){
		this.typelevel=v;
	}
	
	/**
	 * 
	 */
	public int maxnum  = 0  ;
	
	public int getMaxnum(){
		return this.maxnum;
	}
	
	public void setMaxnum(int v){
		this.maxnum=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> monsters  ;
	
	public java.util.ArrayList<Integer> getMonsters(){
		return this.monsters;
	}
	
	public void setMonsters(java.util.ArrayList<Integer> v){
		this.monsters=v;
	}
	
	/**
	 * 
	 */
	public int lootid  = 0  ;
	
	public int getLootid(){
		return this.lootid;
	}
	
	public void setLootid(int v){
		this.lootid=v;
	}
	
	
};