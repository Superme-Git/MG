package fire.pb.game;


public class ImperialExamStateRepo implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public ImperialExamStateRepo(){
		super();
	}
	public ImperialExamStateRepo(ImperialExamStateRepo arg){
		this.id=arg.id ;
		this.answer=arg.answer ;
		this.rightrewardid=arg.rightrewardid ;
		this.errorrewardid=arg.errorrewardid ;
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
	public int answer  = 0  ;
	
	public int getAnswer(){
		return this.answer;
	}
	
	public void setAnswer(int v){
		this.answer=v;
	}
	
	/**
	 * 
	 */
	public int rightrewardid  = 0  ;
	
	public int getRightrewardid(){
		return this.rightrewardid;
	}
	
	public void setRightrewardid(int v){
		this.rightrewardid=v;
	}
	
	/**
	 * 
	 */
	public int errorrewardid  = 0  ;
	
	public int getErrorrewardid(){
		return this.errorrewardid;
	}
	
	public void setErrorrewardid(int v){
		this.errorrewardid=v;
	}
	
	
};