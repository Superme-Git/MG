package fire.pb.item;


public class DSFoodAndDrugEffect  extends SFoodAndDrugEffect {

	public int compareTo(DSFoodAndDrugEffect o){
		return this.id-o.id;
	}

	
	public DSFoodAndDrugEffect(SFoodAndDrugEffect arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DSFoodAndDrugEffect(){
		super();
	}
	public DSFoodAndDrugEffect(DSFoodAndDrugEffect arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};