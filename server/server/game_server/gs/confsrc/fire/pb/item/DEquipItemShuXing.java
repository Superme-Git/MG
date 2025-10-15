package fire.pb.item;


public class DEquipItemShuXing  extends EquipItemShuXing {

	public int compareTo(DEquipItemShuXing o){
		return this.id-o.id;
	}

	
	public DEquipItemShuXing(EquipItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DEquipItemShuXing(){
		super();
	}
	public DEquipItemShuXing(DEquipItemShuXing arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};