
package xbean;

public interface CardInfo extends mkdb.Bean {
	public CardInfo copy(); // deep clone
	public CardInfo toData(); // a Data instance
	public CardInfo toBean(); // a Bean instance
	public CardInfo toDataIf(); // a Data instance If need. else return this
	public CardInfo toBeanIf(); // a Bean instance If need. else return this

	public int getUserid(); // 
	public long getRoleid(); // 
	public long getUsecardtime(); // 用掉新手卡的时间
	public String getAnswer(); // 新手卡号
	public com.locojoy.base.Octets getAnswerOctets(); // 新手卡号
	public int getParenttype(); // 父类型
	public int getType(); // 子类型

	public void setUserid(int _v_); // 
	public void setRoleid(long _v_); // 
	public void setUsecardtime(long _v_); // 用掉新手卡的时间
	public void setAnswer(String _v_); // 新手卡号
	public void setAnswerOctets(com.locojoy.base.Octets _v_); // 新手卡号
	public void setParenttype(int _v_); // 父类型
	public void setType(int _v_); // 子类型
}
