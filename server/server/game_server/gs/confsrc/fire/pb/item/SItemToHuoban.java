package fire.pb.item;


public class SItemToHuoban implements mytools.ConvMain.Checkable ,Comparable<SItemToHuoban>{

	public int compareTo(SItemToHuoban o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SItemToHuoban(){
		super();
	}
	public SItemToHuoban(SItemToHuoban arg){
		this.id=arg.id ;
		this.huobanId=arg.huobanId ;
		this.daysType=arg.daysType ;
		this.bagType=arg.bagType ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 
	 */
	public int huobanId  = 0  ;
	
	public int getHuobanId(){
		return this.huobanId;
	}
	
	public void setHuobanId(int v){
		this.huobanId=v;
	}
	
	/**
	 * 0表示7天;1表示30天;2表示永久
	 */
	public int daysType  = 0  ;
	
	public int getDaysType(){
		return this.daysType;
	}
	
	public void setDaysType(int v){
		this.daysType=v;
	}
	
	/**
	 * 0表示进背包;1表示不进背包
	 */
	public int bagType  = 0  ;
	
	public int getBagType(){
		return this.bagType;
	}
	
	public void setBagType(int v){
		this.bagType=v;
	}
	
	
};