
package xbean;

public interface npcaward extends mkdb.Bean {
	public npcaward copy(); // deep clone
	public npcaward toData(); // a Data instance
	public npcaward toBean(); // a Bean instance
	public npcaward toDataIf(); // a Data instance If need. else return this
	public npcaward toBeanIf(); // a Bean instance If need. else return this

	public long getLasttime(); // 上次领取时间
	public int getCount(); // 已经领取次数
	public int getTotalcount(); // 领取总次数

	public void setLasttime(long _v_); // 上次领取时间
	public void setCount(int _v_); // 已经领取次数
	public void setTotalcount(int _v_); // 领取总次数
}
