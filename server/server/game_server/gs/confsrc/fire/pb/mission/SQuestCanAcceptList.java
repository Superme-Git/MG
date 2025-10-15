package fire.pb.mission;


public class SQuestCanAcceptList  extends QuestCanAcceptList {

	public int compareTo(SQuestCanAcceptList o){
		return this.id-o.id;
	}

	
	public SQuestCanAcceptList(QuestCanAcceptList arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SQuestCanAcceptList(){
		super();
	}
	public SQuestCanAcceptList(SQuestCanAcceptList arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};