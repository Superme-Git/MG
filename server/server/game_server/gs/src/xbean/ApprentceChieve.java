
package xbean;

public interface ApprentceChieve extends mkdb.Bean {
	public ApprentceChieve copy(); // deep clone
	public ApprentceChieve toData(); // a Data instance
	public ApprentceChieve toBean(); // a Bean instance
	public ApprentceChieve toDataIf(); // a Data instance If need. else return this
	public ApprentceChieve toBeanIf(); // a Bean instance If need. else return this

	public int getCurrnumber(); // 当前次数
	public int getTotal(); // 当前总量
	public int getFlag(); // 0=未完成 1=完成 2=已经领奖
	public int getContent(); // 记录一个数值

	public void setCurrnumber(int _v_); // 当前次数
	public void setTotal(int _v_); // 当前总量
	public void setFlag(int _v_); // 0=未完成 1=完成 2=已经领奖
	public void setContent(int _v_); // 记录一个数值
}
