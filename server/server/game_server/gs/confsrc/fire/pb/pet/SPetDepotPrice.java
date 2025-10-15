package fire.pb.pet;


public class SPetDepotPrice implements mytools.ConvMain.Checkable ,Comparable<SPetDepotPrice>{

	public int compareTo(SPetDepotPrice o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPetDepotPrice(){
		super();
	}
	public SPetDepotPrice(SPetDepotPrice arg){
		this.id=arg.id ;
		this.num=arg.num ;
		this.nextneedmoney=arg.nextneedmoney ;
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
	public int num  = 0  ;
	
	public int getNum(){
		return this.num;
	}
	
	public void setNum(int v){
		this.num=v;
	}
	
	/**
	 * 
	 */
	public int nextneedmoney  = 0  ;
	
	public int getNextneedmoney(){
		return this.nextneedmoney;
	}
	
	public void setNextneedmoney(int v){
		this.nextneedmoney=v;
	}
	
	
};