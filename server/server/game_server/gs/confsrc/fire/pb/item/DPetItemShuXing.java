package fire.pb.item;


public class DPetItemShuXing  extends PetItemShuXing {

	public int compareTo(DPetItemShuXing o){
		return this.id-o.id;
	}

	
	public DPetItemShuXing(PetItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public DPetItemShuXing(){
		super();
	}
	public DPetItemShuXing(DPetItemShuXing arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};