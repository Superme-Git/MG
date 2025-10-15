package fire.pb.shop;


public class MarketFirstTable implements mytools.ConvMain.Checkable ,Comparable<MarketFirstTable>{

	public int compareTo(MarketFirstTable o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public MarketFirstTable(){
		super();
	}
	public MarketFirstTable(MarketFirstTable arg){
		this.id=arg.id ;
		this.firstname=arg.firstname ;
		this.isfloating=arg.isfloating ;
		this.secondno=arg.secondno ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 一级页签标识
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 一级页签名字
	 */
	public String firstname  = null  ;
	
	public String getFirstname(){
		return this.firstname;
	}
	
	public void setFirstname(String v){
		this.firstname=v;
	}
	
	/**
	 * 浮动开关 0 关 1开
	 */
	public int isfloating  = 0  ;
	
	public int getIsfloating(){
		return this.isfloating;
	}
	
	public void setIsfloating(int v){
		this.isfloating=v;
	}
	
	/**
	 * 二级页签id
	 */
	public java.util.ArrayList<Integer> secondno  ;
	
	public java.util.ArrayList<Integer> getSecondno(){
		return this.secondno;
	}
	
	public void setSecondno(java.util.ArrayList<Integer> v){
		this.secondno=v;
	}
	
	
};