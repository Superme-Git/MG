
package xbean;

public interface TeamMatch extends mkdb.Bean {
	public TeamMatch copy(); // deep clone
	public TeamMatch toData(); // a Data instance
	public TeamMatch toBean(); // a Bean instance
	public TeamMatch toDataIf(); // a Data instance If need. else return this
	public TeamMatch toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 角色ID by changhao
	public int getMatchtype(); // 类型0是个人1是队伍 by changhao
	public int getTargetid(); // 目标ID by changhao
	public int getLevelmin(); // 需要最小等级 个人匹配这个忽略 by changhao
	public int getLevelmax(); // 需要最大等级 个人匹配这个忽略 by changhao
	public long getOnekeytimestamp(); // 一键喊话时间戳 by changhao
	public long getTimestamp(); // 匹配后的时间戳 by changhao

	public void setRoleid(long _v_); // 角色ID by changhao
	public void setMatchtype(int _v_); // 类型0是个人1是队伍 by changhao
	public void setTargetid(int _v_); // 目标ID by changhao
	public void setLevelmin(int _v_); // 需要最小等级 个人匹配这个忽略 by changhao
	public void setLevelmax(int _v_); // 需要最大等级 个人匹配这个忽略 by changhao
	public void setOnekeytimestamp(long _v_); // 一键喊话时间戳 by changhao
	public void setTimestamp(long _v_); // 匹配后的时间戳 by changhao
}
