package fire.pb.clan;


public class SClanyaofang implements mytools.ConvMain.Checkable ,Comparable<SClanyaofang>{

	public int compareTo(SClanyaofang o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SClanyaofang(){
		super();
	}
	public SClanyaofang(SClanyaofang arg){
		this.id=arg.id ;
		this.mingzi=arg.mingzi ;
		this.money=arg.money ;
		this.banggong=arg.banggong ;
		this.randomgroup=arg.randomgroup ;
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
	public String mingzi  = null  ;
	
	public String getMingzi(){
		return this.mingzi;
	}
	
	public void setMingzi(String v){
		this.mingzi=v;
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
	public int banggong  = 0  ;
	
	public int getBanggong(){
		return this.banggong;
	}
	
	public void setBanggong(int v){
		this.banggong=v;
	}
	
	/**
	 * 
	 */
	public int randomgroup  = 0  ;
	
	public int getRandomgroup(){
		return this.randomgroup;
	}
	
	public void setRandomgroup(int v){
		this.randomgroup=v;
	}
	
	
};