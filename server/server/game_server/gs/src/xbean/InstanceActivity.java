
package xbean;

public interface InstanceActivity extends mkdb.Bean {
	public InstanceActivity copy(); // deep clone
	public InstanceActivity toData(); // a Data instance
	public InstanceActivity toBean(); // a Bean instance
	public InstanceActivity toDataIf(); // a Data instance If need. else return this
	public InstanceActivity toBeanIf(); // a Bean instance If need. else return this

	public final static int STATE_UNSTART = 0; // 
	public final static int STATE_START = 1; // 
	public final static int STATE_END = 2; // 

	public int getInstanceid(); // 副本id,跟任务配置.xlsx中的id对应
	public int getState(); // 状态,0=未开始；1=已开始；2=已结束
	public int getBaseid(); // 活动基础id
	public long getStarttime(); // 开始时间
	public long getEndtime(); // 结束时间

	public void setInstanceid(int _v_); // 副本id,跟任务配置.xlsx中的id对应
	public void setState(int _v_); // 状态,0=未开始；1=已开始；2=已结束
	public void setBaseid(int _v_); // 活动基础id
	public void setStarttime(long _v_); // 开始时间
	public void setEndtime(long _v_); // 结束时间
}
