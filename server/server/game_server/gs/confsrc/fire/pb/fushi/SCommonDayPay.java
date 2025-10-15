package fire.pb.fushi;


public class SCommonDayPay implements mytools.ConvMain.Checkable ,Comparable<SCommonDayPay>{

	public int compareTo(SCommonDayPay o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCommonDayPay(){
		super();
	}
	public SCommonDayPay(SCommonDayPay arg){
		this.id=arg.id ;
		this.serverdata=arg.serverdata ;
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
	public int serverdata  = 0  ;
	
	public int getServerdata(){
		return this.serverdata;
	}
	
	public void setServerdata(int v){
		this.serverdata=v;
	}
	
	
};