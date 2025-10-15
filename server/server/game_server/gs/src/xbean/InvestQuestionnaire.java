
package xbean;

public interface InvestQuestionnaire extends mkdb.Bean {
	public InvestQuestionnaire copy(); // deep clone
	public InvestQuestionnaire toData(); // a Data instance
	public InvestQuestionnaire toBean(); // a Bean instance
	public InvestQuestionnaire toDataIf(); // a Data instance If need. else return this
	public InvestQuestionnaire toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getAnswer(); // key为题目,value为答案
	public java.util.Map<Integer, Integer> getAnswerAsData(); // key为题目,value为答案

}
