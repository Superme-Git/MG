package fire.pb.pet;


public class PetNextExp implements mytools.ConvMain.Checkable ,Comparable<PetNextExp>{

	public int compareTo(PetNextExp o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PetNextExp(){
		super();
	}
	public PetNextExp(PetNextExp arg){
		this.id=arg.id ;
		this.exp=arg.exp ;
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
	public int exp  = 0  ;
	
	public int getExp(){
		return this.exp;
	}
	
	public void setExp(int v){
		this.exp=v;
	}
	
	
};