package fire.pb.item;


public class SEquipLvGemInfo implements mytools.ConvMain.Checkable ,Comparable<SEquipLvGemInfo>{

	public int compareTo(SEquipLvGemInfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipLvGemInfo(){
		super();
	}
	public SEquipLvGemInfo(SEquipLvGemInfo arg){
		this.id=arg.id ;
		this.gemsLevel=arg.gemsLevel ;
		this.hols=arg.hols ;
		this.holsLevel=arg.holsLevel ;
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
	public int gemsLevel  = 0  ;
	
	public int getGemsLevel(){
		return this.gemsLevel;
	}
	
	public void setGemsLevel(int v){
		this.gemsLevel=v;
	}
	
	/**
	 * 
	 */
	public int hols  = 0  ;
	
	public int getHols(){
		return this.hols;
	}
	
	public void setHols(int v){
		this.hols=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> holsLevel  ;
	
	public java.util.ArrayList<Integer> getHolsLevel(){
		return this.holsLevel;
	}
	
	public void setHolsLevel(java.util.ArrayList<Integer> v){
		this.holsLevel=v;
	}
	
	
};