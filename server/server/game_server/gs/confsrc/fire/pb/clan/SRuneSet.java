package fire.pb.clan;


public class SRuneSet implements mytools.ConvMain.Checkable ,Comparable<SRuneSet>{

	public int compareTo(SRuneSet o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRuneSet(){
		super();
	}
	public SRuneSet(SRuneSet arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.desc=arg.desc ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 编号
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 名称
	 */
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 描述
	 */
	public String desc  = null  ;
	
	public String getDesc(){
		return this.desc;
	}
	
	public void setDesc(String v){
		this.desc=v;
	}
	
	
};