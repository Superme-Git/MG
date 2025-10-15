
package xbean;

public interface AnYeTask extends mkdb.Bean {
	public AnYeTask copy(); // deep clone
	public AnYeTask toData(); // a Data instance
	public AnYeTask toBean(); // a Bean instance
	public AnYeTask toDataIf(); // a Data instance If need. else return this
	public AnYeTask toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 任务id
	public int getKind(); // 任务类型
	public int getState(); // 任务状态
	public int getDstitemid(); // 目的道具的id
	public int getDstitemnum(); // 目的道具的数量
	public long getDstnpckey(); // 目的npc的key
	public int getDstnpcid(); // 目的npc的id
	public long getParam1(); // 保留参数1
	public long getParam2(); // 保留参数2
	public long getParam3(); // 保留参数3
	public int getLegend(); // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
	public int getLegendtime(); // 传说持续时间
	public long getLegendend(); // 传说结束时间
	public int getLegenditem(); // 传说物品

	public void setId(int _v_); // 任务id
	public void setKind(int _v_); // 任务类型
	public void setState(int _v_); // 任务状态
	public void setDstitemid(int _v_); // 目的道具的id
	public void setDstitemnum(int _v_); // 目的道具的数量
	public void setDstnpckey(long _v_); // 目的npc的key
	public void setDstnpcid(int _v_); // 目的npc的id
	public void setParam1(long _v_); // 保留参数1
	public void setParam2(long _v_); // 保留参数2
	public void setParam3(long _v_); // 保留参数3
	public void setLegend(int _v_); // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
	public void setLegendtime(int _v_); // 传说持续时间
	public void setLegendend(long _v_); // 传说结束时间
	public void setLegenditem(int _v_); // 传说物品
}
