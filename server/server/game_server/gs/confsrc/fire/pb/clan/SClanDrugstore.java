package fire.pb.clan;


public class SClanDrugstore implements mytools.ConvMain.Checkable ,Comparable<SClanDrugstore>{

	public int compareTo(SClanDrugstore o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SClanDrugstore(){
		super();
	}
	public SClanDrugstore(SClanDrugstore arg){
		this.id=arg.id ;
		this.levelupcost=arg.levelupcost ;
		this.dragnummax=arg.dragnummax ;
		this.doublemoney=arg.doublemoney ;
		this.trimoney=arg.trimoney ;
		this.costeveryday=arg.costeveryday ;
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
	public int levelupcost  = 0  ;
	
	public int getLevelupcost(){
		return this.levelupcost;
	}
	
	public void setLevelupcost(int v){
		this.levelupcost=v;
	}
	
	/**
	 * 
	 */
	public int dragnummax  = 0  ;
	
	public int getDragnummax(){
		return this.dragnummax;
	}
	
	public void setDragnummax(int v){
		this.dragnummax=v;
	}
	
	/**
	 * 
	 */
	public int doublemoney  = 0  ;
	
	public int getDoublemoney(){
		return this.doublemoney;
	}
	
	public void setDoublemoney(int v){
		this.doublemoney=v;
	}
	
	/**
	 * 
	 */
	public int trimoney  = 0  ;
	
	public int getTrimoney(){
		return this.trimoney;
	}
	
	public void setTrimoney(int v){
		this.trimoney=v;
	}
	
	/**
	 * 
	 */
	public int costeveryday  = 0  ;
	
	public int getCosteveryday(){
		return this.costeveryday;
	}
	
	public void setCosteveryday(int v){
		this.costeveryday=v;
	}
	
	
};