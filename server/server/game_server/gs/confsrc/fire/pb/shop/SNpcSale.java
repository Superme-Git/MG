package fire.pb.shop;


public class SNpcSale implements mytools.ConvMain.Checkable ,Comparable<SNpcSale>{

	public int compareTo(SNpcSale o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNpcSale(){
		super();
	}
	public SNpcSale(SNpcSale arg){
		this.id=arg.id ;
		this.floating=arg.floating ;
		this.floatingtime=arg.floatingtime ;
		this.goodsids=arg.goodsids ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 商店序号
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 价格是否波动 0:不波动 1:波动
	 */
	public int floating  = 0  ;
	
	public int getFloating(){
		return this.floating;
	}
	
	public void setFloating(int v){
		this.floating=v;
	}
	
	/**
	 * 波动周期(单位：秒)
	 */
	public int floatingtime  = 0  ;
	
	public int getFloatingtime(){
		return this.floatingtime;
	}
	
	public void setFloatingtime(int v){
		this.floatingtime=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> goodsids  ;
	
	public java.util.ArrayList<Integer> getGoodsids(){
		return this.goodsids;
	}
	
	public void setGoodsids(java.util.ArrayList<Integer> v){
		this.goodsids=v;
	}
	
	
};