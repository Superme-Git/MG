package fire.pb.mission;


public class QuestCanAcceptList implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public QuestCanAcceptList(){
		super();
	}
	public QuestCanAcceptList(QuestCanAcceptList arg){
		this.id=arg.id ;
		this.任务等级min=arg.任务等级min ;
		this.任务等级max=arg.任务等级max ;
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
	public int 任务等级min  = 0  ;
	
	public int get任务等级min(){
		return this.任务等级min;
	}
	
	public void set任务等级min(int v){
		this.任务等级min=v;
	}
	
	/**
	 * 
	 */
	public int 任务等级max  = 0  ;
	
	public int get任务等级max(){
		return this.任务等级max;
	}
	
	public void set任务等级max(int v){
		this.任务等级max=v;
	}
	
	
};