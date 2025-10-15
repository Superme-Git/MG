package fire.pb.item;


public class SChangKuExt implements mytools.ConvMain.Checkable ,Comparable<SChangKuExt>{

	public int compareTo(SChangKuExt o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SChangKuExt(){
		super();
	}
	public SChangKuExt(SChangKuExt arg){
		this.id=arg.id ;
		this.haveCount=arg.haveCount ;
		this.needyinbi=arg.needyinbi ;
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
	public int haveCount  = 0  ;
	
	public int getHaveCount(){
		return this.haveCount;
	}
	
	public void setHaveCount(int v){
		this.haveCount=v;
	}
	
	/**
	 * 
	 */
	public int needyinbi  = 0  ;
	
	public int getNeedyinbi(){
		return this.needyinbi;
	}
	
	public void setNeedyinbi(int v){
		this.needyinbi=v;
	}
	
	
};