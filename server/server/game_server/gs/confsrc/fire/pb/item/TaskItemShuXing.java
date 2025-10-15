package fire.pb.item;


public class TaskItemShuXing  extends ItemShuXing {

	public int compareTo(TaskItemShuXing o){
		return this.id-o.id;
	}

	
	public TaskItemShuXing(ItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public TaskItemShuXing(){
		super();
	}
	public TaskItemShuXing(TaskItemShuXing arg){
		super(arg);
		this.intoquestpack=arg.intoquestpack ;
		this.maxnaijiu=arg.maxnaijiu ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int intoquestpack  = 0  ;
	
	public int getIntoquestpack(){
		return this.intoquestpack;
	}
	
	public void setIntoquestpack(int v){
		this.intoquestpack=v;
	}
	
	/**
	 * 
	 */
	public int maxnaijiu  = 0  ;
	
	public int getMaxnaijiu(){
		return this.maxnaijiu;
	}
	
	public void setMaxnaijiu(int v){
		this.maxnaijiu=v;
	}
	
	
};