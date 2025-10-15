package fire.pb.npc;


public class SNpcPreNameRandom implements mytools.ConvMain.Checkable ,Comparable<SNpcPreNameRandom>{

	public int compareTo(SNpcPreNameRandom o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNpcPreNameRandom(){
		super();
	}
	public SNpcPreNameRandom(SNpcPreNameRandom arg){
		this.id=arg.id ;
		this.preName=arg.preName ;
		this.group=arg.group ;
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
	public String preName  = null  ;
	
	public String getPreName(){
		return this.preName;
	}
	
	public void setPreName(String v){
		this.preName=v;
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