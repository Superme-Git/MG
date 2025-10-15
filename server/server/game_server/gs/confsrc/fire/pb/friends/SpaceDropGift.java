package fire.pb.friends;


public class SpaceDropGift implements mytools.ConvMain.Checkable ,Comparable<SpaceDropGift>{

	public int compareTo(SpaceDropGift o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SpaceDropGift(){
		super();
	}
	public SpaceDropGift(SpaceDropGift arg){
		this.id=arg.id ;
		this.item=arg.item ;
		this.weight=arg.weight ;
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
	public int item  = 0  ;
	
	public int getItem(){
		return this.item;
	}
	
	public void setItem(int v){
		this.item=v;
	}
	
	/**
	 * 
	 */
	public int weight  = 0  ;
	
	public int getWeight(){
		return this.weight;
	}
	
	public void setWeight(int v){
		this.weight=v;
	}
	
	
};