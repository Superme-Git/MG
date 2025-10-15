package fire.pb.circletask;


public class RenXingCircTaskCost implements mytools.ConvMain.Checkable ,Comparable<RenXingCircTaskCost>{

	public int compareTo(RenXingCircTaskCost o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public RenXingCircTaskCost(){
		super();
	}
	public RenXingCircTaskCost(RenXingCircTaskCost arg){
		this.id=arg.id ;
		this.stonecost=arg.stonecost ;
		this.xiayicost=arg.xiayicost ;
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
	public int stonecost  = 0  ;
	
	public int getStonecost(){
		return this.stonecost;
	}
	
	public void setStonecost(int v){
		this.stonecost=v;
	}
	
	/**
	 * 
	 */
	public int xiayicost  = 0  ;
	
	public int getXiayicost(){
		return this.xiayicost;
	}
	
	public void setXiayicost(int v){
		this.xiayicost=v;
	}
	
	
};