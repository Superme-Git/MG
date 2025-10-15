
package xbean;

public interface PvP5Role extends mkdb.Bean {
	public PvP5Role copy(); // deep clone
	public PvP5Role toData(); // a Data instance
	public PvP5Role toBean(); // a Bean instance
	public PvP5Role toDataIf(); // a Data instance If need. else return this
	public PvP5Role toBeanIf(); // a Bean instance If need. else return this

	public int getGrade(); // 此记录所属的赛级
	public int getCamp(); // 所属阵营
	public int getExcellent(); // 是否优胜
	public int getScore(); // 积分
	public int getBattlenum(); // 本场比赛的次数
	public int getWinnum(); // 本场比赛赢的次数
	public int getContinuewinnum(); // 连胜次数
	public int getFirstwinaward(); // 首胜奖励
	public int getFivebattleaward(); // 五战奖励
	public long getLastbattletime(); // 上次战斗结束的时间
	public long getLastentertime(); // 上次进入赛场时间

	public void setGrade(int _v_); // 此记录所属的赛级
	public void setCamp(int _v_); // 所属阵营
	public void setExcellent(int _v_); // 是否优胜
	public void setScore(int _v_); // 积分
	public void setBattlenum(int _v_); // 本场比赛的次数
	public void setWinnum(int _v_); // 本场比赛赢的次数
	public void setContinuewinnum(int _v_); // 连胜次数
	public void setFirstwinaward(int _v_); // 首胜奖励
	public void setFivebattleaward(int _v_); // 五战奖励
	public void setLastbattletime(long _v_); // 上次战斗结束的时间
	public void setLastentertime(long _v_); // 上次进入赛场时间
}
