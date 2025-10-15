package fire.pb.activity;


public class FestivalConf implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FestivalConf(){
		super();
	}
	public FestivalConf(FestivalConf arg){
		this.id=arg.id ;
		this.date=arg.date ;
		this.item1=arg.item1 ;
		this.item1num=arg.item1num ;
		this.item1bind=arg.item1bind ;
		this.item2=arg.item2 ;
		this.item2num=arg.item2num ;
		this.item2bind=arg.item2bind ;
		this.item3=arg.item3 ;
		this.item3num=arg.item3num ;
		this.item3bind=arg.item3bind ;
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
	public String date  = null  ;
	
	public String getDate(){
		return this.date;
	}
	
	public void setDate(String v){
		this.date=v;
	}
	
	/**
	 * 
	 */
	public int item1  = 0  ;
	
	public int getItem1(){
		return this.item1;
	}
	
	public void setItem1(int v){
		this.item1=v;
	}
	
	/**
	 * 
	 */
	public int item1num  = 0  ;
	
	public int getItem1num(){
		return this.item1num;
	}
	
	public void setItem1num(int v){
		this.item1num=v;
	}
	
	/**
	 * 
	 */
	public int item1bind  = 0  ;
	
	public int getItem1bind(){
		return this.item1bind;
	}
	
	public void setItem1bind(int v){
		this.item1bind=v;
	}
	
	/**
	 * 
	 */
	public int item2  = 0  ;
	
	public int getItem2(){
		return this.item2;
	}
	
	public void setItem2(int v){
		this.item2=v;
	}
	
	/**
	 * 
	 */
	public int item2num  = 0  ;
	
	public int getItem2num(){
		return this.item2num;
	}
	
	public void setItem2num(int v){
		this.item2num=v;
	}
	
	/**
	 * 
	 */
	public int item2bind  = 0  ;
	
	public int getItem2bind(){
		return this.item2bind;
	}
	
	public void setItem2bind(int v){
		this.item2bind=v;
	}
	
	/**
	 * 
	 */
	public int item3  = 0  ;
	
	public int getItem3(){
		return this.item3;
	}
	
	public void setItem3(int v){
		this.item3=v;
	}
	
	/**
	 * 
	 */
	public int item3num  = 0  ;
	
	public int getItem3num(){
		return this.item3num;
	}
	
	public void setItem3num(int v){
		this.item3num=v;
	}
	
	/**
	 * 
	 */
	public int item3bind  = 0  ;
	
	public int getItem3bind(){
		return this.item3bind;
	}
	
	public void setItem3bind(int v){
		this.item3bind=v;
	}
	
	
};