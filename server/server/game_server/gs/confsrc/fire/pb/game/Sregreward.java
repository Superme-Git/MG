package fire.pb.game;


public class Sregreward implements mytools.ConvMain.Checkable ,Comparable<Sregreward>{

	public int compareTo(Sregreward o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Sregreward(){
		super();
	}
	public Sregreward(Sregreward arg){
		this.id=arg.id ;
		this.itemid=arg.itemid ;
		this.itemnum=arg.itemnum ;
		this.isbind=arg.isbind ;
		this.mtype=arg.mtype ;
		this.money=arg.money ;
		this.ratio=arg.ratio ;
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
	public int mtype  = 0  ;
	
	public int getMtype(){
		return this.mtype;
	}
	
	public void setMtype(int v){
		this.mtype=v;
	}
	
	/**
	 * 
	 */
	public int money  = 0  ;
	
	public int getMoney(){
		return this.money;
	}
	
	public void setMoney(int v){
		this.money=v;
	}
	
	/**
	 * 
	 */
	public int ratio  = 0  ;
	
	public int getRatio(){
		return this.ratio;
	}
	
	public void setRatio(int v){
		this.ratio=v;
	}
	
	
};