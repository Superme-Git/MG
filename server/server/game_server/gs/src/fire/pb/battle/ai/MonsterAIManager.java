package fire.pb.battle.ai;

import java.util.HashMap;
import java.util.Map;

import fire.pb.battle.BattleField;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SMonsterConfig;
import fire.pb.npc.SWildMonsterConfig;
import fire.pb.util.Parser;
import fire.pb.util.Parser.ID2Odds;

public class MonsterAIManager
{
	private final static MonsterAIManager instance = new MonsterAIManager();
	private MonsterAIManager(){};
	public static MonsterAIManager getInstance()
	{
		return instance;
	}
	
	private Map<Integer,MonsterBattleAI> monsterAIs = new HashMap<Integer, MonsterBattleAI>();
	
	
	public Map<Integer, MonsterBattleAI> getMonsterAIs()
	{
		return monsterAIs;
	}
	public void init()
	{
		synchronized (monsterAIs)
		{
			monsterAIs.clear();
			for (SMonsterConfig monCfg : ConfigManager.getInstance().getConf(SMonsterConfig.class).values())
			{
				try
				{
					if (monCfg.aiIds != null && !monCfg.aiIds.equals(""))
					{
						MonsterBattleAI monsterAI = genMonsterAI(monCfg.id, monCfg.aiIds);
						if(monsterAI != null)
							monsterAIs.put(monsterAI.getMonsterId(), monsterAI);
					}
				} catch (Exception e)
				{
					BattleField.logger.error("Monster Id=" + monCfg.getId() + " AI init error.", e);
				}
			}

			for (SWildMonsterConfig monCfg : ConfigManager.getInstance().getConf(SWildMonsterConfig.class).values())
			{
				try
				{
					if (monCfg.aiIds != null && !monCfg.aiIds.equals(""))
					{
						MonsterBattleAI monsterAI = genMonsterAI(monCfg.id, monCfg.aiIds);
						if(monsterAI != null)
							monsterAIs.put(monsterAI.getMonsterId(), monsterAI);
					}
				} catch (Exception e)
				{
					BattleField.logger.error("Monster Id=" + monCfg.getId() + " AI init error.", e);
				}
			}
		}
		
	}
	
	public MonsterBattleAI genMonsterAI(int monsterId, String monsteraistr)
	{
		try
		{
			MonsterBattleAI monsterAI = new MonsterBattleAI(monsterId);
			String[] aistrs = monsteraistr.split(";");
			for (String aistr : aistrs)
			{
				String[] aiIdstr = aistr.split(",");
				if(aiIdstr.length == 1)
					monsterAI.getBattleAIs().add(Float.valueOf(aistr).intValue());
				else
				{
					ID2Odds id2odds = Parser.parseIdAndOddsWithComma(aistr);
					if(id2odds != null)
						monsterAI.getBattleAIOdds().add(id2odds);
				}
			}
			return monsterAI;
		}
		catch(Exception e)
		{
			BattleField.logger.error("Monster Id=" + monsterId + " AI init error.", e);
		}
		return null;
	}
	
}
