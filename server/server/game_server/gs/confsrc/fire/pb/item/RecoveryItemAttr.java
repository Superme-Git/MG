package fire.pb.item;


public class RecoveryItemAttr  extends ItemShuXing {


	
	public RecoveryItemAttr(ItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public RecoveryItemAttr(){
		super();
	}
	public RecoveryItemAttr(RecoveryItemAttr arg){
		super(arg);
		this.addhp=arg.addhp ;
		this.addhpmax=arg.addhpmax ;
		this.addmp=arg.addmp ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int addhp  = 0  ;
	
	public int getAddhp(){
		return this.addhp;
	}
	
	public void setAddhp(int v){
		this.addhp=v;
	}
	
	/**
	 * 
	 */
	public int addhpmax  = 0  ;
	
	public int getAddhpmax(){
		return this.addhpmax;
	}
	
	public void setAddhpmax(int v){
		this.addhpmax=v;
	}
	
	/**
	 * 
	 */
	public int addmp  = 0  ;
	
	public int getAddmp(){
		return this.addmp;
	}
	
	public void setAddmp(int v){
		this.addmp=v;
	}
	
	
};