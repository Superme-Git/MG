package fire.pb.circletask;


public class CircTaskPetCatch implements mytools.ConvMain.Checkable ,Comparable<CircTaskPetCatch>{

	public int compareTo(CircTaskPetCatch o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskPetCatch(){
		super();
	}
	public CircTaskPetCatch(CircTaskPetCatch arg){
		this.id=arg.id ;
		this.ctgroup=arg.ctgroup ;
		this.school=arg.school ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.recycleitem=arg.recycleitem ;
		this.itemnum=arg.itemnum ;
		this.shopnpc=arg.shopnpc ;
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
	public int ctgroup  = 0  ;
	
	public int getCtgroup(){
		return this.ctgroup;
	}
	
	public void setCtgroup(int v){
		this.ctgroup=v;
	}
	
	/**
	 * 
	 */
	public int school  = 0  ;
	
	public int getSchool(){
		return this.school;
	}
	
	public void setSchool(int v){
		this.school=v;
	}
	
	/**
	 * 
	 */
	public int levelmin  = 0  ;
	
	public int getLevelmin(){
		return this.levelmin;
	}
	
	public void setLevelmin(int v){
		this.levelmin=v;
	}
	
	/**
	 * 
	 */
	public int levelmax  = 0  ;
	
	public int getLevelmax(){
		return this.levelmax;
	}
	
	public void setLevelmax(int v){
		this.levelmax=v;
	}
	
	/**
	 * 
	 */
	public String recycleitem  = null  ;
	
	public String getRecycleitem(){
		return this.recycleitem;
	}
	
	public void setRecycleitem(String v){
		this.recycleitem=v;
	}
	
	/**
	 * 
	 */
	public int itemnum  = 0  ;
	
	public int getItemnum(){
		return this.itemnum;
	}
	
	public void setItemnum(int v){
		this.itemnum=v;
	}
	
	/**
	 * 
	 */
	public int shopnpc  = 0  ;
	
	public int getShopnpc(){
		return this.shopnpc;
	}
	
	public void setShopnpc(int v){
		this.shopnpc=v;
	}
	
	
};