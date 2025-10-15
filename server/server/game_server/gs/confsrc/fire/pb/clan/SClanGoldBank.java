package fire.pb.clan;


public class SClanGoldBank implements mytools.ConvMain.Checkable ,Comparable<SClanGoldBank>{

	public int compareTo(SClanGoldBank o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SClanGoldBank(){
		super();
	}
	public SClanGoldBank(SClanGoldBank arg){
		this.id=arg.id ;
		this.levelupcost=arg.levelupcost ;
		this.bonus=arg.bonus ;
		this.allbonus=arg.allbonus ;
		this.limitmoney=arg.limitmoney ;
		this.costeveryday=arg.costeveryday ;
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
	public int levelupcost  = 0  ;
	
	public int getLevelupcost(){
		return this.levelupcost;
	}
	
	public void setLevelupcost(int v){
		this.levelupcost=v;
	}
	
	/**
	 * 
	 */
	public int bonus  = 0  ;
	
	public int getBonus(){
		return this.bonus;
	}
	
	public void setBonus(int v){
		this.bonus=v;
	}
	
	/**
	 * 
	 */
	public int allbonus  = 0  ;
	
	public int getAllbonus(){
		return this.allbonus;
	}
	
	public void setAllbonus(int v){
		this.allbonus=v;
	}
	
	/**
	 * 
	 */
	public int limitmoney  = 0  ;
	
	public int getLimitmoney(){
		return this.limitmoney;
	}
	
	public void setLimitmoney(int v){
		this.limitmoney=v;
	}
	
	/**
	 * 
	 */
	public int costeveryday  = 0  ;
	
	public int getCosteveryday(){
		return this.costeveryday;
	}
	
	public void setCosteveryday(int v){
		this.costeveryday=v;
	}
	
	
};