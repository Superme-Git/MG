package fire.pb.map;


public class FindTreasureResult implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FindTreasureResult(){
		super();
	}
	public FindTreasureResult(FindTreasureResult arg){
		this.id=arg.id ;
		this.itemId=arg.itemId ;
		this.awardId=arg.awardId ;
		this.group=arg.group ;
		this.minlevel=arg.minlevel ;
		this.maxlevel=arg.maxlevel ;
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
	public int itemId  = 0  ;
	
	public int getItemId(){
		return this.itemId;
	}
	
	public void setItemId(int v){
		this.itemId=v;
	}
	
	/**
	 * 
	 */
	public String awardId  = null  ;
	
	public String getAwardId(){
		return this.awardId;
	}
	
	public void setAwardId(String v){
		this.awardId=v;
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
	
	/**
	 * 
	 */
	public int minlevel  = 0  ;
	
	public int getMinlevel(){
		return this.minlevel;
	}
	
	public void setMinlevel(int v){
		this.minlevel=v;
	}
	
	/**
	 * 
	 */
	public int maxlevel  = 0  ;
	
	public int getMaxlevel(){
		return this.maxlevel;
	}
	
	public void setMaxlevel(int v){
		this.maxlevel=v;
	}
	
	
};