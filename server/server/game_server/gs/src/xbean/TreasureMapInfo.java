
package xbean;

public interface TreasureMapInfo extends mkdb.Bean {
	public TreasureMapInfo copy(); // deep clone
	public TreasureMapInfo toData(); // a Data instance
	public TreasureMapInfo toBean(); // a Bean instance
	public TreasureMapInfo toDataIf(); // a Data instance If need. else return this
	public TreasureMapInfo toBeanIf(); // a Bean instance If need. else return this

	public int getAwardid(); // 奖励id
	public int getTableindex(); // 奖励表里的唯一id
	public int getItemid(); // 使用的物品id
	public int getItemkey(); // 物品的key

	public void setAwardid(int _v_); // 奖励id
	public void setTableindex(int _v_); // 奖励表里的唯一id
	public void setItemid(int _v_); // 使用的物品id
	public void setItemkey(int _v_); // 物品的key
}
