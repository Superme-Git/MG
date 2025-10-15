package fire.pb.shop;


public class SMallShop implements mytools.ConvMain.Checkable ,Comparable<SMallShop>{

	public int compareTo(SMallShop o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SMallShop(){
		super();
	}
	public SMallShop(SMallShop arg){
		this.id=arg.id ;
		this.type=arg.type ;
		this.totalrecharge=arg.totalrecharge ;
		this.viplvrequire=arg.viplvrequire ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 商品ID
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 商品分类
	 */
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 累计充值符石
	 */
	public int totalrecharge  = 0  ;
	
	public int getTotalrecharge(){
		return this.totalrecharge;
	}
	
	public void setTotalrecharge(int v){
		this.totalrecharge=v;
	}
	
	/**
	 * VIP等级要求
	 */
	public int viplvrequire  = 0  ;
	
	public int getViplvrequire(){
		return this.viplvrequire;
	}
	
	public void setViplvrequire(int v){
		this.viplvrequire=v;
	}
	
	
};