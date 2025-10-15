package fire.pb.item;


public class FoodItemAttr  extends RecoveryItemAttr {


	
	public FoodItemAttr(RecoveryItemAttr arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FoodItemAttr(){
		super();
	}
	public FoodItemAttr(FoodItemAttr arg){
		super(arg);
		this.是否有品质=arg.是否有品质 ;
		this.增加宠物寿命=arg.增加宠物寿命 ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int 是否有品质  = 0  ;
	
	public int get是否有品质(){
		return this.是否有品质;
	}
	
	public void set是否有品质(int v){
		this.是否有品质=v;
	}
	
	/**
	 * 
	 */
	public int 增加宠物寿命  = 0  ;
	
	public int get增加宠物寿命(){
		return this.增加宠物寿命;
	}
	
	public void set增加宠物寿命(int v){
		this.增加宠物寿命=v;
	}
	
	
};