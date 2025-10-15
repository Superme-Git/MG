
package xbean;

public interface GacdPicStatus extends mkdb.Bean {
	public GacdPicStatus copy(); // deep clone
	public GacdPicStatus toData(); // a Data instance
	public GacdPicStatus toBean(); // a Bean instance
	public GacdPicStatus toDataIf(); // a Data instance If need. else return this
	public GacdPicStatus toBeanIf(); // a Bean instance If need. else return this

	public long getPictime(); // 答图形码的时间
	public String getAnswer(); // 正确答案
	public com.locojoy.base.Octets getAnswerOctets(); // 正确答案
	public int getResult(); // 答对还是答错
	public long getQuestiontime(); // 答题的时间,30分钟内不再出题

	public void setPictime(long _v_); // 答图形码的时间
	public void setAnswer(String _v_); // 正确答案
	public void setAnswerOctets(com.locojoy.base.Octets _v_); // 正确答案
	public void setResult(int _v_); // 答对还是答错
	public void setQuestiontime(long _v_); // 答题的时间,30分钟内不再出题
}
