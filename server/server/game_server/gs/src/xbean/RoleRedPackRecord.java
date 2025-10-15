
package xbean;

public interface RoleRedPackRecord extends mkdb.Bean {
	public RoleRedPackRecord copy(); // deep clone
	public RoleRedPackRecord toData(); // a Data instance
	public RoleRedPackRecord toBean(); // a Bean instance
	public RoleRedPackRecord toDataIf(); // a Data instance If need. else return this
	public RoleRedPackRecord toBeanIf(); // a Bean instance If need. else return this

	public long getSendnum(); // 发红包数量
	public long getSendgold(); // 发红包金币数量
	public long getSendfushi(); // 发红包符石数量
	public long getReceivenum(); // 收红包数量
	public long getReceivegold(); // 收红包金币数量
	public long getReceivefushi(); // 收红包符石数量

	public void setSendnum(long _v_); // 发红包数量
	public void setSendgold(long _v_); // 发红包金币数量
	public void setSendfushi(long _v_); // 发红包符石数量
	public void setReceivenum(long _v_); // 收红包数量
	public void setReceivegold(long _v_); // 收红包金币数量
	public void setReceivefushi(long _v_); // 收红包符石数量
}
