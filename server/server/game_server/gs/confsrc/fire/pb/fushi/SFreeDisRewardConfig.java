package fire.pb.fushi;


public class SFreeDisRewardConfig implements mytools.ConvMain.Checkable ,Comparable<SFreeDisRewardConfig>{

	public int compareTo(SFreeDisRewardConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFreeDisRewardConfig(){
		super();
	}
	public SFreeDisRewardConfig(SFreeDisRewardConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.itemids=arg.itemids ;
		this.num=arg.num ;
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
	public java.util.ArrayList<Integer> itemids  ;
	
	public java.util.ArrayList<Integer> getItemids(){
		return this.itemids;
	}
	
	public void setItemids(java.util.ArrayList<Integer> v){
		this.itemids=v;
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
	
	
};