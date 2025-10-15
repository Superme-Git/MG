package fire.pb.battle;


public class SFormationConfig implements mytools.ConvMain.Checkable ,Comparable<SFormationConfig>{

	public int compareTo(SFormationConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFormationConfig(){
		super();
	}
	public SFormationConfig(SFormationConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.positions=arg.positions ;
		this.suppress1=arg.suppress1 ;
		this.suppressparam1=arg.suppressparam1 ;
		this.suppress2=arg.suppress2 ;
		this.suppressparam2=arg.suppressparam2 ;
		this.matchid=arg.matchid ;
		this.matchidaddexp=arg.matchidaddexp ;
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
	public java.util.ArrayList<Integer> positions  ;
	
	public java.util.ArrayList<Integer> getPositions(){
		return this.positions;
	}
	
	public void setPositions(java.util.ArrayList<Integer> v){
		this.positions=v;
	}
	
	/**
	 * 
	 */
	public String suppress1  = null  ;
	
	public String getSuppress1(){
		return this.suppress1;
	}
	
	public void setSuppress1(String v){
		this.suppress1=v;
	}
	
	/**
	 * 
	 */
	public String suppressparam1  = null  ;
	
	public String getSuppressparam1(){
		return this.suppressparam1;
	}
	
	public void setSuppressparam1(String v){
		this.suppressparam1=v;
	}
	
	/**
	 * 
	 */
	public String suppress2  = null  ;
	
	public String getSuppress2(){
		return this.suppress2;
	}
	
	public void setSuppress2(String v){
		this.suppress2=v;
	}
	
	/**
	 * 
	 */
	public String suppressparam2  = null  ;
	
	public String getSuppressparam2(){
		return this.suppressparam2;
	}
	
	public void setSuppressparam2(String v){
		this.suppressparam2=v;
	}
	
	/**
	 * 
	 */
	public int matchid  = 0  ;
	
	public int getMatchid(){
		return this.matchid;
	}
	
	public void setMatchid(int v){
		this.matchid=v;
	}
	
	/**
	 * 
	 */
	public int matchidaddexp  = 0  ;
	
	public int getMatchidaddexp(){
		return this.matchidaddexp;
	}
	
	public void setMatchidaddexp(int v){
		this.matchidaddexp=v;
	}
	
	
};