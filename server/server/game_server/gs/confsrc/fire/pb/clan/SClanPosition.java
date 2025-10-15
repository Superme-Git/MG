package fire.pb.clan;


public class SClanPosition implements mytools.ConvMain.Checkable ,Comparable<SClanPosition>{

	public int compareTo(SClanPosition o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SClanPosition(){
		super();
	}
	public SClanPosition(SClanPosition arg){
		this.id=arg.id ;
		this.posname=arg.posname ;
		this.posmaxnum=arg.posmaxnum ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 职务编号
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 职务名称
	 */
	public String posname  = null  ;
	
	public String getPosname(){
		return this.posname;
	}
	
	public void setPosname(String v){
		this.posname=v;
	}
	
	/**
	 * 职务名额上限
	 */
	public int posmaxnum  = 0  ;
	
	public int getPosmaxnum(){
		return this.posmaxnum;
	}
	
	public void setPosmaxnum(int v){
		this.posmaxnum=v;
	}
	
	
};