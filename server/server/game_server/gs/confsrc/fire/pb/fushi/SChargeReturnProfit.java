package fire.pb.fushi;


public class SChargeReturnProfit implements mytools.ConvMain.Checkable ,Comparable<SChargeReturnProfit>{

	public int compareTo(SChargeReturnProfit o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SChargeReturnProfit(){
		super();
	}
	public SChargeReturnProfit(SChargeReturnProfit arg){
		this.id=arg.id ;
		this.chargevalue=arg.chargevalue ;
		this.rewarditems=arg.rewarditems ;
		this.rewarditemnums=arg.rewarditemnums ;
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
	public int chargevalue  = 0  ;
	
	public int getChargevalue(){
		return this.chargevalue;
	}
	
	public void setChargevalue(int v){
		this.chargevalue=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> rewarditems  ;
	
	public java.util.ArrayList<Integer> getRewarditems(){
		return this.rewarditems;
	}
	
	public void setRewarditems(java.util.ArrayList<Integer> v){
		this.rewarditems=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> rewarditemnums  ;
	
	public java.util.ArrayList<Integer> getRewarditemnums(){
		return this.rewarditemnums;
	}
	
	public void setRewarditemnums(java.util.ArrayList<Integer> v){
		this.rewarditemnums=v;
	}
	
	
};