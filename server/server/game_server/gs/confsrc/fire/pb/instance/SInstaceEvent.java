package fire.pb.instance;


public class SInstaceEvent implements mytools.ConvMain.Checkable ,Comparable<SInstaceEvent>{

	public int compareTo(SInstaceEvent o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInstaceEvent(){
		super();
	}
	public SInstaceEvent(SInstaceEvent arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.type=arg.type ;
		this.thingcondition=arg.thingcondition ;
		this.belongfuben=arg.belongfuben ;
		this.thingresult=arg.thingresult ;
		this.point=arg.point ;
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
	public String thingcondition  = null  ;
	
	public String getThingcondition(){
		return this.thingcondition;
	}
	
	public void setThingcondition(String v){
		this.thingcondition=v;
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
	public String thingresult  = null  ;
	
	public String getThingresult(){
		return this.thingresult;
	}
	
	public void setThingresult(String v){
		this.thingresult=v;
	}
	
	/**
	 * 
	 */
	public String point  = null  ;
	
	public String getPoint(){
		return this.point;
	}
	
	public void setPoint(String v){
		this.point=v;
	}
	
	
};