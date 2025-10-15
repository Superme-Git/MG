package fire.pb.pet;


public class PetResetPointConfig implements mytools.ConvMain.Checkable ,Comparable<PetResetPointConfig>{

	public int compareTo(PetResetPointConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PetResetPointConfig(){
		super();
	}
	public PetResetPointConfig(PetResetPointConfig arg){
		this.id=arg.id ;
		this.cost=arg.cost ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 重置次数
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
	public int cost  = 0  ;
	
	public int getCost(){
		return this.cost;
	}
	
	public void setCost(int v){
		this.cost=v;
	}
	
	
};