package fire.pb.item;


public class BagConfig implements mytools.ConvMain.Checkable ,Comparable<BagConfig>{

	public int compareTo(BagConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public BagConfig(){
		super();
	}
	public BagConfig(BagConfig arg){
		this.id=arg.id ;
		this.sizesize=arg.sizesize ;
		this.canpile=arg.canpile ;
		this.loginsend=arg.loginsend ;
		this.moveable=arg.moveable ;
		this.maxmoney=arg.maxmoney ;
		this.tablename=arg.tablename ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			do{
				int tmprefvalue=sizesize;
				
				if(tmprefvalue < 0) throw new RuntimeException("BagConfig.sizesize="+tmprefvalue+",所以不满足条件 BagConfig.sizesize < 0");
			}while(false);
			do{
				int tmprefvalue=canpile;
				
				if(tmprefvalue < 0) throw new RuntimeException("BagConfig.canpile="+tmprefvalue+",所以不满足条件 BagConfig.canpile < 0");
				if(tmprefvalue > 1) throw new RuntimeException("BagConfig.canpile="+tmprefvalue+",所以不满足条件 BagConfig.canpile > 1");
			}while(false);
			do{
				int tmprefvalue=loginsend;
				
				if(tmprefvalue < 0) throw new RuntimeException("BagConfig.loginsend="+tmprefvalue+",所以不满足条件 BagConfig.loginsend < 0");
				if(tmprefvalue > 1) throw new RuntimeException("BagConfig.loginsend="+tmprefvalue+",所以不满足条件 BagConfig.loginsend > 1");
			}while(false);
			do{
				int tmprefvalue=moveable;
				
				if(tmprefvalue < 0) throw new RuntimeException("BagConfig.moveable="+tmprefvalue+",所以不满足条件 BagConfig.moveable < 0");
				if(tmprefvalue > 1) throw new RuntimeException("BagConfig.moveable="+tmprefvalue+",所以不满足条件 BagConfig.moveable > 1");
			}while(false);
			do{
				long tmprefvalue=maxmoney;
				
				if(tmprefvalue < 0L) throw new RuntimeException("BagConfig.maxmoney="+tmprefvalue+",所以不满足条件 BagConfig.maxmoney < 0L");
			}while(false);
	}
	/**
	 * id
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
	public int sizesize  = 0  ;
	
	public int getSizesize(){
		return this.sizesize;
	}
	
	public void setSizesize(int v){
		this.sizesize=v;
	}
	
	/**
	 * 
	 */
	public int canpile  = 0  ;
	
	public int getCanpile(){
		return this.canpile;
	}
	
	public void setCanpile(int v){
		this.canpile=v;
	}
	
	/**
	 * 
	 */
	public int loginsend  = 0  ;
	
	public int getLoginsend(){
		return this.loginsend;
	}
	
	public void setLoginsend(int v){
		this.loginsend=v;
	}
	
	/**
	 * 
	 */
	public int moveable  = 0  ;
	
	public int getMoveable(){
		return this.moveable;
	}
	
	public void setMoveable(int v){
		this.moveable=v;
	}
	
	/**
	 * 
	 */
	public long maxmoney  = 0L  ;
	
	public long getMaxmoney(){
		return this.maxmoney;
	}
	
	public void setMaxmoney(long v){
		this.maxmoney=v;
	}
	
	/**
	 * 
	 */
	public String tablename  = null  ;
	
	public String getTablename(){
		return this.tablename;
	}
	
	public void setTablename(String v){
		this.tablename=v;
	}
	
	
};