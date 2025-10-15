
package xbean;

public interface HuoBanInfo extends mkdb.Bean {
	public HuoBanInfo copy(); // deep clone
	public HuoBanInfo toData(); // a Data instance
	public HuoBanInfo toBean(); // a Bean instance
	public HuoBanInfo toDataIf(); // a Data instance If need. else return this
	public HuoBanInfo toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 编号
	public int getLevel(); // 等级
	public int getColor(); // 颜色
	public int getInfight(); // 是否参战,1为参战
	public int getState(); // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
	public int getType(); // 伙伴类型 法攻,物攻,辅助,治疗,封印
	public long getTimes(); // 到期时间;当state为1时,此值无效
	public long getSettimes(); // 最近设置时间
	public int getWeekindex(); // 对应周免费的id

	public void setId(int _v_); // 编号
	public void setLevel(int _v_); // 等级
	public void setColor(int _v_); // 颜色
	public void setInfight(int _v_); // 是否参战,1为参战
	public void setState(int _v_); // 是否解锁, 0为未解锁; 1为永久使用; 2为本周免费; 3为免费剩余天数
	public void setType(int _v_); // 伙伴类型 法攻,物攻,辅助,治疗,封印
	public void setTimes(long _v_); // 到期时间;当state为1时,此值无效
	public void setSettimes(long _v_); // 最近设置时间
	public void setWeekindex(int _v_); // 对应周免费的id
}
