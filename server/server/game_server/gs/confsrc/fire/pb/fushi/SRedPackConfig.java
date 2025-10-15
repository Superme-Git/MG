package fire.pb.fushi;


public class SRedPackConfig implements mytools.ConvMain.Checkable ,Comparable<SRedPackConfig>{

	public int compareTo(SRedPackConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRedPackConfig(){
		super();
	}
	public SRedPackConfig(SRedPackConfig arg){
		this.id=arg.id ;
		this.fushimin=arg.fushimin ;
		this.fishimax=arg.fishimax ;
		this.daysendmax=arg.daysendmax ;
		this.dayreceivemax=arg.dayreceivemax ;
		this.dayfushisendmax=arg.dayfushisendmax ;
		this.packmin=arg.packmin ;
		this.packmax=arg.packmax ;
		this.level=arg.level ;
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
	public int fushimin  = 0  ;
	
	public int getFushimin(){
		return this.fushimin;
	}
	
	public void setFushimin(int v){
		this.fushimin=v;
	}
	
	/**
	 * 
	 */
	public int fishimax  = 0  ;
	
	public int getFishimax(){
		return this.fishimax;
	}
	
	public void setFishimax(int v){
		this.fishimax=v;
	}
	
	/**
	 * 
	 */
	public int daysendmax  = 0  ;
	
	public int getDaysendmax(){
		return this.daysendmax;
	}
	
	public void setDaysendmax(int v){
		this.daysendmax=v;
	}
	
	/**
	 * 
	 */
	public int dayreceivemax  = 0  ;
	
	public int getDayreceivemax(){
		return this.dayreceivemax;
	}
	
	public void setDayreceivemax(int v){
		this.dayreceivemax=v;
	}
	
	/**
	 * 
	 */
	public int dayfushisendmax  = 0  ;
	
	public int getDayfushisendmax(){
		return this.dayfushisendmax;
	}
	
	public void setDayfushisendmax(int v){
		this.dayfushisendmax=v;
	}
	
	/**
	 * 
	 */
	public int packmin  = 0  ;
	
	public int getPackmin(){
		return this.packmin;
	}
	
	public void setPackmin(int v){
		this.packmin=v;
	}
	
	/**
	 * 
	 */
	public int packmax  = 0  ;
	
	public int getPackmax(){
		return this.packmax;
	}
	
	public void setPackmax(int v){
		this.packmax=v;
	}
	
	/**
	 * 
	 */
	public int level  = 0  ;
	
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int v){
		this.level=v;
	}
	
	
};