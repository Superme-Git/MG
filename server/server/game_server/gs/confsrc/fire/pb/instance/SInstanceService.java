package fire.pb.instance;


public class SInstanceService implements mytools.ConvMain.Checkable ,Comparable<SInstanceService>{

	public int compareTo(SInstanceService o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInstanceService(){
		super();
	}
	public SInstanceService(SInstanceService arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.belongfuben=arg.belongfuben ;
		this.friststate=arg.friststate ;
		this.changestate=arg.changestate ;
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
	public int belongfuben  = 0  ;
	
	public int getBelongfuben(){
		return this.belongfuben;
	}
	
	public void setBelongfuben(int v){
		this.belongfuben=v;
	}
	
	/**
	 * 
	 */
	public int friststate  = 0  ;
	
	public int getFriststate(){
		return this.friststate;
	}
	
	public void setFriststate(int v){
		this.friststate=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> changestate  ;
	
	public java.util.ArrayList<String> getChangestate(){
		return this.changestate;
	}
	
	public void setChangestate(java.util.ArrayList<String> v){
		this.changestate=v;
	}
	
	
};