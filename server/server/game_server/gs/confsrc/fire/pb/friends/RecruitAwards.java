package fire.pb.friends;


public class RecruitAwards implements mytools.ConvMain.Checkable ,Comparable<RecruitAwards>{

	public int compareTo(RecruitAwards o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public RecruitAwards(){
		super();
	}
	public RecruitAwards(RecruitAwards arg){
		this.id=arg.id ;
		this.num=arg.num ;
		this.awards=arg.awards ;
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
	public int num  = 0  ;
	
	public int getNum(){
		return this.num;
	}
	
	public void setNum(int v){
		this.num=v;
	}
	
	/**
	 * 
	 */
	public String awards  = null  ;
	
	public String getAwards(){
		return this.awards;
	}
	
	public void setAwards(String v){
		this.awards=v;
	}
	
	
};