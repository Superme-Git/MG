
package xbean;

public interface RequestRuneInfo extends mkdb.Bean {
	public RequestRuneInfo copy(); // deep clone
	public RequestRuneInfo toData(); // a Data instance
	public RequestRuneInfo toBean(); // a Bean instance
	public RequestRuneInfo toDataIf(); // a Data instance If need. else return this
	public RequestRuneInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 请求角色roleid
	public long getTargetroleid(); // 目标roleid
	public int getActiontype(); // 动作类型  0 请求符文    1捐献符文
	public long getRequesttime(); // 请求时间
	public int getItemid(); // 物品id
	public int getItemlevel(); // 物品等级

	public void setRoleid(long _v_); // 请求角色roleid
	public void setTargetroleid(long _v_); // 目标roleid
	public void setActiontype(int _v_); // 动作类型  0 请求符文    1捐献符文
	public void setRequesttime(long _v_); // 请求时间
	public void setItemid(int _v_); // 物品id
	public void setItemlevel(int _v_); // 物品等级
}
