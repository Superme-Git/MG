package fire.pb.clan;


public class SClanHotel implements mytools.ConvMain.Checkable ,Comparable<SClanHotel>{

	public int compareTo(SClanHotel o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SClanHotel(){
		super();
	}
	public SClanHotel(SClanHotel arg){
		this.id=arg.id ;
		this.levelupcost=arg.levelupcost ;
		this.peoplemax=arg.peoplemax ;
		this.apprenticemax=arg.apprenticemax ;
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
	public int peoplemax  = 0  ;
	
	public int getPeoplemax(){
		return this.peoplemax;
	}
	
	public void setPeoplemax(int v){
		this.peoplemax=v;
	}
	
	/**
	 * 
	 */
	public int apprenticemax  = 0  ;
	
	public int getApprenticemax(){
		return this.apprenticemax;
	}
	
	public void setApprenticemax(int v){
		this.apprenticemax=v;
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