
package xbean;

public interface LevelSeal extends mkdb.Bean {
	public LevelSeal copy(); // deep clone
	public LevelSeal toData(); // a Data instance
	public LevelSeal toBean(); // a Bean instance
	public LevelSeal toDataIf(); // a Data instance If need. else return this
	public LevelSeal toBeanIf(); // a Bean instance If need. else return this

	public int getLevel(); // 到达等级
	public int getRolenum(); // 已经到达这个等级的人数

	public void setLevel(int _v_); // 到达等级
	public void setRolenum(int _v_); // 已经到达这个等级的人数
}
