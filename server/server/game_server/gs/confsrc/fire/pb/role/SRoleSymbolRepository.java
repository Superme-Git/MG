package fire.pb.role;


public class SRoleSymbolRepository implements mytools.ConvMain.Checkable ,Comparable<SRoleSymbolRepository>{

	public int compareTo(SRoleSymbolRepository o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRoleSymbolRepository(){
		super();
	}
	public SRoleSymbolRepository(SRoleSymbolRepository arg){
		this.id=arg.id ;
		this.symbol=arg.symbol ;
		this.type=arg.type ;
		this.gender=arg.gender ;
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
	public String symbol  = null  ;
	
	public String getSymbol(){
		return this.symbol;
	}
	
	public void setSymbol(String v){
		this.symbol=v;
	}
	
	/**
	 * 
	 */
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 
	 */
	public int gender  = 0  ;
	
	public int getGender(){
		return this.gender;
	}
	
	public void setGender(int v){
		this.gender=v;
	}
	
	
};