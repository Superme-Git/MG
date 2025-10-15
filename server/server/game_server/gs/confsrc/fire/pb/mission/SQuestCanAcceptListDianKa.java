package fire.pb.mission;


public class SQuestCanAcceptListDianKa  extends QuestCanAcceptList {

	public int compareTo(SQuestCanAcceptListDianKa o){
		return this.id-o.id;
	}

	
	public SQuestCanAcceptListDianKa(QuestCanAcceptList arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SQuestCanAcceptListDianKa(){
		super();
	}
	public SQuestCanAcceptListDianKa(SQuestCanAcceptListDianKa arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};