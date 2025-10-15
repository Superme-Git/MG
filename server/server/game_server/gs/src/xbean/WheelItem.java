
package xbean;

public interface WheelItem extends mkdb.Bean {
	public WheelItem copy(); // deep clone
	public WheelItem toData(); // a Data instance
	public WheelItem toBean(); // a Bean instance
	public WheelItem toDataIf(); // a Data instance If need. else return this
	public WheelItem toBeanIf(); // a Bean instance If need. else return this

	public int getItemtype(); // 1为物品,2为经验,3为金钱
	public int getItemid(); // 金钱和经验的id为0
	public int getNum(); // 数量
	public int getTimes(); // 倍数,为实际倍数的10倍
	public int getBind(); // 是否绑定,只对物品有效
	public int getLimit(); // 最大上限,只对物品有效
	public int getMsgid(); // 发公告,只对物品有效

	public void setItemtype(int _v_); // 1为物品,2为经验,3为金钱
	public void setItemid(int _v_); // 金钱和经验的id为0
	public void setNum(int _v_); // 数量
	public void setTimes(int _v_); // 倍数,为实际倍数的10倍
	public void setBind(int _v_); // 是否绑定,只对物品有效
	public void setLimit(int _v_); // 最大上限,只对物品有效
	public void setMsgid(int _v_); // 发公告,只对物品有效
}
