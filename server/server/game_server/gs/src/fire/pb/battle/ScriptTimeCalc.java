package fire.pb.battle;

import java.util.List;
import java.util.Properties;

import fire.pb.main.ConfigManager;
import fire.pb.skill.SActionTime;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.util.FireProp;

public class ScriptTimeCalc
{
	public final static int ATTACK_TIME_ID = 1;//普通攻击	
	public final static int COMBO_ATTACK_TIME_ID = 2;//普通攻击额外攻击次数（连击、追击）每次附加时间	
	public final static int USE_ITEM_TIME_ID = 3;//使用物品
	public final static int SUMMON_TIME_ID = 6;//召唤宠物
	public final static int SUMMON_BACK_TIME_ID = 7;//召还宠物
	public final static int INSTANT_SUMMON_TIME_ID = 8;//瞬时召唤
	public final static int CATCH_TIME_ID = 9;//捕捉
	public final static int RUNAWAY_TIME_ID = 10;//逃跑
	public final static int INSTANT_RUNAWAY_TIME_ID = 11;//瞬时逃跑
	public final static int OPER_FAIL_TIME_ID = 12;//操作失败
	public final static int ATTACK_BACK_TIME_ID = 14;//反击
	public final static int DEATH_TIME_ID = 22;//死亡/鬼魂
	public final static int KICK_OUT_TIME_ID = 23;//被击飞
	public final static int CHUN_GE_TIME_ID = 24;//原地复活
	public final static int ROUND_END_TIME_ID = 25;//回合末结算
	public final static int BETWEEN_ACTION_TIME_ID = 26;//行动间隙填充时间
	public final static int TOGATHER_ATTACK_TIME_ID = 28;//合击
	public final static int MAX_SKILL_TIME_ID = 29;//技能招式最大时间
	public final static int MIN_SKILL_TIME_ID = 30;//技能招式最小时间
	public final static int MIN_TIME_PERCENT_ID = 31;//最小时间百分比,100为底
	public final static int MAX_TIME_PERCENT_ID = 32;//最大时间百分比,100为底
	public final static int MIN_ROUND_TIME_ID = 33;//回合最小时间
	public final static int MAX_ROUND_TIME_ID = 34;//回合最大时间
	public final static int DEFAULT_RESULT_TIME_ID = 35;//默认动作时间
	public final static int SKILL_NAME_TIME_ID = 36;//技能名称显示时间
	public final static int DODGE_TIME_ID = 37;//躲闪时间
	public final static int BE_ATTACK_TIME_ID = 38;//受击时间
	public final static int AI_ACTION_TIME = 40;//客户端ai动作时间
	public final static int SUBSKILL_ACTION_TIME = 41;//子技能动作时间
	
	public static int calcScriptTime(List<ResultItem> script, List<fire.pb.battle.AIOperation> clientAIActions, List<Integer> itemtimelist)
	{
		if(script.isEmpty())
			return getScriptTimeByTimeId(MIN_ROUND_TIME_ID);//一个脚本都没有
		
		int sumtime = 0;
		int roundendtime = 0;
		
		for(ResultItem item : script)
		{
			int t = getResultItemTime(item);
			if(item.execute.operationtype == OperationType.ACTION_ROUNDENDDEMO)
			{
				if(t > roundendtime)
					roundendtime = t;
			}
			else
			{
				if(t > 0)
					itemtimelist.add(t);
				sumtime += (t + getScriptTimeByTimeId(BETWEEN_ACTION_TIME_ID));
			}
		}
		sumtime += roundendtime;
		sumtime += (clientAIActions.size() * AI_ACTION_TIME) + EXT_SCRIPT_PLAY_TIME;
		return Math.max(getScriptTimeByTimeId(MIN_ROUND_TIME_ID),Math.min(sumtime, getScriptTimeByTimeId(MAX_ROUND_TIME_ID)));
	}
	
	private static int getResultItemTime(ResultItem item)
	{
		switch(item.execute.operationtype)
		{
		case OperationType.ACTION_DEFEND: // 防御
		case OperationType.ACTION_PROTECT: // 保护
		case OperationType.ACTION_REST: // 休息
		case OperationType.ACTION_BATTLE_END: // 战斗结束,只有AI怪的AI指令有这个Action
			return 0;
		case OperationType.ACTION_ATTACK: // 攻击
			return getAttackTime(item);
		case OperationType.ACTION_SKILL: // 使用技能
			return getNormalSkillTime(item);
		case OperationType.ACTION_USEITEM: // 使用物品
			return getUseItemTime(item);
		case OperationType.ACTION_SUMMON: // 召唤宠物
			return getScriptTimeByTimeId(SUMMON_TIME_ID);
		case OperationType.ACTION_WITHDRAW: // 召还宠物
			return getScriptTimeByTimeId(SUMMON_BACK_TIME_ID);
		case OperationType.ACTION_CATHCH: // 捕捉
			return getScriptTimeByTimeId(CATCH_TIME_ID);
		case OperationType.ACTION_ESCAPE: // 逃跑
			return getScriptTimeByTimeId(RUNAWAY_TIME_ID);
		case OperationType.ACTION_UNIQUE_SKILL: // 绝技
		case OperationType.ACTION_SPECIAL_SKILL: // 特殊技能
			return getNormalSkillTime(item);
		case OperationType.ACTION_SUMMON_INSTANT: // 瞬时召唤
			return getScriptTimeByTimeId(INSTANT_SUMMON_TIME_ID);
		case OperationType.ACTION_ESCAPE_INSTANT: // 瞬时逃跑
			return getScriptTimeByTimeId(INSTANT_RUNAWAY_TIME_ID);
		case OperationType.ACTION_FAILURE: // 操作失败
		case OperationType.ACTION_FAILURE_NO_WONDER: // 操作失败
			return getScriptTimeByTimeId(OPER_FAIL_TIME_ID);
		case OperationType.ACTION_ENVIRONMENTDEMO: // 不带施法者的Demo，attackID填0
			return getNormalSkillTime(item);
		case OperationType.ACTION_ENVIRONMENTCHANGE: // 战场环境改变 operateid填战场环境id
			return getScriptTimeByTimeId(DEFAULT_RESULT_TIME_ID);
		case OperationType.ACTION_ROUNDENDDEMO: //  回合末结算demo
			return getRoundEndTime(item);
		}
		return getScriptTimeByTimeId(DEFAULT_RESULT_TIME_ID);
	}
	
	private static int getRoundEndTime(ResultItem item)
	{
		int t = getScriptTimeByTimeId(ROUND_END_TIME_ID);
		for(SubResultItem subitem : item.subresultlist)
		{
			for(DemoResult demo : subitem.resultlist)
			{
				int deathtime = getDeathAddTime(demo);
				if(t < deathtime)
					t = deathtime;
			}
		}
		return t;
	}
	
	private static DemoResult getLastDemo(ResultItem item)
	{
		DemoResult lastdemo = null;
		if(item.subresultlist.size() > 0)
		{
			SubResultItem lastsubitem = item.subresultlist.get(item.subresultlist.size() - 1);
			if(lastsubitem != null)
			{
				lastdemo = lastsubitem.resultlist.get(lastsubitem.resultlist.size() - 1);
			}
		}
		return lastdemo;
	}
	
	/**
	 * 计算普通攻击时间
	 * @param item
	 * @return
	 */
	private static int getAttackTime(ResultItem item)
	{
		int time = 0;
		for(SubResultItem subitem: item.subresultlist)
		{
			time += getSubAttackTime(subitem);
		}
		return time;
	}

	
	private static DemoResult getSubLastDemo(SubResultItem subitem)
	{
		DemoResult lastdemo = null;
		if(subitem.resultlist.size() > 0)
		{
			lastdemo = subitem.resultlist.get(subitem.resultlist.size() - 1);
		}
		return lastdemo;
	}
	
	/**
	 * 计算普通攻击时间
	 * @param item
	 * @return
	 */
	private static int getSubAttackTime(SubResultItem item)
	{
		DemoResult lastdemo = getSubLastDemo(item);

		//先判断合击
		if(lastdemo != null && lastdemo.assisterid > 0)
			return getScriptTimeByTimeId(TOGATHER_ATTACK_TIME_ID);
		
		//单人攻击
		int time = getScriptTimeByTimeId(ATTACK_TIME_ID);//一次攻击的时间
		if(lastdemo != null)
		{
			time += (item.resultlist.size() - 1) * getScriptTimeByTimeId(COMBO_ATTACK_TIME_ID);//连击追击加成
			time += getDeathAddTime(lastdemo);//只验证最后一个的死亡
			//验证最后一个的反击
			if(lastdemo.attackback != 0)
				time += getScriptTimeByTimeId(ATTACK_BACK_TIME_ID);
		}
		return time;
	}
	
	private static final int RESULT_DEATH_GHOST = ResultType.RESULT_DEATH | ResultType.RESULT_GHOST;
	private static final int RESULT_DEAD_HALF_FULL_RELIVE = ResultType.RESULT_DEAD_FULL_RELIVE;
	
	/**
	 * 获取可能死亡击飞躲避的时间
	 * @param demo
	 * @return
	 */
	private static int getDeathAddTime(DemoResult demo)
	{
		if((demo.targetresult & ResultType.RESULT_KICKOUT) != 0
				||(demo.attackerresult & ResultType.RESULT_KICKOUT) != 0)
		{
			return getScriptTimeByTimeId(KICK_OUT_TIME_ID);
		}
		if((demo.targetresult & RESULT_DEATH_GHOST) != 0
				||(demo.attackerresult & RESULT_DEATH_GHOST) != 0)
		{
			return getScriptTimeByTimeId(DEATH_TIME_ID);
		}
		else if((demo.targetresult & RESULT_DEAD_HALF_FULL_RELIVE) != 0
				||(demo.attackerresult & RESULT_DEAD_HALF_FULL_RELIVE) != 0)
		{
			return getScriptTimeByTimeId(CHUN_GE_TIME_ID);
		}
		else if((demo.targetresult & ResultType.RESULT_DODGE) != 0
				||(demo.attackerresult & ResultType.RESULT_DODGE) != 0)
		{
			return getScriptTimeByTimeId(DODGE_TIME_ID);
		}
		
		return getScriptTimeByTimeId(BE_ATTACK_TIME_ID);
	}
	
	// 获取技能的
	private static int getNormalSkillTime(ResultItem item)
	{
		int time = 0;
		FightSkillConfig skillcfg = fire.pb.skill.Module.getInstance().getFightSkillConfig(item.execute.operationid);
		if(skillcfg == null)
			time = getScriptTimeByTimeId(DEFAULT_RESULT_TIME_ID);
		else
		{
			time = skillcfg.getPlayTime();
			time += skillcfg.getSkillSpecialAddTime(); //加上特殊延长时间 by changhao
		}
		
		int deathtime = 0;
		for(SubResultItem subitem : item.subresultlist)
		{
			float ftimerate = 1.0f;
			int costtime = fire.pb.skill.Module.getInstance().getSubSkillCostTimes(subitem.subskillid);
			if(costtime == 0)
				ftimerate = 0.0f;
			else if(costtime != -1)
				ftimerate = 0.5f;
			
			for(DemoResult demo : subitem.resultlist)
			{
				int t = getDeathAddTime(demo);
				if(t > deathtime)
					deathtime = t;
			}
			deathtime += (getScriptTimeByTimeId(SUBSKILL_ACTION_TIME)*ftimerate);
		}
		time += deathtime;
		time += getScriptTimeByTimeId(SKILL_NAME_TIME_ID);
		int mintime = getScriptTimeByTimeId(MIN_SKILL_TIME_ID);
		if(time < mintime)
			return mintime;
		int maxtime = getScriptTimeByTimeId(MAX_SKILL_TIME_ID);
		/*if(skillcfg == null)
			System.out.print("operationid:"+item.execute.operationid+" used time "+time+"\r\n"); 
		else
			System.out.print("operationid:"+skillcfg.getSkillIndex()+" used time "+time+"\r\n");
		*/
		if(time > maxtime)
			return maxtime;		
		return time;
	}
	
	private static int getUseItemTime(ResultItem item)
	{
		int time = getScriptTimeByTimeId(USE_ITEM_TIME_ID);
		DemoResult lastdemo = getLastDemo(item);
		if(lastdemo != null)
		{
			int t = getDeathAddTime(lastdemo);
			time += t;
		}
		return time;
	}

	/**
	 * 获取固定时间
	 * @param timeid
	 * @return
	 */
	public static int getScriptTimeByTimeId(int timeid)
	{
		SActionTime cfg = ConfigManager.getInstance().getConf(SActionTime.class).get(timeid);
		if(cfg == null)
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("没有配战斗时间参数的timeid = " + timeid);}
			return 0;
		}
		return cfg.actiontime;
	}
	public static int getMinScriptTime(int standardtime)
	{
		int minpercent = getScriptTimeByTimeId(MIN_TIME_PERCENT_ID);
		return standardtime * minpercent / 100; 
	}
	public static int getMaxScriptTime(int standardtime)
	{
		int maxpercent = getScriptTimeByTimeId(MAX_TIME_PERCENT_ID);
		return standardtime * maxpercent / 100;
	}
	static Properties prop = ConfigManager.getInstance().getPropConf("battle");
	protected static final float EXT_SCRIPT_PLAY_TIME = FireProp.getIntValue(prop, "mtgserv.ext_script_play_time"); //额外的脚本播放时间
	
}
