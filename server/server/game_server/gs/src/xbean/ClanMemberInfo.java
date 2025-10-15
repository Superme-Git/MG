
package xbean;

public interface ClanMemberInfo extends mkdb.Bean {
	public ClanMemberInfo copy(); // deep clone
	public ClanMemberInfo toData(); // a Data instance
	public ClanMemberInfo toBean(); // a Bean instance
	public ClanMemberInfo toDataIf(); // a Data instance If need. else return this
	public ClanMemberInfo toBeanIf(); // a Bean instance If need. else return this

	public int getClanposition(); // 职位信息
	public boolean getIsbannedtalk(); // 是否被禁言
	public long getBannedtalktime(); // 被禁言的时间
	public String getSalutation(); // 公会中的称谓
	public com.locojoy.base.Octets getSalutationOctets(); // 公会中的称谓
	public long getJointime(); // 加入公会时间
	public int getOwnbonus(); // 可领取分红
	public long getBonusgettime(); // 领取分红时间
	public int getTasknum(); // 公会任务次数
	public int getWeekclanpoint(); // 周公会贡献度by hzl
	public int getPreweekclanpoint(); // 上周周公会贡献度by hzl
	public int getClanhelpnum(); // 公会本周援助次数
	public short getClaninstnum(); // 参加公会副本次数
	public long getClaninstnumtime(); // 记录参加公会副本次数时间
	public short getClanfightnum(); // 参加公会战次数
	public long getClanfightnumtime(); // 记录参加公会战次数时间

	public void setClanposition(int _v_); // 职位信息
	public void setIsbannedtalk(boolean _v_); // 是否被禁言
	public void setBannedtalktime(long _v_); // 被禁言的时间
	public void setSalutation(String _v_); // 公会中的称谓
	public void setSalutationOctets(com.locojoy.base.Octets _v_); // 公会中的称谓
	public void setJointime(long _v_); // 加入公会时间
	public void setOwnbonus(int _v_); // 可领取分红
	public void setBonusgettime(long _v_); // 领取分红时间
	public void setTasknum(int _v_); // 公会任务次数
	public void setWeekclanpoint(int _v_); // 周公会贡献度by hzl
	public void setPreweekclanpoint(int _v_); // 上周周公会贡献度by hzl
	public void setClanhelpnum(int _v_); // 公会本周援助次数
	public void setClaninstnum(short _v_); // 参加公会副本次数
	public void setClaninstnumtime(long _v_); // 记录参加公会副本次数时间
	public void setClanfightnum(short _v_); // 参加公会战次数
	public void setClanfightnumtime(long _v_); // 记录参加公会战次数时间
}
