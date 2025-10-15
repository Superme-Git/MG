package fire.pb.instance;


public class SInstanceAct implements mytools.ConvMain.Checkable ,Comparable<SInstanceAct>{

	public int compareTo(SInstanceAct o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInstanceAct(){
		super();
	}
	public SInstanceAct(SInstanceAct arg){
		this.id=arg.id ;
		this.type=arg.type ;
		this.parameters=arg.parameters ;
		this.belongfuben=arg.belongfuben ;
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
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 
	 */
	public String parameters  = null  ;
	
	public String getParameters(){
		return this.parameters;
	}
	
	public void setParameters(String v){
		this.parameters=v;
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
	
	
};