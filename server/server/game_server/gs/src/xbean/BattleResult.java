
package xbean;

public interface BattleResult extends mkdb.Bean {
	public BattleResult copy(); // deep clone
	public BattleResult toData(); // a Data instance
	public BattleResult toBean(); // a Bean instance
	public BattleResult toDataIf(); // a Data instance If need. else return this
	public BattleResult toBeanIf(); // a Bean instance If need. else return this

	public long getBattleid(); // 战斗ID
	public int getConfigid(); // 战斗配置ID，默认为0时不是通过战斗配置new的ID
	public int getEnvironment(); // 战场环境
	public int getPhase(); // 战斗的阶段
	public int getBattletype(); // 战斗类型
	public int getAreatype(); // 练功区类型
	public int getAreaconf(); // 练功区配置值
	public int getHostform(); // 主方光环
	public int getGuestform(); // 客方光环
	public int getMonstertype(); // 怪物类型
	public java.util.Map<Long, Integer> getRoleids(); // 所有参战的角色ID : 角色的战斗索引
	public java.util.Map<Long, Integer> getRoleidsAsData(); // 所有参战的角色ID : 角色的战斗索引
	public java.util.NavigableMap<Integer, xbean.Fighter> getFighters(); // 所有的参战者 战斗索引 ： 战斗者 按索引排序
	public java.util.NavigableMap<Integer, xbean.Fighter> getFightersAsData(); // 所有的参战者 战斗索引 ： 战斗者 按索引排序
	public java.util.Set<Integer> getSkipeddeadfighters(); // 本回合因为死亡而未出手的战斗者，出手结束清除
	public java.util.Set<Integer> getSkipeddeadfightersAsData(); // 本回合因为死亡而未出手的战斗者，出手结束清除
	public java.util.Map<Integer, xbean.Monster> getMonsters(); // 所有在该场战斗中生成的怪物/NPC，key为fighterId
	public java.util.Map<Integer, xbean.Monster> getMonstersAsData(); // 所有在该场战斗中生成的怪物/NPC，key为fighterId
	public java.util.List<xbean.FailedMonster> getFailedmonsters(); // 失败的怪物（包括己方客方）
	public java.util.List<xbean.FailedMonster> getFailedmonstersAsData(); // 失败的怪物（包括己方客方）
	public java.util.NavigableMap<Integer, xbean.Fighter> getInifighters(); // 把参战者的状态在战斗前保存一份,切磋pk会用到
	public java.util.NavigableMap<Integer, xbean.Fighter> getInifightersAsData(); // 把参战者的状态在战斗前保存一份,切磋pk会用到
	public java.util.Map<Long, Integer> getAudience(); // 观战的roleid battleindex
	public java.util.Map<Long, Integer> getAudienceAsData(); // 观战的roleid battleindex
	public java.util.NavigableMap<Integer, xbean.Fighter> getWatchers(); // 
	public java.util.NavigableMap<Integer, xbean.Fighter> getWatchersAsData(); // 
	public long getMonsterid(); // 明雷怪id
	public int getRound(); // 战斗当前的回合数
	public long getRoundtime(); // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
	public long getScriptplaytime(); // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
	public java.util.Map<Integer, xbean.Decision> getDecisions(); // 战斗单元决策结果
	public java.util.Map<Integer, xbean.Decision> getDecisionsAsData(); // 战斗单元决策结果
	public java.util.Set<Long> getFeedbacks(); // roleid				角色的反馈记录
	public java.util.Set<Long> getFeedbacksAsData(); // roleid				角色的反馈记录
	public int getBattleresult(); // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
	public java.util.Map<Integer, Integer> getRoundresult(); // 战斗单位：战斗结果
	public java.util.Map<Integer, Integer> getRoundresultAsData(); // 战斗单位：战斗结果
	public java.util.Map<Integer, xbean.Protectors> getProtectormap(); // 被保护人：保护者列表
	public java.util.Map<Integer, xbean.Protectors> getProtectormapAsData(); // 被保护人：保护者列表
	public java.util.Map<Integer, Float> getBattledatas(); // 保存战斗相关的一些数据
	public java.util.Map<Integer, Float> getBattledatasAsData(); // 保存战斗相关的一些数据

	public void setBattleid(long _v_); // 战斗ID
	public void setConfigid(int _v_); // 战斗配置ID，默认为0时不是通过战斗配置new的ID
	public void setEnvironment(int _v_); // 战场环境
	public void setPhase(int _v_); // 战斗的阶段
	public void setBattletype(int _v_); // 战斗类型
	public void setAreatype(int _v_); // 练功区类型
	public void setAreaconf(int _v_); // 练功区配置值
	public void setHostform(int _v_); // 主方光环
	public void setGuestform(int _v_); // 客方光环
	public void setMonstertype(int _v_); // 怪物类型
	public void setMonsterid(long _v_); // 明雷怪id
	public void setRound(int _v_); // 战斗当前的回合数
	public void setRoundtime(long _v_); // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
	public void setScriptplaytime(long _v_); // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
	public void setBattleresult(int _v_); // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
}
