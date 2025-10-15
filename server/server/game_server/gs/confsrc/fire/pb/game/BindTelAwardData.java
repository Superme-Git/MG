package fire.pb.game;


public class BindTelAwardData implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public BindTelAwardData(){
		super();
	}
	public BindTelAwardData(BindTelAwardData arg){
		this.item1id=arg.item1id ;
		this.item1num=arg.item1num ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public String item1id  = null  ;
	
	public String getItem1id(){
		return this.item1id;
	}
	
	public void setItem1id(String v){
		this.item1id=v;
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
	
	
};