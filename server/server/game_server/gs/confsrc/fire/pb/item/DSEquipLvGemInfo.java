package fire.pb.item;


public class DSEquipLvGemInfo  extends SEquipLvGemInfo {

	public int compareTo(DSEquipLvGemInfo o){
		return this.id-o.id;
	}

	
	public DSEquipLvGemInfo(SEquipLvGemInfo arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DSEquipLvGemInfo(){
		super();
	}
	public DSEquipLvGemInfo(DSEquipLvGemInfo arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};