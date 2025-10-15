
package xbean;

public interface Gather extends mkdb.Bean {
	public Gather copy(); // deep clone
	public Gather toData(); // a Data instance
	public Gather toBean(); // a Bean instance
	public Gather toDataIf(); // a Data instance If need. else return this
	public Gather toBeanIf(); // a Bean instance If need. else return this

	public final static int GETITEM = 0; // 获得物品
	public final static int LAUNCHBATTLE = 1; // 开始战斗

	public long getGatherkey(); // 采集物key
	public int getResult(); // 采集的结果,战斗或者获得物品
	public long getEndgathertime(); // 开始采集的时间

	public void setGatherkey(long _v_); // 采集物key
	public void setResult(int _v_); // 采集的结果,战斗或者获得物品
	public void setEndgathertime(long _v_); // 开始采集的时间
}
