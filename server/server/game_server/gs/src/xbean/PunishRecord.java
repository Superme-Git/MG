
package xbean;

public interface PunishRecord extends mkdb.Bean {
	public PunishRecord copy(); // deep clone
	public PunishRecord toData(); // a Data instance
	public PunishRecord toBean(); // a Bean instance
	public PunishRecord toDataIf(); // a Data instance If need. else return this
	public PunishRecord toBeanIf(); // a Bean instance If need. else return this

	public final static int TYPE_FORBID_LOGIN = 1; // 禁止登陆
	public final static int TYPE_SHUTUP = 2; // 禁言
	public final static int TYPE_UNFORBID_LOGIN = 3; // 解除禁止登陆
	public final static int TYPE_UNSHUTUP = 4; // 解除禁言

	public int getUserid(); // 用户id
	public long getRoleid(); // 角色id，roleid为0时是对整个账户的处罚
	public int getType(); // 惩罚类型，参考下面
	public long getForbidtime(); // 封禁的持续时间
	public int getGmuserid(); // 封禁操作的GM用户id
	public long getOptime(); // 操作时间
	public String getReason(); // 原因
	public com.locojoy.base.Octets getReasonOctets(); // 原因

	public void setUserid(int _v_); // 用户id
	public void setRoleid(long _v_); // 角色id，roleid为0时是对整个账户的处罚
	public void setType(int _v_); // 惩罚类型，参考下面
	public void setForbidtime(long _v_); // 封禁的持续时间
	public void setGmuserid(int _v_); // 封禁操作的GM用户id
	public void setOptime(long _v_); // 操作时间
	public void setReason(String _v_); // 原因
	public void setReasonOctets(com.locojoy.base.Octets _v_); // 原因
}
