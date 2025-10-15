package fire.pb.instance;


public class SInstanceChildSave implements mytools.ConvMain.Checkable ,Comparable<SInstanceChildSave>{

	public int compareTo(SInstanceChildSave o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInstanceChildSave(){
		super();
	}
	public SInstanceChildSave(SInstanceChildSave arg){
		this.id=arg.id ;
		this.belongfuben=arg.belongfuben ;
		this.belongjindu=arg.belongjindu ;
		this.childrenjindu=arg.childrenjindu ;
		this.YNchildrenjindu=arg.YNchildrenjindu ;
		this.num=arg.num ;
		this.eachpoint=arg.eachpoint ;
		this.finishpoint=arg.finishpoint ;
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
	public int belongjindu  = 0  ;
	
	public int getBelongjindu(){
		return this.belongjindu;
	}
	
	public void setBelongjindu(int v){
		this.belongjindu=v;
	}
	
	/**
	 * 
	 */
	public int childrenjindu  = 0  ;
	
	public int getChildrenjindu(){
		return this.childrenjindu;
	}
	
	public void setChildrenjindu(int v){
		this.childrenjindu=v;
	}
	
	/**
	 * 
	 */
	public String YNchildrenjindu  = null  ;
	
	public String getYNchildrenjindu(){
		return this.YNchildrenjindu;
	}
	
	public void setYNchildrenjindu(String v){
		this.YNchildrenjindu=v;
	}
	
	/**
	 * 
	 */
	public int num  = 0  ;
	
	public int getNum(){
		return this.num;
	}
	
	public void setNum(int v){
		this.num=v;
	}
	
	/**
	 * 
	 */
	public String eachpoint  = null  ;
	
	public String getEachpoint(){
		return this.eachpoint;
	}
	
	public void setEachpoint(String v){
		this.eachpoint=v;
	}
	
	/**
	 * 
	 */
	public String finishpoint  = null  ;
	
	public String getFinishpoint(){
		return this.finishpoint;
	}
	
	public void setFinishpoint(String v){
		this.finishpoint=v;
	}
	
	
};