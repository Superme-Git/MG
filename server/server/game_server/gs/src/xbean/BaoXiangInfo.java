
package xbean;

public interface BaoXiangInfo extends mkdb.Bean {
	public BaoXiangInfo copy(); // deep clone
	public BaoXiangInfo toData(); // a Data instance
	public BaoXiangInfo toBean(); // a Bean instance
	public BaoXiangInfo toDataIf(); // a Data instance If need. else return this
	public BaoXiangInfo toBeanIf(); // a Bean instance If need. else return this

	public int getOpentimes(); // 拾取次数
	public long getLastopentime(); // 上次拾取时间

	public void setOpentimes(int _v_); // 拾取次数
	public void setLastopentime(long _v_); // 上次拾取时间
}
