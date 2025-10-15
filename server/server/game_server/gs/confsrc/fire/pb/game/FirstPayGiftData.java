package fire.pb.game;


public class FirstPayGiftData implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FirstPayGiftData(){
		super();
	}
	public FirstPayGiftData(FirstPayGiftData arg){
		this.itemid=arg.itemid ;
		this.itemnum=arg.itemnum ;
		this.isbind=arg.isbind ;
		this.petid=arg.petid ;
		this.petnum=arg.petnum ;
		this.petisbind=arg.petisbind ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int itemid  = 0  ;
	
	public int getItemid(){
		return this.itemid;
	}
	
	public void setItemid(int v){
		this.itemid=v;
	}
	
	/**
	 * 
	 */
	public int itemnum  = 0  ;
	
	public int getItemnum(){
		return this.itemnum;
	}
	
	public void setItemnum(int v){
		this.itemnum=v;
	}
	
	/**
	 * 
	 */
	public int isbind  = 0  ;
	
	public int getIsbind(){
		return this.isbind;
	}
	
	public void setIsbind(int v){
		this.isbind=v;
	}
	
	/**
	 * 
	 */
	public int petid  = 0  ;
	
	public int getPetid(){
		return this.petid;
	}
	
	public void setPetid(int v){
		this.petid=v;
	}
	
	/**
	 * 
	 */
	public int petnum  = 0  ;
	
	public int getPetnum(){
		return this.petnum;
	}
	
	public void setPetnum(int v){
		this.petnum=v;
	}
	
	/**
	 * 
	 */
	public int petisbind  = 0  ;
	
	public int getPetisbind(){
		return this.petisbind;
	}
	
	public void setPetisbind(int v){
		this.petisbind=v;
	}
	
	
};