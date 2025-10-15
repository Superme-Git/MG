package fire.pb.mission;


public class Squestions implements mytools.ConvMain.Checkable ,Comparable<Squestions>{

	public int compareTo(Squestions o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Squestions(){
		super();
	}
	public Squestions(Squestions arg){
		this.id=arg.id ;
		this.questionsid=arg.questionsid ;
		this.correct=arg.correct ;
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
	public int questionsid  = 0  ;
	
	public int getQuestionsid(){
		return this.questionsid;
	}
	
	public void setQuestionsid(int v){
		this.questionsid=v;
	}
	
	/**
	 * 
	 */
	public int correct  = 0  ;
	
	public int getCorrect(){
		return this.correct;
	}
	
	public void setCorrect(int v){
		this.correct=v;
	}
	
	
};