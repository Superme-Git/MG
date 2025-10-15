
package xbean;

public interface BattleInfo extends mkdb.Bean {
	public BattleInfo copy(); // deep clone
	public BattleInfo toData(); // a Data instance
	public BattleInfo toBean(); // a Bean instance
	public BattleInfo toDataIf(); // a Data instance If need. else return this
	public BattleInfo toBeanIf(); // a Bean instance If need. else return this

	public final static int PHASE_GOING = 0; // 战斗进行中
	public final static int PHASE_WAITOPERATION = 1; // 等待角色选择操作
	public final static int PHASE_WAITFEEDBACK = 2; // 等待角色反馈操作结束
	public final static int AREA_BATTLEHIDE = 1; // 暗雷
	public final static int AREA_BATTLESHOW = 2; // 明雷
	public final static int MAX_ROUNDNUM = 150; // 战斗最多持续多少回合
	public final static int DATA_MONSTER_NUM = 1; // 进入战斗时怪物的个数，只统计客方怪物的数量，不算主方怪物
	public final static int DATA_MONSTER_CHIEF_NUM = 2; // 进入战斗时怪物头领的个数，只统计客方怪物的数量，不算主方怪物
	public final static int DATA_HOST_ROLE_NUM = 3; // 进入战斗时主队角色的个数
	public final static int DATA_GUEST_ROLE_NUM = 4; // 进入战斗时客队角色的个数
	public final static int DATA_HOST_ROLE_AVERAGE_LEVEL = 5; // 进入战斗时主队角色的平均等级
	public final static int DATA_GUEST_ROLE_AVERAGE_LEVEL = 6; // 进入战斗时主队角色的平均等级
	public final static int DATA_HOST_AVERAGE_CREDIT = 7; // 进入战斗时主队角色的平均切磋积分
	public final static int DATA_GUEST_AVERAGE_CREDIT = 8; // 进入战斗时客队角色的平均切磋积分
	public final static int DATA_MONSTER_AVERAGE_LEVEL = 9; // 入战斗时客方怪物的平均等级
	public final static int DATA_HOST_ROLE_MAX_LEVEL = 10; // 进入战斗时，主方角色的最大等级
	public final static int DATA_HOST_ROLE_MIN_LEVEL = 11; // 进入战斗时，主方角色的最小等级
	public final static int DATA_BATTLE_CONFIG_LEVEL = 12; // 进入战斗时，如果战斗配置表里有等级公式，计算出的等级

	public long getBattleid(); // 战斗ID
	public int getConfigid(); // 战斗配置ID，默认为0时不是通过战斗配置new的ID
	public int getBackground(); // 战斗地图
	public int getBgm(); // 背景音乐
	public int getEnvironment(); // 战场环境
	public int getGmactiontimetimes(); // 时间倍率应用除以10.0，用于调试，gm使用。
	public int getEnvchangefrom(); // 环境改变来源（0 = 场外， 1 = 场内）
	public int getPhase(); // 战斗的阶段
	public int getRound(); // 战斗当前的回合数
	public boolean getCanrunaway(); // 本场战斗是否能逃跑
	public int getAutokey(); // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
	public int getBattletype(); // 战斗类型
	public int getAreatype(); // 练功区类型
	public int getAreaconf(); // 练功区配置值
	public int getBabyflag(); // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
	public int getBabybagfull(); // 宠物栏是否满  0不满    1满
	public long getHostroleid(); // 发起战斗的主角或者队长的id
	public long getMonsterid(); // 明雷怪npckey
	public int getMonstertype(); // 怪物类型
	public java.util.List<fire.pb.battle.BattleEndHandler> getBattleendhandlers(); // 处理战斗结束的handler
	public int getHostform(); // 主方光环
	public int getGuestform(); // 客方光环
	public int getHostformlevel(); // 主方光环等级
	public int getGuestformlevel(); // 客方光环等级
	public float getHostformeffcet(); // 主对客方光环压制效果
	public java.util.List<fire.pb.battle.FighterInfo> getFigterinfoonbegin(); // 出战斗时的战斗者信息
	public java.util.Map<Long, Integer> getRoleids(); // 所有参战的角色ID : 角色的战斗索引
	public java.util.Map<Long, Integer> getRoleidsAsData(); // 所有参战的角色ID : 角色的战斗索引
	public java.util.NavigableMap<Integer, xbean.Fighter> getFighters(); // 所有的参战者 战斗索引 ： 战斗者 按索引排序
	public java.util.NavigableMap<Integer, xbean.Fighter> getFightersAsData(); // 所有的参战者 战斗索引 ： 战斗者 按索引排序
	public java.util.Map<Integer, fire.pb.battle.Fighter> getFighterobjects(); // 包装好的Fighter类，战斗开始时直接创建完，不用每次用的时候再new,注意使用前要锁rolelock
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
	public java.util.List<Long> getOfflineroleids(); // 战斗中掉线的角色，不能发送协议
	public java.util.List<Long> getOfflineroleidsAsData(); // 战斗中掉线的角色，不能发送协议
	public long getBattlebegintime(); // 战斗开始的时间
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
	public java.util.concurrent.ScheduledFuture<?> getTimeout(); // 超时检测
	public java.util.List<xbean.BattleAI> getBattleais(); // 挂在战场上的AI
	public java.util.List<xbean.BattleAI> getBattleaisAsData(); // 挂在战场上的AI
	public fire.script.FightJSEngine getEngine(); // 用于本场战斗的JS引擎
	public java.util.Map<Integer, fire.pb.battle.ActionChangedAttrs> getFighterchangedattrs(); // 战斗者每个回合改变的属性,key=fighterId
	public java.util.Map<Integer, Float> getBattledatas(); // 保存战斗相关的一些数据
	public java.util.Map<Integer, Float> getBattledatasAsData(); // 保存战斗相关的一些数据
	public fire.pb.battle.Fighter getFieldfighter(); // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
	public java.util.List<xbean.BattleEndProcedure> getEndprocedures(); // 战斗者出战斗时执行的Procedure
	public java.util.Map<Integer, xbean.RoundResultItems> getRoundresultitem(); // 整场战斗的demo
	public java.util.List<Integer> getItemtimelist(); // 整场战斗的demo时间
	public java.util.List<Integer> getItemtimelistAsData(); // 整场战斗的demo时间
	public java.util.List<fire.pb.battle.operate.Operate> getReliveacts(); // 复活后要出手的行动
	public java.util.Map<Long, Integer> getBattledeads(); // 第几回合死掉的角色
	public java.util.Map<Long, Integer> getBattledeadsAsData(); // 第几回合死掉的角色
	public int getBattlemaxround(); // 战斗的最大回合数 如果不设置默认为200回合
	public java.util.Map<Integer, Integer> getParams(); // 与战斗有关的一些参数
	public java.util.Map<Integer, Integer> getParamsAsData(); // 与战斗有关的一些参数
	public java.util.List<Long> getQuitteamroleids(); // 战斗中有退队角色,战后自动退队 by changhao
	public java.util.List<Long> getQuitteamroleidsAsData(); // 战斗中有退队角色,战后自动退队 by changhao
	public java.util.List<Long> getAbsentteamroleids(); // 战斗中有暂离角色,战后自动暂离 by changhao
	public java.util.List<Long> getAbsentteamroleidsAsData(); // 战斗中有暂离角色,战后自动暂离 by changhao
	public java.util.List<Long> getTeamidlist(); // 战斗里所有队伍id by changhao
	public java.util.List<Long> getTeamidlistAsData(); // 战斗里所有队伍id by changhao
	public int getLivedieteam(); // 是否是组队生死战0不是    1是

	public void setBattleid(long _v_); // 战斗ID
	public void setConfigid(int _v_); // 战斗配置ID，默认为0时不是通过战斗配置new的ID
	public void setBackground(int _v_); // 战斗地图
	public void setBgm(int _v_); // 背景音乐
	public void setEnvironment(int _v_); // 战场环境
	public void setGmactiontimetimes(int _v_); // 时间倍率应用除以10.0，用于调试，gm使用。
	public void setEnvchangefrom(int _v_); // 环境改变来源（0 = 场外， 1 = 场内）
	public void setPhase(int _v_); // 战斗的阶段
	public void setRound(int _v_); // 战斗当前的回合数
	public void setCanrunaway(boolean _v_); // 本场战斗是否能逃跑
	public void setAutokey(int _v_); // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
	public void setBattletype(int _v_); // 战斗类型
	public void setAreatype(int _v_); // 练功区类型
	public void setAreaconf(int _v_); // 练功区配置值
	public void setBabyflag(int _v_); // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
	public void setBabybagfull(int _v_); // 宠物栏是否满  0不满    1满
	public void setHostroleid(long _v_); // 发起战斗的主角或者队长的id
	public void setMonsterid(long _v_); // 明雷怪npckey
	public void setMonstertype(int _v_); // 怪物类型
	public void setHostform(int _v_); // 主方光环
	public void setGuestform(int _v_); // 客方光环
	public void setHostformlevel(int _v_); // 主方光环等级
	public void setGuestformlevel(int _v_); // 客方光环等级
	public void setHostformeffcet(float _v_); // 主对客方光环压制效果
	public void setBattlebegintime(long _v_); // 战斗开始的时间
	public void setRoundtime(long _v_); // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
	public void setScriptplaytime(long _v_); // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
	public void setBattleresult(int _v_); // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
	public void setTimeout(java.util.concurrent.ScheduledFuture<?> _v_); // 超时检测
	public void setEngine(fire.script.FightJSEngine _v_); // 用于本场战斗的JS引擎
	public void setFieldfighter(fire.pb.battle.Fighter _v_); // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
	public void setBattlemaxround(int _v_); // 战斗的最大回合数 如果不设置默认为200回合
	public void setLivedieteam(int _v_); // 是否是组队生死战0不是    1是
}
