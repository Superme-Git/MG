
package xbean;

public interface Course extends mkdb.Bean {
	public Course copy(); // deep clone
	public Course toData(); // a Data instance
	public Course toBean(); // a Bean instance
	public Course toDataIf(); // a Data instance If need. else return this
	public Course toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // id
	public int getCount(); // 完成次数, 有的历程需要多次完成一个操作
	public int getState(); // 状态 0 未完成, 1 已经完成, 2 已经领奖

	public void setId(int _v_); // id
	public void setCount(int _v_); // 完成次数, 有的历程需要多次完成一个操作
	public void setState(int _v_); // 状态 0 未完成, 1 已经完成, 2 已经领奖
}
