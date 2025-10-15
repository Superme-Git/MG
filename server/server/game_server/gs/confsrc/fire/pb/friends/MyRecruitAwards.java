package fire.pb.friends;


public class MyRecruitAwards implements mytools.ConvMain.Checkable ,Comparable<MyRecruitAwards>{

	public int compareTo(MyRecruitAwards o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public MyRecruitAwards(){
		super();
	}
	public MyRecruitAwards(MyRecruitAwards arg){
		this.id=arg.id ;
		this.level=arg.level ;
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
	public int level  = 0  ;
	
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int v){
		this.level=v;
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