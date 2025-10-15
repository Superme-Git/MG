
package xbean;

public interface ClanFightBattleField extends mkdb.Bean {
	public ClanFightBattleField copy(); // deep clone
	public ClanFightBattleField toData(); // a Data instance
	public ClanFightBattleField toBean(); // a Bean instance
	public ClanFightBattleField toDataIf(); // a Data instance If need. else return this
	public ClanFightBattleField toBeanIf(); // a Bean instance If need. else return this

	public final static int STATE_CLAN_FIGHT_NORMAL = 1; // 战场正常状态 by changhao
	public final static int STATE_CLAN_FIGHT_OVER = 2; // 战场结束 by changhao

	public int getBattlefieldid(); // excel表里的ID by changhao
	public long getClanfightid(); // 公会战id by changhao
	public int getState(); // 战场状态 by changhao
	public long getClanid1(); // 公会1 by changhao
	public long getClanid2(); // 公会2 by changhao
	public String getClanname1(); // 公会名字1 by changhao
	public com.locojoy.base.Octets getClanname1Octets(); // 公会名字1 by changhao
	public String getClanname2(); // 公会名字2 by changhao
	public com.locojoy.base.Octets getClanname2Octets(); // 公会名字2 by changhao
	public int getClanscore1(); // 公会1积分 by changhao
	public int getClanscore2(); // 公会2积分 by changhao
	public java.util.Map<Long, Integer> getClan1roleids(); // 进入公会战场的 公会1玩家 by changhao
	public java.util.Map<Long, Integer> getClan1roleidsAsData(); // 进入公会战场的 公会1玩家 by changhao
	public java.util.Map<Long, Integer> getClan2roleids(); // 进入公会战场的 公会2玩家 by changhao
	public java.util.Map<Long, Integer> getClan2roleidsAsData(); // 进入公会战场的 公会2玩家 by changhao
	public java.util.Map<Long, String> getEnterroleids(); // 进入过战场的角色id by changhao
	public java.util.Map<Long, String> getEnterroleidsAsData(); // 进入过战场的角色id by changhao
	public java.util.Map<Long, Integer> getClan1enterroleids(); // 公会1战场进入过的玩家 by changhao
	public java.util.Map<Long, Integer> getClan1enterroleidsAsData(); // 公会1战场进入过的玩家 by changhao
	public java.util.Map<Long, Integer> getClan2enterroleids(); // 公会2战场进入过的玩家 by changhao
	public java.util.Map<Long, Integer> getClan2enterroleidsAsData(); // 公会2战场进入过的玩家 by changhao
	public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklist(); // 当前战场排名1 by changhao
	public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklistAsData(); // 当前战场排名1 by changhao
	public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklist(); // 当前战场排名2 by changhao
	public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklistAsData(); // 当前战场排名2 by changhao
	public java.util.Map<Long, Integer> getClan1rolerankmap(); // key是角色id value是排名(从0开始) by changhao
	public java.util.Map<Long, Integer> getClan1rolerankmapAsData(); // key是角色id value是排名(从0开始) by changhao
	public java.util.Map<Long, Integer> getClan2rolerankmap(); // key是角色id value是排名(从0开始) by changhao
	public java.util.Map<Long, Integer> getClan2rolerankmapAsData(); // key是角色id value是排名(从0开始) by changhao
	public long getLastclan1rolesortstamp(); // 上次排序时间戳 by changhao
	public long getLastclan2rolesortstamp(); // 上次排序时间戳 by changhao
	public java.util.List<Long> getVictoryboxids(); // 胜利宝箱链表 by changhao
	public java.util.List<Long> getVictoryboxidsAsData(); // 胜利宝箱链表 by changhao
	public java.util.List<Long> getCelebrateboxids(); // 庆祝宝箱链表 by changhao
	public java.util.List<Long> getCelebrateboxidsAsData(); // 庆祝宝箱链表 by changhao
	public long getDatetime(); // 周几那场比赛时间 by changhao
	public long getActivitystarttime(); // 公会战活动开始时间 by changhao
	public long getActivitypreparetime(); // 公会战准备结束时间 by changhao
	public long getActivitynoentertime(); // 公会战不能在进入时间 by changhao
	public long getActivityendtime(); // 公会战活动结束时间 by changhao
	public int getWhichwin(); // -1没结果0是第一个公会1是第二个公会 by changhao

	public void setBattlefieldid(int _v_); // excel表里的ID by changhao
	public void setClanfightid(long _v_); // 公会战id by changhao
	public void setState(int _v_); // 战场状态 by changhao
	public void setClanid1(long _v_); // 公会1 by changhao
	public void setClanid2(long _v_); // 公会2 by changhao
	public void setClanname1(String _v_); // 公会名字1 by changhao
	public void setClanname1Octets(com.locojoy.base.Octets _v_); // 公会名字1 by changhao
	public void setClanname2(String _v_); // 公会名字2 by changhao
	public void setClanname2Octets(com.locojoy.base.Octets _v_); // 公会名字2 by changhao
	public void setClanscore1(int _v_); // 公会1积分 by changhao
	public void setClanscore2(int _v_); // 公会2积分 by changhao
	public void setLastclan1rolesortstamp(long _v_); // 上次排序时间戳 by changhao
	public void setLastclan2rolesortstamp(long _v_); // 上次排序时间戳 by changhao
	public void setDatetime(long _v_); // 周几那场比赛时间 by changhao
	public void setActivitystarttime(long _v_); // 公会战活动开始时间 by changhao
	public void setActivitypreparetime(long _v_); // 公会战准备结束时间 by changhao
	public void setActivitynoentertime(long _v_); // 公会战不能在进入时间 by changhao
	public void setActivityendtime(long _v_); // 公会战活动结束时间 by changhao
	public void setWhichwin(int _v_); // -1没结果0是第一个公会1是第二个公会 by changhao
}
