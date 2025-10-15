package fire.pb.skill;


public class SNuqiget implements mytools.ConvMain.Checkable ,Comparable<SNuqiget>{

	public int compareTo(SNuqiget o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNuqiget(){
		super();
	}
	public SNuqiget(SNuqiget arg){
		this.id=arg.id ;
		this.hurtmin=arg.hurtmin ;
		this.hurtmax=arg.hurtmax ;
		this.nuqiget=arg.nuqiget ;
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
	public int hurtmin  = 0  ;
	
	public int getHurtmin(){
		return this.hurtmin;
	}
	
	public void setHurtmin(int v){
		this.hurtmin=v;
	}
	
	/**
	 * 
	 */
	public int hurtmax  = 0  ;
	
	public int getHurtmax(){
		return this.hurtmax;
	}
	
	public void setHurtmax(int v){
		this.hurtmax=v;
	}
	
	/**
	 * 
	 */
	public int nuqiget  = 0  ;
	
	public int getNuqiget(){
		return this.nuqiget;
	}
	
	public void setNuqiget(int v){
		this.nuqiget=v;
	}
	
	
};