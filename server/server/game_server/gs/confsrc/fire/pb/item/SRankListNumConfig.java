package fire.pb.item;


public class SRankListNumConfig implements mytools.ConvMain.Checkable ,Comparable<SRankListNumConfig>{

	public int compareTo(SRankListNumConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRankListNumConfig(){
		super();
	}
	public SRankListNumConfig(SRankListNumConfig arg){
		this.id=arg.id ;
		this.personNum=arg.personNum ;
		this.namename=arg.namename ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
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
	public int personNum  = 0  ;
	
	public int getPersonNum(){
		return this.personNum;
	}
	
	public void setPersonNum(int v){
		this.personNum=v;
	}
	
	/**
	 * 
	 */
	public String namename  = null  ;
	
	public String getNamename(){
		return this.namename;
	}
	
	public void setNamename(String v){
		this.namename=v;
	}
	
	
};