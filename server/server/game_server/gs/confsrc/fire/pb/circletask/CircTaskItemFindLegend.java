package fire.pb.circletask;


public class CircTaskItemFindLegend implements mytools.ConvMain.Checkable ,Comparable<CircTaskItemFindLegend>{

	public int compareTo(CircTaskItemFindLegend o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskItemFindLegend(){
		super();
	}
	public CircTaskItemFindLegend(CircTaskItemFindLegend arg){
		this.id=arg.id ;
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
	public int weight  = 0  ;
	
	public int getWeight(){
		return this.weight;
	}
	
	public void setWeight(int v){
		this.weight=v;
	}
	
	
};