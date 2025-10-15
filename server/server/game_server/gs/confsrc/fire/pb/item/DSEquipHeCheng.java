package fire.pb.item;


public class DSEquipHeCheng  extends SEquipHeCheng {

	public int compareTo(DSEquipHeCheng o){
		return this.id-o.id;
	}

	
	public DSEquipHeCheng(SEquipHeCheng arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DSEquipHeCheng(){
		super();
	}
	public DSEquipHeCheng(DSEquipHeCheng arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};