
package xbean;

public interface InstanceNpc extends mkdb.Bean {
	public InstanceNpc copy(); // deep clone
	public InstanceNpc toData(); // a Data instance
	public InstanceNpc toBean(); // a Bean instance
	public InstanceNpc toDataIf(); // a Data instance If need. else return this
	public InstanceNpc toBeanIf(); // a Bean instance If need. else return this

	public int getNpcbaseid(); // npcbaseId
	public int getState(); // 状态 1 显示 2 隐藏

	public void setNpcbaseid(int _v_); // npcbaseId
	public void setState(int _v_); // 状态 1 显示 2 隐藏
}
