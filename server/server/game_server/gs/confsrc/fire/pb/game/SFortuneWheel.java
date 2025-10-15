package fire.pb.game;


public class SFortuneWheel implements mytools.ConvMain.Checkable ,Comparable<SFortuneWheel>{

	public int compareTo(SFortuneWheel o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFortuneWheel(){
		super();
	}
	public SFortuneWheel(SFortuneWheel arg){
		this.id=arg.id ;
		this.cards=arg.cards ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 大转盘的id,会有多种大转盘,奖励不一样
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
	public java.util.ArrayList<String> cards  ;
	
	public java.util.ArrayList<String> getCards(){
		return this.cards;
	}
	
	public void setCards(java.util.ArrayList<String> v){
		this.cards=v;
	}
	
	
};