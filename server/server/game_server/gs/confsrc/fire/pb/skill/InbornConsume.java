package fire.pb.skill;


public class InbornConsume implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public InbornConsume(){
		super();
	}
	public InbornConsume(InbornConsume arg){
		this.exp=arg.exp ;
		this.money=arg.money ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int exp  = 0  ;
	
	public int getExp(){
		return this.exp;
	}
	
	public void setExp(int v){
		this.exp=v;
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
	
	
};