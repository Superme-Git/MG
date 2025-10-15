package fire.pb.item;


public class gemItemShuXing  extends ItemShuXing {

	public int compareTo(gemItemShuXing o){
		return this.id-o.id;
	}

	
	public gemItemShuXing(ItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public gemItemShuXing(){
		super();
	}
	public gemItemShuXing(gemItemShuXing arg){
		super(arg);
		this.effectname=arg.effectname ;
		this.effect=arg.effect ;
		this.gemType=arg.gemType ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> effectname  ;
	
	public java.util.ArrayList<Integer> getEffectname(){
		return this.effectname;
	}
	
	public void setEffectname(java.util.ArrayList<Integer> v){
		this.effectname=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> effect  ;
	
	public java.util.ArrayList<Integer> getEffect(){
		return this.effect;
	}
	
	public void setEffect(java.util.ArrayList<Integer> v){
		this.effect=v;
	}
	
	/**
	 * 
	 */
	public int gemType  = 0  ;
	
	public int getGemType(){
		return this.gemType;
	}
	
	public void setGemType(int v){
		this.gemType=v;
	}
	
	
};