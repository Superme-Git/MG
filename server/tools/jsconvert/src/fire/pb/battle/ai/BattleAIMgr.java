package fire.pb.battle.ai;

import java.util.Map;
import java.util.TreeMap;

import fire.pb.battle.SCondition;
import fire.pb.battle.STargetFilter;
import fire.pb.main.ConfigMgr;
import fire.script.JavaScript;


public class BattleAIMgr
{
	
	private final static BattleAIMgr instance = new BattleAIMgr();
	private BattleAIMgr(){};
	public static BattleAIMgr getInstance()
	{
		return instance;
	}

	
	Map<Integer,Condition> conditions = new TreeMap<Integer, Condition>();
	public Map<Integer,Condition> getConditions()
	{
		return conditions;
	}
	
	Map<Integer,TargetFilter> targetFilters = new TreeMap<Integer, TargetFilter>();
	
	public Map<Integer,TargetFilter> getTargetFilters()
	{
		return  targetFilters;
	}
	
	public void initBattleAI()
	{
		//parse targetfilter
		Map<Integer,STargetFilter> stargetFilters = ConfigMgr.getInstance().getConf(fire.pb.battle.STargetFilter.class);
		synchronized (targetFilters)
		{
			targetFilters.clear();
			for (STargetFilter sfilter : stargetFilters.values())
			{
				TargetFilter filter = new TargetFilter(sfilter.id);
				
				if (sfilter.buffs != null)
				{
					try
					{
						String[] strs = sfilter.buffs.split("_");
						for (int i = 1; i < strs.length; i += 2)
						{
							int buffId = Integer.valueOf(strs[i]);
							if (buffId > 0)
								filter.buffs.add(buffId);
						}
						filter.buffjs = new JavaScript(sfilter.buffs);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				targetFilters.put(filter.id, filter);
			}
		}
		
		//parse condition
		Map<Integer,SCondition> sconditions = ConfigMgr.getInstance().getConf(fire.pb.battle.SCondition.class);
		synchronized (conditions)
		{
			conditions.clear();
			for (SCondition scondition : sconditions.values())
			{
				try
				{
					Condition condition = new Condition(scondition.id);
					if(scondition.conditionscript != null && !scondition.conditionscript.equals(""))
						condition.jscondition = new JavaScript(scondition.conditionscript);
					if (scondition.targetfilters != null && !scondition.targetfilters.equals(""))
					{
						
						try
						{
							String[] strs = scondition.targetfilters.split("_");
							for (int i = 1; i < strs.length; i += 2)
							{
								int targetfilterId = Integer.valueOf(strs[i]);
								if (targetfilterId > 0)
									condition.targetFilterIds.add(targetfilterId);
							}
							condition.strGoalRequire =  new JavaScript(scondition.targetfilters);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					conditions.put(condition.id, condition);
				} catch (Exception e)
				{
//					BattleField.logger.error("战斗AI条件初始化失败，ConditionId = " + scondition.id,e);
					System.out.println("战斗AI条件初始化失败，ConditionId = " + scondition.id + e);
				}
			}
		}

//		MonsterAIManager.getInstance().init();
	}
}
