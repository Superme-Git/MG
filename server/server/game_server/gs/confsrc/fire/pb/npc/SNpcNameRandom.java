package fire.pb.npc;


public class SNpcNameRandom implements mytools.ConvMain.Checkable ,Comparable<SNpcNameRandom>{

	public int compareTo(SNpcNameRandom o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNpcNameRandom(){
		super();
	}
	public SNpcNameRandom(SNpcNameRandom arg){
		this.id=arg.id ;
		this.firstName=arg.firstName ;
		this.secondName=arg.secondName ;
		this.group=arg.group ;
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
	public String firstName  = null  ;
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setFirstName(String v){
		this.firstName=v;
	}
	
	/**
	 * 
	 */
	public String secondName  = null  ;
	
	public String getSecondName(){
		return this.secondName;
	}
	
	public void setSecondName(String v){
		this.secondName=v;
	}
	
	/**
	 * 
	 */
	public int group  = 0  ;
	
	public int getGroup(){
		return this.group;
	}
	
	public void setGroup(int v){
		this.group=v;
	}
	
	
};