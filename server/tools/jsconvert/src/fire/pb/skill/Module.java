package fire.pb.skill;

import java.util.HashMap;
import java.util.Map;

import fire.pb.main.IModule;
import fire.pb.main.ModuleMgr;
import fire.pb.skill.SSceneSkillConfig;
import fire.pb.skill.SSkillConfig;
import fire.pb.skill.SSubSkillConfig;
import fire.pb.skill.battle.BattleSkillConfig;
import fire.pb.skill.scene.SceneSkillConfig;
import fire.script.JavaScript;

public class Module implements IModule
{
	public final static String MODULE_NAME = "skill";
//	static public final Logger logger = Logger.getLogger("SKILL");
	Map<Integer, BattleSkillConfig> battleSkillConfigs = new HashMap<Integer, BattleSkillConfig>();
	Map<Integer, SceneSkillConfig> sceneSkillConfigs = new HashMap<Integer, SceneSkillConfig>();
	Map<Integer, SubSkillConfig> sSubSkillConfig = new HashMap<Integer, SubSkillConfig>();

	Map<String, Integer> Js2ids = new HashMap<String, Integer>();
	            		
	
	public static Module getInstance()
	{
		//不再用以上的单例模式，从ModuleManager获得，由其管理
		return ((fire.pb.skill.Module)ModuleMgr.getInstance().getModuleByName(MODULE_NAME));
	}


	public static java.util.Map<Integer, SSkillConfig> getSSkillConfigs()
	{
		return fire.pb.main.ConfigMgr.getInstance().getConf(fire.pb.skill.SSkillConfig.class);
	}

	@Override
	public void exit()
	{

	}

	@Override
	public synchronized void init() throws Exception
	{

		//logger.info("skill模块初始化开始");
		Map<Integer, SSkillConfig> sbattleSkillconfigs = fire.pb.main.ConfigMgr.getInstance().getConf(
		fire.pb.skill.SSkillConfig.class);
		initBattleSkillConfigs(sbattleSkillconfigs);
		//logger.info("加载战斗技能，个数：" + battleSkillConfigs.size());
		Map<Integer, SSceneSkillConfig> sSceneSkillconfigs = fire.pb.main.ConfigMgr.getInstance().getConf(
		fire.pb.skill.SSceneSkillConfig.class);
		initSceneSkillConfigs(sSceneSkillconfigs);
		Map<Integer, SSubSkillConfig> sSSubSkillConfig = fire.pb.main.ConfigMgr.getInstance().getConf(
		fire.pb.skill.SSubSkillConfig.class);
		initSSubSkillConfigs(sSSubSkillConfig);
		//logger.info("加载场景技能，个数：" + sSceneSkillconfigs.size());
		//logger.info("skill模块初始化完成");
	}
	
	private void initSSubSkillConfigs(Map<Integer, SSubSkillConfig> sSSubSkillConfig){
		sSubSkillConfig.clear();
		for (int skillId : sSSubSkillConfig.keySet()){
			try
			{
				SubSkillConfig subskill= new SubSkillConfig(skillId);
				SSubSkillConfig subconf = sSSubSkillConfig.get(skillId);
				subskill.setName(subconf.name);
			
				subskill.setRelevanceSkillID(subconf.relevanceSkillID);
				
				if (subconf.targetRate != null)
					subskill.setTargetRate(new JavaScript(subconf.targetRate));
				else
					subskill.setTargetRate(new JavaScript("1"));
				
				// 何种情况下执行本技能
				if (subconf.actionCondition != null)
				{
					String[] actionConditionstr = subconf.actionCondition.split(",");
					subskill.setActionConditions(new int[actionConditionstr.length]);
					for (int i = 0; i < actionConditionstr.length; i++)
					{
						subskill.getActionConditions()[i] = Integer.valueOf(actionConditionstr[i]);
		
					}
				}
				
				subskill.setAimCampType(subconf.aimCampType);
		
				// 选择目标单位类型
				if (subconf.aimRaceType != null)
				{
					String[] aimRaceTypestr = subconf.aimRaceType.split(",");
					subskill.setAimRaceTypes(new int[aimRaceTypestr.length]);
					for (int i = 0; i < aimRaceTypestr.length; i++)
					{
						subskill.getAimRaceTypes()[i] = Integer.valueOf(aimRaceTypestr[i]);
		
					}
				}

				// 选择目标单位关系
				if (subconf.aimRelationType != null)
				{
					String[] aimRelationTypestr = subconf.aimRelationType.split(",");
					subskill.setAimRelationTypes(new int[aimRelationTypestr.length]);
					for (int i = 0; i < aimRelationTypestr.length; i++)
					{
						subskill.getAimRelationTypes()[i] = Integer.valueOf(aimRelationTypestr[i]);

					}
				}						
				// 选择目标生效精准职业编号
				if (subconf.jobid != null)
				{
					String[] jobidstr = subconf.jobid.split(",");
					subskill.setjobids(new int[jobidstr.length]);
					for (int i = 0; i < jobidstr.length; i++)
					{
						subskill.getjobids()[i] = Integer.valueOf(jobidstr[i]);								
					}
				}
				
				// 选择目标单位关系
				if (subconf.monsterid != null)
				{
					String[] aimMonsteridsstr = subconf.monsterid.split(",");
					subskill.setaimMonsterids(new int[aimMonsteridsstr.length]);
					for (int i = 0; i < aimMonsteridsstr.length; i++)
					{
						subskill.getaimMonsterids()[i] = Integer.valueOf(aimMonsteridsstr[i]);								
					}
				}						
				
				if (subconf.confilictStates != null)
					subskill.setConfilictStateJs(new JavaScript(subconf.confilictStates));
				else
					subskill.setConfilictStateJs(new JavaScript("1"));					
				
				subskill.setConfilictStatesnotifyId (subconf.confilictStatesnotify);

				// buff0要求的前提buff
				if (subconf.requiredBuffs != null)
				{
					String[] buffidstr = subconf.requiredBuffs.split(";");
					subskill.setRequiredBuffIds(new int[buffidstr.length]);
					for (int i = 0; i < buffidstr.length; i++)
					{
						subskill.getRequiredBuffIds()[i] = Integer.valueOf(buffidstr[i]);
					}
				}
				// 与buff0释放冲突的buff
				if (subconf.confilictBuffs != null)
				{
					String[] buffidstr = subconf.confilictBuffs.split(";");
					subskill.setConfilictBuffIds(new int[buffidstr.length]);
					for (int i = 0; i < buffidstr.length; i++)
					{
						subskill.getConfilictBuffIds()[0] = Integer.valueOf(buffidstr[i]);
					}
				}							
				
				if (subconf.targetType != null)
					subskill.setTargettype(Integer.valueOf(subconf.targetType));
				if (subconf.targetNum != null)
					subskill.setTargetCountJs(new JavaScript(subconf.targetNum));
				else
					subskill.setTargetCountJs(new JavaScript("1"));
				
				if (subconf.minmagicdamage != null)
					subskill.setMinmagicdamageJs(new JavaScript(subconf.minmagicdamage));
				else
					subskill.setMinmagicdamageJs(new JavaScript("1"));
				
				subskill.setSortArg(subconf.sortArg);
				
				if (subconf.targetRate != null)
					subskill.setTargetRate(new JavaScript(subconf.targetRate));
				else
					subskill.setTargetRate(new JavaScript("1"));
				
						
				subskill.setBuffArgs(new BuffArg[4]);
				if (subconf.buff0_id != null)
				{
					subskill.getBuffArgs()[0] = new BuffArg(Integer.valueOf(subconf.buff0_id));
					if (subconf.buff0_rate != null)
						subskill.getBuffArgs()[0].rateJs = new JavaScript(subconf.buff0_rate);
					else
						subskill.getBuffArgs()[0].rateJs = new JavaScript("1");
					if (subconf.buff0_round != null)
						subskill.getBuffArgs()[0].roundJs = new JavaScript(subconf.buff0_round);
					else
						subskill.getBuffArgs()[0].roundJs = new JavaScript("0");
					subskill.getBuffArgs()[0].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff0_values);

					if (subconf.buff0_confilictStates != null)
						subskill.getBuffArgs()[0].confilictStateJs = new JavaScript(subconf.buff0_confilictStates);
					else
						subskill.getBuffArgs()[0].confilictStateJs = new JavaScript("1");					
					

					if (subconf.buff0_odds != null)
						subskill.getBuffArgs()[0].oddsJs = new JavaScript(subconf.buff0_odds);
					else
						subskill.getBuffArgs()[0].oddsJs = new JavaScript("1");		
					 
						
				}
				if (subconf.buff1_id != null)
				{
					subskill.getBuffArgs()[1] = new BuffArg(Integer.valueOf(subconf.buff1_id));
					if (subconf.buff1_rate != null)
						subskill.getBuffArgs()[1].rateJs = new JavaScript(subconf.buff1_rate);
					else
						subskill.getBuffArgs()[1].rateJs = new JavaScript("1");

					if (subconf.buff1_round != null)
						subskill.getBuffArgs()[1].roundJs = new JavaScript(subconf.buff1_round);
					else
						subskill.getBuffArgs()[1].roundJs = new JavaScript("0");
					subskill.getBuffArgs()[1].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff1_values);

					//	buff1状态过滤
					if (subconf.buff1_confilictStates != null)
						subskill.getBuffArgs()[1].confilictStateJs = new JavaScript(subconf.buff1_confilictStates);
					else
						subskill.getBuffArgs()[1].confilictStateJs = new JavaScript("1");					
					

					if (subconf.buff1_odds != null)
						subskill.getBuffArgs()[1].oddsJs = new JavaScript(subconf.buff1_odds);
					else
						subskill.getBuffArgs()[1].oddsJs = new JavaScript("1");	
					
				}
				if (subconf.buff2_id != null)
				{
					subskill.getBuffArgs()[2] = new BuffArg(Integer.valueOf(subconf.buff2_id));
					if (subconf.buff2_rate != null)
						subskill.getBuffArgs()[2].rateJs = new JavaScript(subconf.buff2_rate);
					else
						subskill.getBuffArgs()[2].rateJs = new JavaScript("1");

					if (subconf.buff2_round != null)
						subskill.getBuffArgs()[2].roundJs = new JavaScript(subconf.buff2_round);
					else
						subskill.getBuffArgs()[2].roundJs = new JavaScript("0");
					subskill.getBuffArgs()[2].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff2_values);

					//	buff2状态过滤
					if (subconf.buff2_confilictStates != null)
						subskill.getBuffArgs()[2].confilictStateJs = new JavaScript(subconf.buff2_confilictStates);
					else
						subskill.getBuffArgs()[2].confilictStateJs = new JavaScript("1");					
					
					if (subconf.buff2_odds != null)
						subskill.getBuffArgs()[2].oddsJs = new JavaScript(subconf.buff2_odds);
					else
						subskill.getBuffArgs()[2].oddsJs = new JavaScript("1");	

				}
				if (subconf.buff3_id != null)
				{
					subskill.getBuffArgs()[3] = new BuffArg(Integer.valueOf(subconf.buff3_id));
					if (subconf.buff3_rate != null)
						subskill.getBuffArgs()[3].rateJs = new JavaScript(subconf.buff3_rate);
					else
						subskill.getBuffArgs()[3].rateJs = new JavaScript("1");

					if (subconf.buff3_round != null)
						subskill.getBuffArgs()[3].roundJs = new JavaScript(subconf.buff3_round);
					else
						subskill.getBuffArgs()[3].roundJs = new JavaScript("0");
					subskill.getBuffArgs()[3].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff3_values);

					//	buff3状态过滤
					if (subconf.buff3_confilictStates != null)
						subskill.getBuffArgs()[3].confilictStateJs = new JavaScript(subconf.buff3_confilictStates);
					else
						subskill.getBuffArgs()[3].confilictStateJs = new JavaScript("1");					
					
					if (subconf.buff3_odds != null)
						subskill.getBuffArgs()[3].oddsJs = new JavaScript(subconf.buff3_odds);
					else
						subskill.getBuffArgs()[3].oddsJs = new JavaScript("1");	

				}
				subskill.parseUsedAttrTypes();
				sSubSkillConfig.put(skillId, subskill);
			} catch (Exception e)
			{
				e.printStackTrace();
				//logger.error("ERROR: Init battle skill config fail. skillId = " + skillId);
			}
		}
	}

	public java.util.Map<Integer, BattleSkillConfig> getSkillId2configs()
	{
		return battleSkillConfigs;
	}

	private void initBattleSkillConfigs(Map<Integer, SSkillConfig> sSkillconfigs)
	{
		battleSkillConfigs.clear();
		for (int skillId : sSkillconfigs.keySet())
		{
			try
			{
				BattleSkillConfig skillconf = new BattleSkillConfig();
				SSkillConfig xlsconf = sSkillconfigs.get(skillId);
				// SKILL ID
				skillconf.setSkillId(skillId);

				// 消耗HP
				if (xlsconf.getHpConsume() == null)
					skillconf.setHpConsumeJs(new JavaScript("0"));
				else
					skillconf.setHpConsumeJs(new JavaScript(xlsconf.getHpConsume()));
				// 消耗MP
				if (xlsconf.getMpConsume() == null)
					skillconf.setMpConsumeJs(new JavaScript("0"));
				else
					skillconf.setMpConsumeJs(new JavaScript(xlsconf.getMpConsume()));
				// 消耗SP
				if (xlsconf.getSpConsume() == null)
					skillconf.setSpConsumeJs(new JavaScript("0"));
				else
					skillconf.setSpConsumeJs(new JavaScript(xlsconf.getSpConsume()));
				
				// 消耗EP
				if (xlsconf.getEpConsume() == null)
					skillconf.setEpConsumeJs(new JavaScript("0"));
				else
					skillconf.setEpConsumeJs(new JavaScript(xlsconf.getSpConsume()));
				
				// 消耗SP
				if (xlsconf.getScriptName() != null)
					skillconf.setScriptName(xlsconf.getScriptName());
				
				if (xlsconf.getSubskillIDs() != null)
				{
					String[] subskillids = xlsconf.getSubskillIDs().split(",");
					skillconf.setSubSkills(new SubSkillConfig[subskillids.length]);
					
					String[] subskillstartTimes = xlsconf.subskillstarttimes.split(",");
					int nCount = 0;
					for (String subskillid : subskillids)
					{			
						fire.pb.skill.SSubSkillConfig subconf = fire.pb.main.ConfigMgr.getInstance().getConf( fire.pb.skill.SSubSkillConfig.class ).get(Integer.valueOf(subskillid));
						if(subconf == null)
						{
							//logger.info("error: 技能加载失败  skillId = "+skillId);
							//logger.info("error: fire.pb.skill.SSubSkillConfig.xml 找不到  subskillid = "+subskillid+"的数据");
							break;
						}							
						skillconf.getSubSkills()[nCount] = new SubSkillConfig(Integer.valueOf(subskillid));
						
						SubSkillConfig subskill=skillconf.getSubSkills()[nCount];						
						subskill.setName(subconf.name);
						
						if(nCount < subskillstartTimes.length)
							subskill.setSkillStartTime(Integer.valueOf(subskillstartTimes[nCount]));
						else
							subskill.setSkillStartTime(-1);
						
						subskill.setRelevanceSkillID(subconf.relevanceSkillID);
						
						if (subconf.targetRate != null)
							subskill.setTargetRate(new JavaScript(subconf.targetRate));
						else
							subskill.setTargetRate(new JavaScript("1"));
						
						// 何种情况下执行本技能
						if (subconf.actionCondition != null)
						{
							String[] actionConditionstr = subconf.actionCondition.split(",");
							subskill.setActionConditions(new int[actionConditionstr.length]);
							for (int i = 0; i < actionConditionstr.length; i++)
							{
								subskill.getActionConditions()[i] = Integer.valueOf(actionConditionstr[i]);

							}
						}
						
						subskill.setAimCampType(subconf.aimCampType);

						// 选择目标单位类型
						if (subconf.aimRaceType != null)
						{
							String[] aimRaceTypestr = subconf.aimRaceType.split(",");
							subskill.setAimRaceTypes(new int[aimRaceTypestr.length]);
							for (int i = 0; i < aimRaceTypestr.length; i++)
							{
								subskill.getAimRaceTypes()[i] = Integer.valueOf(aimRaceTypestr[i]);

							}
						}

						// 选择目标单位关系
						if (subconf.aimRelationType != null)
						{
							String[] aimRelationTypestr = subconf.aimRelationType.split(",");
							subskill.setAimRelationTypes(new int[aimRelationTypestr.length]);
							for (int i = 0; i < aimRelationTypestr.length; i++)
							{
								subskill.getAimRelationTypes()[i] = Integer.valueOf(aimRelationTypestr[i]);

							}
						}						
						// 选择目标生效精准职业编号
						if (subconf.jobid != null)
						{
							String[] jobidstr = subconf.jobid.split(",");
							subskill.setjobids(new int[jobidstr.length]);
							for (int i = 0; i < jobidstr.length; i++)
							{
								subskill.getjobids()[i] = Integer.valueOf(jobidstr[i]);								
							}
						}
						
						// 选择目标单位关系
						if (subconf.monsterid != null)
						{
							String[] aimMonsteridsstr = subconf.monsterid.split(",");
							subskill.setaimMonsterids(new int[aimMonsteridsstr.length]);
							for (int i = 0; i < aimMonsteridsstr.length; i++)
							{
								subskill.getaimMonsterids()[i] = Integer.valueOf(aimMonsteridsstr[i]);								
							}
						}						
						
						if (subconf.confilictStates != null)
							subskill.setConfilictStateJs(new JavaScript(subconf.confilictStates));
						else
							subskill.setConfilictStateJs(new JavaScript("1"));					
						
						subskill.setConfilictStatesnotifyId (subconf.confilictStatesnotify);

						// buff0要求的前提buff
						if (subconf.requiredBuffs != null)
						{
							String[] buffidstr = subconf.requiredBuffs.split(";");
							subskill.setRequiredBuffIds(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getRequiredBuffIds()[i] = Integer.valueOf(buffidstr[i]);
							}
						}
						// 与buff0释放冲突的buff
						if (subconf.confilictBuffs != null)
						{
							String[] buffidstr = subconf.confilictBuffs.split(";");
							subskill.setConfilictBuffIds(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getConfilictBuffIds()[0] = Integer.valueOf(buffidstr[i]);
							}
						}							
						
						if (subconf.targetType != null)
							subskill.setTargettype(Integer.valueOf(subconf.targetType));
						if (subconf.targetNum != null)
							subskill.setTargetCountJs(new JavaScript(subconf.targetNum));
						else
							subskill.setTargetCountJs(new JavaScript("1"));
						
						if (subconf.minmagicdamage != null)
							subskill.setMinmagicdamageJs(new JavaScript(subconf.minmagicdamage));
						else
							subskill.setMinmagicdamageJs(new JavaScript("1"));
						
						subskill.setSortArg(subconf.sortArg);
						
						subskill.setBuffArgs(new BuffArg[4]);
						if (subconf.buff0_id != null)
						{
							subskill.getBuffArgs()[0] = new BuffArg(Integer.valueOf(subconf.buff0_id));
							if (subconf.buff0_rate != null)
								subskill.getBuffArgs()[0].rateJs = new JavaScript(subconf.buff0_rate);
							else
								subskill.getBuffArgs()[0].rateJs = new JavaScript("1");
							if (subconf.buff0_round != null)
								subskill.getBuffArgs()[0].roundJs = new JavaScript(subconf.buff0_round);
							else
								subskill.getBuffArgs()[0].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[0].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff0_values);

							if (subconf.buff0_confilictStates != null)
								subskill.getBuffArgs()[0].confilictStateJs = new JavaScript(subconf.buff0_confilictStates);
							else
								subskill.getBuffArgs()[0].confilictStateJs = new JavaScript("1");					
							

							if (subconf.buff0_odds != null)
								subskill.getBuffArgs()[0].oddsJs = new JavaScript(subconf.buff0_odds);
							else
								subskill.getBuffArgs()[0].oddsJs = new JavaScript("1");		
							 
								
						}
						if (subconf.buff1_id != null)
						{
							subskill.getBuffArgs()[1] = new BuffArg(Integer.valueOf(subconf.buff1_id));
							if (subconf.buff1_rate != null)
								subskill.getBuffArgs()[1].rateJs = new JavaScript(subconf.buff1_rate);
							else
								subskill.getBuffArgs()[1].rateJs = new JavaScript("1");

							if (subconf.buff1_round != null)
								subskill.getBuffArgs()[1].roundJs = new JavaScript(subconf.buff1_round);
							else
								subskill.getBuffArgs()[1].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[1].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff1_values);

							//	buff1状态过滤
							if (subconf.buff1_confilictStates != null)
								subskill.getBuffArgs()[1].confilictStateJs = new JavaScript(subconf.buff1_confilictStates);
							else
								subskill.getBuffArgs()[1].confilictStateJs = new JavaScript("1");					
							

							if (subconf.buff1_odds != null)
								subskill.getBuffArgs()[1].oddsJs = new JavaScript(subconf.buff1_odds);
							else
								subskill.getBuffArgs()[1].oddsJs = new JavaScript("1");	
							
						}
						if (subconf.buff2_id != null)
						{
							subskill.getBuffArgs()[2] = new BuffArg(Integer.valueOf(subconf.buff2_id));
							if (subconf.buff2_rate != null)
								subskill.getBuffArgs()[2].rateJs = new JavaScript(subconf.buff2_rate);
							else
								subskill.getBuffArgs()[2].rateJs = new JavaScript("1");

							if (subconf.buff2_round != null)
								subskill.getBuffArgs()[2].roundJs = new JavaScript(subconf.buff2_round);
							else
								subskill.getBuffArgs()[2].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[2].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff2_values);

							//	buff2状态过滤
							if (subconf.buff2_confilictStates != null)
								subskill.getBuffArgs()[2].confilictStateJs = new JavaScript(subconf.buff2_confilictStates);
							else
								subskill.getBuffArgs()[2].confilictStateJs = new JavaScript("1");					
							
							if (subconf.buff2_odds != null)
								subskill.getBuffArgs()[2].oddsJs = new JavaScript(subconf.buff2_odds);
							else
								subskill.getBuffArgs()[2].oddsJs = new JavaScript("1");	

						}
						if (subconf.buff3_id != null)
						{
							subskill.getBuffArgs()[3] = new BuffArg(Integer.valueOf(subconf.buff3_id));
							if (subconf.buff3_rate != null)
								subskill.getBuffArgs()[3].rateJs = new JavaScript(subconf.buff3_rate);
							else
								subskill.getBuffArgs()[3].rateJs = new JavaScript("1");

							if (subconf.buff3_round != null)
								subskill.getBuffArgs()[3].roundJs = new JavaScript(subconf.buff3_round);
							else
								subskill.getBuffArgs()[3].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[3].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff3_values);

							//	buff3状态过滤
							if (subconf.buff3_confilictStates != null)
								subskill.getBuffArgs()[3].confilictStateJs = new JavaScript(subconf.buff3_confilictStates);
							else
								subskill.getBuffArgs()[3].confilictStateJs = new JavaScript("1");					
							
							if (subconf.buff3_odds != null)
								subskill.getBuffArgs()[3].oddsJs = new JavaScript(subconf.buff3_odds);
							else
								subskill.getBuffArgs()[3].oddsJs = new JavaScript("1");	

						}
						nCount++;	
					}					
				}
				skillconf.parseUsedAttrTypes();
				battleSkillConfigs.put(skillId, skillconf);
			} catch (Exception e)
			{
				e.printStackTrace();
				//logger.error("ERROR: Init battle skill config fail. skillId = " + skillId);
			}
		}
	}
	

	
	public void initSceneSkillConfigs(Map<Integer, SSceneSkillConfig> sSceneSkillconfigs)
	{
		sceneSkillConfigs.clear();
		for (SSceneSkillConfig sSceneSkillConfig : sSceneSkillconfigs.values())
		{
			try
			{
				SceneSkillConfig skillconf = new SceneSkillConfig(sSceneSkillConfig.id);
				// 消耗HP
				if (sSceneSkillConfig.getHpConsume() == null)
					skillconf.setHpConsumeJs(new JavaScript("0"));
				else
					skillconf.setHpConsumeJs(new JavaScript(sSceneSkillConfig.getHpConsume()));
				// 消耗MP
				if (sSceneSkillConfig.getMpConsume() == null)
					skillconf.setMpConsumeJs(new JavaScript("0"));
				else
					skillconf.setMpConsumeJs(new JavaScript(sSceneSkillConfig.getMpConsume()));
				// 消耗SP
				if (sSceneSkillConfig.getSpConsume() == null)
					skillconf.setSpConsumeJs(new JavaScript("0"));
				else
					skillconf.setSpConsumeJs(new JavaScript(sSceneSkillConfig.getSpConsume()));

				if (sSceneSkillConfig.getSubskillIDs() != null)
				{
					String[] subskillids = sSceneSkillConfig.getSubskillIDs().split(",");
					skillconf.setSubSkills(new SubSkillConfig[subskillids.length]);
					
					String[] subskillstartTimes = sSceneSkillConfig.subskillstarttimes.split(",");
					
					int nCount = 0;
					for (String subskillid : subskillids)
					{			
						fire.pb.skill.SSubSkillConfig subconf = fire.pb.main.ConfigMgr.getInstance().getConf( fire.pb.skill.SSubSkillConfig.class ).get(Integer.valueOf(subskillid));
						if(subconf ==null)
							continue;
							//logger.error("技能中没有那子技能缺少数据， 子技能 = " + subskillid);
							
						skillconf.getSubSkills()[nCount] = new SubSkillConfig(Integer.valueOf(subskillid));
						SubSkillConfig subskill=skillconf.getSubSkills()[nCount];
						
						if (nCount < subskillstartTimes.length)
							subskill.setSkillStartTime(Integer.valueOf(subskillstartTimes[nCount]));
						else
							subskill.setSkillStartTime(-1);
						
						if(subconf.name !=null)
							subskill.setName(subconf.name);

						if (subconf.confilictBuffs != null)
							subskill.setConfilictStateJs(new JavaScript(subconf.confilictBuffs));
						else
							subskill.setConfilictStateJs(new JavaScript("1"));					
						
						subskill.setConfilictStatesnotifyId (subconf.confilictStatesnotify);
						// buff0要求的前提buff 
						if (subconf.requiredBuffs != null)
						{
							String[] buffidstr = subconf.requiredBuffs.split(";");
							subskill.setRequiredBuffIds(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getRequiredBuffIds()[i] = Integer.valueOf(buffidstr[i]);
							}
						}
						// 与buff0释放冲突的buff
						if (subconf.confilictBuffs != null)
						{
							String[] buffidstr = subconf.confilictBuffs.split(";");
							subskill.setConfilictBuffIds(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getConfilictBuffIds()[0] = Integer.valueOf(buffidstr[i]);
							}
						}							
						
						if (subconf.targetType != null)
							subskill.setTargettype(Integer.valueOf(subconf.targetType));
						if (subconf.targetNum != null)
							subskill.setTargetCountJs(new JavaScript(subconf.targetNum));
						else
							subskill.setTargetCountJs(new JavaScript("1"));
						subskill.setSortArg(subconf.sortArg);
						subskill.setBuffArgs(new BuffArg[4]);
						if (subconf.buff0_id != null)
						{
							subskill.getBuffArgs()[0] = new BuffArg(Integer.valueOf(subconf.buff0_id));
							if (subconf.buff0_rate != null)
								subskill.getBuffArgs()[0].rateJs = new JavaScript(subconf.buff0_rate);
							else
								subskill.getBuffArgs()[0].rateJs = new JavaScript("1");
							if (subconf.buff0_round != null)
								subskill.getBuffArgs()[0].roundJs = new JavaScript(subconf.buff0_round);
							else
								subskill.getBuffArgs()[0].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[0].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff0_values);

							if (subconf.buff0_confilictStates != null)
								subskill.getBuffArgs()[0].confilictStateJs = new JavaScript(subconf.buff0_confilictStates);
							else
								subskill.getBuffArgs()[0].confilictStateJs = new JavaScript("1");					
							
							if (subconf.buff0_odds != null)
								subskill.getBuffArgs()[0].oddsJs = new JavaScript(subconf.buff0_odds);
							else
								subskill.getBuffArgs()[0].oddsJs = new JavaScript("1");					

						}
						if (subconf.buff1_id != null)
						{
							subskill.getBuffArgs()[1] = new BuffArg(Integer.valueOf(subconf.buff1_id));
							if (subconf.buff1_rate != null)
								subskill.getBuffArgs()[1].rateJs = new JavaScript(subconf.buff1_rate);
							else
								subskill.getBuffArgs()[1].rateJs = new JavaScript("1");

							if (subconf.buff1_round != null)
								subskill.getBuffArgs()[1].roundJs = new JavaScript(subconf.buff1_round);
							else
								subskill.getBuffArgs()[1].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[1].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff1_values);

							//	buff1状态过滤
							if (subconf.buff1_confilictStates != null)
								subskill.getBuffArgs()[1].confilictStateJs = new JavaScript(subconf.buff1_confilictStates);
							else
								subskill.getBuffArgs()[1].confilictStateJs = new JavaScript("1");					
							
							if (subconf.buff1_odds != null)
								subskill.getBuffArgs()[1].oddsJs = new JavaScript(subconf.buff1_odds);
							else
								subskill.getBuffArgs()[1].oddsJs = new JavaScript("1");	

						}
						if (subconf.buff2_id != null)
						{
							subskill.getBuffArgs()[2] = new BuffArg(Integer.valueOf(subconf.buff2_id));
							if (subconf.buff2_rate != null)
								subskill.getBuffArgs()[2].rateJs = new JavaScript(subconf.buff2_rate);
							else
								subskill.getBuffArgs()[2].rateJs = new JavaScript("1");

							if (subconf.buff2_round != null)
								subskill.getBuffArgs()[2].roundJs = new JavaScript(subconf.buff2_round);
							else
								subskill.getBuffArgs()[2].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[2].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff2_values);

							//	buff2状态过滤
							if (subconf.buff2_confilictStates != null)
								subskill.getBuffArgs()[2].confilictStateJs = new JavaScript(subconf.buff2_confilictStates);
							else
								subskill.getBuffArgs()[2].confilictStateJs = new JavaScript("1");					
							
							if (subconf.buff2_odds != null)
								subskill.getBuffArgs()[2].oddsJs = new JavaScript(subconf.buff2_odds);
							else
								subskill.getBuffArgs()[2].oddsJs = new JavaScript("1");	

						}
						if (subconf.buff3_id != null)
						{
							subskill.getBuffArgs()[3] = new BuffArg(Integer.valueOf(subconf.buff3_id));
							if (subconf.buff3_rate != null)
								subskill.getBuffArgs()[3].rateJs = new JavaScript(subconf.buff3_rate);
							else
								subskill.getBuffArgs()[3].rateJs = new JavaScript("1");

							if (subconf.buff3_round != null)
								subskill.getBuffArgs()[3].roundJs = new JavaScript(subconf.buff3_round);
							else
								subskill.getBuffArgs()[3].roundJs = new JavaScript("0");
							subskill.getBuffArgs()[3].effectJsMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff3_values);

							//	buff3状态过滤
							if (subconf.buff3_confilictStates != null)
								subskill.getBuffArgs()[3].confilictStateJs = new JavaScript(subconf.buff3_confilictStates);
							else
								subskill.getBuffArgs()[3].confilictStateJs = new JavaScript("1");					
							
							if (subconf.buff3_odds != null)
								subskill.getBuffArgs()[3].oddsJs = new JavaScript(subconf.buff3_odds);
							else
								subskill.getBuffArgs()[3].oddsJs = new JavaScript("1");	

						}
						nCount++;	
					}					
				}				
				skillconf.parseUsedAttrTypes();
				sceneSkillConfigs.put(skillconf.getSkillId(), skillconf);
			} catch (Exception e)
			{
				e.printStackTrace();
				//logger.error("ERROR: Init scene skill config fail. skillId = " + sSceneSkillConfig.id);
			}

		}
	}
}
