
package xbean;

public interface RoleLockInfo extends mkdb.Bean {
	public RoleLockInfo copy(); // deep clone
	public RoleLockInfo toData(); // a Data instance
	public RoleLockInfo toBean(); // a Bean instance
	public RoleLockInfo toDataIf(); // a Data instance If need. else return this
	public RoleLockInfo toBeanIf(); // a Bean instance If need. else return this

	public String getPassword(); // 安全锁密码,为空表示没有设置安全锁
	public com.locojoy.base.Octets getPasswordOctets(); // 安全锁密码,为空表示没有设置安全锁
	public long getUnlocktime(); // 开始解锁的时间,为0表示没解锁
	public long getForceunlocktime(); // 开始强行解锁的时间,为0表示没有申请强制解锁
	public int getErrortimes(); // 连续输错密码的次数
	public long getFullerrortime(); // 连续输错密码达上限的时间

	public void setPassword(String _v_); // 安全锁密码,为空表示没有设置安全锁
	public void setPasswordOctets(com.locojoy.base.Octets _v_); // 安全锁密码,为空表示没有设置安全锁
	public void setUnlocktime(long _v_); // 开始解锁的时间,为0表示没解锁
	public void setForceunlocktime(long _v_); // 开始强行解锁的时间,为0表示没有申请强制解锁
	public void setErrortimes(int _v_); // 连续输错密码的次数
	public void setFullerrortime(long _v_); // 连续输错密码达上限的时间
}
