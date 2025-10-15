package fire.pb.team;


public class SGuangHuanLevelup implements mytools.ConvMain.Checkable ,Comparable<SGuangHuanLevelup>{

	public int compareTo(SGuangHuanLevelup o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SGuangHuanLevelup(){
		super();
	}
	public SGuangHuanLevelup(SGuangHuanLevelup arg){
		this.id=arg.id ;
		this.needexp=arg.needexp ;
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
	public int needexp  = 0  ;
	
	public int getNeedexp(){
		return this.needexp;
	}
	
	public void setNeedexp(int v){
		this.needexp=v;
	}
	
	
};