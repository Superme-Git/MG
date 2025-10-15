
package xbean;

public interface ClanInfo extends mkdb.Bean {
	public ClanInfo copy(); // deep clone
	public ClanInfo toData(); // a Data instance
	public ClanInfo toBean(); // a Bean instance
	public ClanInfo toDataIf(); // a Data instance If need. else return this
	public ClanInfo toBeanIf(); // a Bean instance If need. else return this

	public int getIndex(); // 公会序号
	public long getKey(); // 公会表里的key
	public String getClanname(); // 公会名字
	public com.locojoy.base.Octets getClannameOctets(); // 公会名字
	public String getOldclanname(); // 公会曾用名
	public com.locojoy.base.Octets getOldclannameOctets(); // 公会曾用名
	public long getCreator(); // 公会创始人
	public int getPopularity(); // 公会人气度
	public int getClanlevel(); // 公会级别
	public int getClanlevelmax(); // 公会增加到达的最大等级 by changhao
	public String getClanaim(); // 公会宗旨
	public com.locojoy.base.Octets getClanaimOctets(); // 公会宗旨
	public long getClanmaster(); // 会长
	public long getClanvicemaster(); // 副会长
	public int getClanrank(); // 公会排名
	public java.util.Map<Integer, xbean.ClanSkill> getClanskillmap(); // 公会技能信息
	public java.util.Map<Integer, xbean.ClanSkill> getClanskillmapAsData(); // 公会技能信息
	public java.util.Map<Long, xbean.ClanMemberInfo> getMembers(); // 公会成员
	public java.util.Map<Long, xbean.ClanMemberInfo> getMembersAsData(); // 公会成员
	public java.util.Map<Long, Long> getApplicants(); // 申请加入公会人员
	public java.util.Map<Long, Long> getApplicantsAsData(); // 申请加入公会人员
	public long getCreatetime(); // 公会创建时间
	public int getMoney(); // 公会资金 by changhao
	public java.util.Map<Integer, Integer> getHouse(); // 2金库3药房4旅馆  对应等级  by changhao
	public java.util.Map<Integer, Integer> getHouseAsData(); // 2金库3药房4旅馆  对应等级  by changhao
	public int getBonus(); // 公会分红 by changhao
	public int getAutostate(); // 公会自动接收申请人入会的状态：0关闭 1开启
	public int getTotalscore(); // 公会总评分 by changhao
	public int getFiremembernum(); // 每日踢出次数
	public int getClaninstservice(); // 公会选择的副本服务编号
	public java.util.List<xbean.ClanEventRecord> getClaneventrecordlist(); // 公会事件
	public java.util.List<xbean.ClanEventRecord> getClaneventrecordlistAsData(); // 公会事件
	public short getRequestlevel(); // 公会自动接收申请人入会的等级
	public xbean.ImpeachMent getImpeachment(); // 弹劾
	public int getClandkp(); // 本周公会dkp
	public int getLastclandkp(); // 上周公会dkp

	public void setIndex(int _v_); // 公会序号
	public void setKey(long _v_); // 公会表里的key
	public void setClanname(String _v_); // 公会名字
	public void setClannameOctets(com.locojoy.base.Octets _v_); // 公会名字
	public void setOldclanname(String _v_); // 公会曾用名
	public void setOldclannameOctets(com.locojoy.base.Octets _v_); // 公会曾用名
	public void setCreator(long _v_); // 公会创始人
	public void setPopularity(int _v_); // 公会人气度
	public void setClanlevel(int _v_); // 公会级别
	public void setClanlevelmax(int _v_); // 公会增加到达的最大等级 by changhao
	public void setClanaim(String _v_); // 公会宗旨
	public void setClanaimOctets(com.locojoy.base.Octets _v_); // 公会宗旨
	public void setClanmaster(long _v_); // 会长
	public void setClanvicemaster(long _v_); // 副会长
	public void setClanrank(int _v_); // 公会排名
	public void setCreatetime(long _v_); // 公会创建时间
	public void setMoney(int _v_); // 公会资金 by changhao
	public void setBonus(int _v_); // 公会分红 by changhao
	public void setAutostate(int _v_); // 公会自动接收申请人入会的状态：0关闭 1开启
	public void setTotalscore(int _v_); // 公会总评分 by changhao
	public void setFiremembernum(int _v_); // 每日踢出次数
	public void setClaninstservice(int _v_); // 公会选择的副本服务编号
	public void setRequestlevel(short _v_); // 公会自动接收申请人入会的等级
	public void setClandkp(int _v_); // 本周公会dkp
	public void setLastclandkp(int _v_); // 上周公会dkp
}
