
package xbean;

public interface SingleCompensationAward extends mkdb.Bean {
	public SingleCompensationAward copy(); // deep clone
	public SingleCompensationAward toData(); // a Data instance
	public SingleCompensationAward toBean(); // a Bean instance
	public SingleCompensationAward toDataIf(); // a Data instance If need. else return this
	public SingleCompensationAward toBeanIf(); // a Bean instance If need. else return this

	public int getType(); // 奖励类型
	public long getId(); // 奖励id,类型是物品时为物品id
	public long getNum(); // 奖励数量
	public long getFlag(); // 奖励标记,类型是物品时有绑定标记

	public void setType(int _v_); // 奖励类型
	public void setId(long _v_); // 奖励id,类型是物品时为物品id
	public void setNum(long _v_); // 奖励数量
	public void setFlag(long _v_); // 奖励标记,类型是物品时有绑定标记
}
