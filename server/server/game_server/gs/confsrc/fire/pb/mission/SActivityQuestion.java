package fire.pb.mission;


public class SActivityQuestion implements mytools.ConvMain.Checkable ,Comparable<SActivityQuestion>{

	public int compareTo(SActivityQuestion o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SActivityQuestion(){
		super();
	}
	public SActivityQuestion(SActivityQuestion arg){
		this.id=arg.id ;
		this.questionid=arg.questionid ;
		this.step=arg.step ;
		this.question=arg.question ;
		this.answer1=arg.answer1 ;
		this.answer2=arg.answer2 ;
		this.answer3=arg.answer3 ;
		this.rightanswer=arg.rightanswer ;
		this.rightrewardid=arg.rightrewardid ;
		this.errorrewardid=arg.errorrewardid ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 题目ID
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
	public int questionid  = 0  ;
	
	public int getQuestionid(){
		return this.questionid;
	}
	
	public void setQuestionid(int v){
		this.questionid=v;
	}
	
	/**
	 * 步骤
	 */
	public int step  = 0  ;
	
	public int getStep(){
		return this.step;
	}
	
	public void setStep(int v){
		this.step=v;
	}
	
	/**
	 * 
	 */
	public String question  = null  ;
	
	public String getQuestion(){
		return this.question;
	}
	
	public void setQuestion(String v){
		this.question=v;
	}
	
	/**
	 * 
	 */
	public String answer1  = null  ;
	
	public String getAnswer1(){
		return this.answer1;
	}
	
	public void setAnswer1(String v){
		this.answer1=v;
	}
	
	/**
	 * 
	 */
	public String answer2  = null  ;
	
	public String getAnswer2(){
		return this.answer2;
	}
	
	public void setAnswer2(String v){
		this.answer2=v;
	}
	
	/**
	 * 
	 */
	public String answer3  = null  ;
	
	public String getAnswer3(){
		return this.answer3;
	}
	
	public void setAnswer3(String v){
		this.answer3=v;
	}
	
	/**
	 * 
	 */
	public int rightanswer  = 0  ;
	
	public int getRightanswer(){
		return this.rightanswer;
	}
	
	public void setRightanswer(int v){
		this.rightanswer=v;
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