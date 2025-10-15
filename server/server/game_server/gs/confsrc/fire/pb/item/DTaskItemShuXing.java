package fire.pb.item;


public class DTaskItemShuXing  extends TaskItemShuXing {

	public int compareTo(DTaskItemShuXing o){
		return this.id-o.id;
	}

	
	public DTaskItemShuXing(TaskItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DTaskItemShuXing(){
		super();
	}
	public DTaskItemShuXing(DTaskItemShuXing arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};