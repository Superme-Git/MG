package fire.pb.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.log.Logger;
import fire.pb.battle.ScriptTimeCalc;
import fire.pb.buff.single.SingleBuffConfig;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.scene.SceneSkillConfig;
import fire.script.JavaScript;

public class Module implements ModuleInterface
{
	public final static String MODULE_NAME = "skill";
	static public final Logger logger = Logger.getLogger("SKILL");
	Map<Integer, FightSkillConfig> battleSkillConfigs = new HashMap<Integer, FightSkillConfig>();
	Map<Integer, SceneSkillConfig> sceneSkillConfigs = new HashMap<Integer, SceneSkillConfig>();
	Map<Integer, List<Integer>> schoolSkillIds = new HashMap<Integer, List<Integer>>();
	static Map<Integer, InbornConfig> inbornConfigs = new HashMap<Integer, InbornConfig>();
	Map<Integer, Integer> subSkillCostTimes = new HashMap<Integer, Integer>();

	Map<String, Integer> Js2ids = new HashMap<String, Integer>();
	            		
	public static Module getInstance()
	{
		return ((fire.pb.skill.Module)ModuleManager.getInstance().getModuleByName(MODULE_NAME));
	}

	public static SInbornInfo getSInbornInfo(int apid)
	{
	
		SInbornInfo ap = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.skill.SInbornInfo.class).get(apid);
		return ap;
	}

	public static InbornConfig getInbornConfig(int apid)
	{	
		return inbornConfigs.get(apid);
	}

	
	public static SInbornMaxLevel getSInbornMaxLevel(int apid)
	{
		SInbornMaxLevel ap = fire.pb.main.ConfigManager.getInstance().getConf(
				fire.pb.skill.SInbornMaxLevel.class).get(apid);
		return ap;
	}

	public static SInbornEffectSkill getSInbornEffectSkill(int skillId)
	{
		SInbornEffectSkill ap = fire.pb.main.ConfigManager.getInstance().getConf(
				fire.pb.skill.SInbornEffectSkill.class).get(skillId);
		return ap;
	}

	public static SInbornConsume getSInbornConsume(int apid)
	{
		SInbornConsume consume = fire.pb.main.ConfigManager.getInstance().getConf(
				fire.pb.skill.SInbornConsume.class).get(apid);
		return consume;
	}

	public static java.util.Map<Integer, SSkillConfig> getSSkillConfigs()
	{
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.skill.SSkillConfig.class);
	}

	public static SAssistSkill getSAssistSkill(int id)
	{
		SAssistSkill asc = fire.pb.main.ConfigManager.getInstance().getConf(
				fire.pb.skill.SAssistSkill.class).get(id);
		return asc;
	}
	
	public static List<Integer> getRealmInbornsBySchool(int school, int realm)
	{
		List<Integer> apids = new ArrayList<Integer>();
		
		for(int apchildid : inbornSeq)
		{
			if(apchildid / 10 == realm)
				apids.add(school *100 + apchildid);
		}
		
		return apids;
	}

	@Override
	public void exit()
	{

	}

	@Override
	public synchronized void init() throws Exception
	{

		logger.info("skill模块初始化开始");
		Map<Integer, SSkillConfig> sbattleSkillconfigs = fire.pb.main.ConfigManager.getInstance().getConf(
		fire.pb.skill.SSkillConfig.class);
		initFightSkillConfigs(sbattleSkillconfigs);
		logger.info("加载战斗技能，个数：" + battleSkillConfigs.size());
		Map<Integer, SSceneSkillConfig> sSceneSkillconfigs = fire.pb.main.ConfigManager.getInstance().getConf(
		fire.pb.skill.SSceneSkillConfig.class);
		initSceneSkillConfigs(sSceneSkillconfigs);
		initInbornConfigs();
		logger.info("加载场景技能，个数：" + sSceneSkillconfigs.size());
		logger.info("skill模块初始化完成");
	}

	public java.util.Map<Integer, FightSkillConfig> getSkillId2configs()
	{
		return battleSkillConfigs;
	}

	private void initFightSkillConfigs(Map<Integer, SSkillConfig> sSkillconfigs)
	{
		battleSkillConfigs.clear();
		subSkillCostTimes.clear();
		for (int skillId : sSkillconfigs.keySet())
		{
			try
			{
				FightSkillConfig skillCfg = new FightSkillConfig();
				SSkillConfig tableCfg = sSkillconfigs.get(skillId);
				// SKILL ID
				skillCfg.setSkillIndex(skillId);
				skillCfg.setSkillName(tableCfg.name);
				skillCfg.setType(tableCfg.type);
				skillCfg.setUseInBattle(tableCfg.inOutBattle);
				skillCfg.setSkillSpecialAddTime(tableCfg.skillspecialtime);
				skillCfg.setIsOnlyPve(tableCfg.isonlypve==1);
				skillCfg.setPreCountAim(tableCfg.isprecountaim);
				skillCfg.setCanEffectByHate(tableCfg.caneffectbyhate==1);	
				skillCfg.setActiveSkill(tableCfg.isActive);
				skillCfg.setCanDouble(tableCfg.canDouble);
				skillCfg.setCanAtackBack(tableCfg.canAtackBack);
				skillCfg.setForbiddenDefPrt(tableCfg.forbiddendefprt);	
				skillCfg.setIgnoreHide(tableCfg.ignorehide);
				skillCfg.setAddautoSkill(tableCfg.addautoskill);				
				int schoolId = skillId / 10000;
				if(schoolId >= 16)
					schoolId += (skillId / 1000)%10;
				if (schoolId >= 11 && schoolId <= 19)
				{
					List<Integer> skillIds = schoolSkillIds.get(schoolId);
					if (skillIds == null)
					{
						skillIds = new LinkedList<Integer>();
						schoolSkillIds.put(schoolId, skillIds);
					}
					skillIds.add(skillId);
				}

				skillCfg.setSkillBelongType(tableCfg.getTypeinserver());
				skillCfg.setTargettype(tableCfg.getTargetType());
				if (tableCfg.getInitSpeedBonus() != null && !tableCfg.getInitSpeedBonus().equals(""))
					skillCfg.setSpeedBonus(Float.valueOf(tableCfg.getInitSpeedBonus()).intValue());

				if (tableCfg.getHpConsume() == null)
					skillCfg.setHpConsumeJavaScript(new JavaScript("0"));
				else
					skillCfg.setHpConsumeJavaScript(new JavaScript(tableCfg.getHpConsume()));

				if (tableCfg.getMpConsume() == null)
					skillCfg.setMpConsumeJavaScript(new JavaScript("0"));
				else
					skillCfg.setMpConsumeJavaScript(new JavaScript(tableCfg.getMpConsume()));

				if (tableCfg.getSpConsume() == null)
					skillCfg.setSpConsumeJavaScript(new JavaScript("0"));
				else
					skillCfg.setSpConsumeJavaScript(new JavaScript(tableCfg.getSpConsume()));
				
				if (tableCfg.getEpConsume() == null)
					skillCfg.setEpConsumeJavaScript(new JavaScript("0"));
				else
					skillCfg.setEpConsumeJavaScript(new JavaScript(tableCfg.getEpConsume()));
			
				if (tableCfg.getSubskillIDs() != null)
				{
					String[] subskillids = tableCfg.getSubskillIDs().split(",");
					skillCfg.setSubSkills(new SubSkillConfig[subskillids.length]);
					
					String[] subskillstartTimes = tableCfg.subskillstarttimes.split(",");
					int nCount = 0;
					for (String subskillid : subskillids)
					{			
						fire.pb.skill.SSubSkillConfig subconf = fire.pb.main.ConfigManager.getInstance().getConf( fire.pb.skill.SSubSkillConfig.class ).get(Integer.valueOf(subskillid));
						if(subconf == null)
						{
							logger.info("error: 技能加载失败  skillId = "+skillId);
							logger.info("error: fire.pb.skill.skillcfgConfig.xml 找不到  subskillid = "+subskillid+"的数据");
							break;
						}							
						skillCfg.getSubSkills()[nCount] = new SubSkillConfig(Integer.valueOf(subskillid));
						
						SubSkillConfig subskill=skillCfg.getSubSkills()[nCount];						
						subskill.setSubName(subconf.name);
						
						if(nCount < subskillstartTimes.length&&nCount != 0)
							subskill.setSkillBeginTime(Integer.valueOf(subskillstartTimes[nCount]));
						else
							subskill.setSkillBeginTime(-1);
						subSkillCostTimes.put(subconf.getId(), subskill.getSkillBeginTime());
						
						subskill.setRelevanceSubSkillId(subconf.relevanceSkillID);
						
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
						
						subskill.setGoalCampType(subconf.aimCampType);

						// 选择目标单位类型
						if (subconf.aimRaceType != null)
						{
							String[] aimRaceTypestr = subconf.aimRaceType.split(",");
							subskill.setGoalRaceTypes(new int[aimRaceTypestr.length]);
							for (int i = 0; i < aimRaceTypestr.length; i++)
							{
								subskill.getGoalRaceTypes()[i] = Integer.valueOf(aimRaceTypestr[i]);

							}
						}

						// 选择目标单位关系
						if (subconf.aimRelationType != null)
						{
							String[] aimRelationTypestr = subconf.aimRelationType.split(",");
							subskill.setGoalRelationTypes(new int[aimRelationTypestr.length]);
							for (int i = 0; i < aimRelationTypestr.length; i++)
							{
								subskill.getGoalRelationTypes()[i] = Integer.valueOf(aimRelationTypestr[i]);

							}
						}						
						// 选择目标生效精准职业编号
						if (subconf.jobid != null)
						{
							String[] jobidstr = subconf.jobid.split(",");
							subskill.setJobIds(new int[jobidstr.length]);
							for (int i = 0; i < jobidstr.length; i++)
							{
								subskill.getJobIds()[i] = Integer.valueOf(jobidstr[i]);								
							}
						}
						
						// 选择目标单位关系
						if (subconf.monsterid != null)
						{
							String[] aimMonsteridsstr = subconf.monsterid.split(",");
							subskill.setGoalMonsterids(new int[aimMonsteridsstr.length]);
							for (int i = 0; i < aimMonsteridsstr.length; i++)
							{
								subskill.getGoalMonsterids()[i] = Integer.valueOf(aimMonsteridsstr[i]);								
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
							subskill.setRequiredBuffIndexs(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getRequiredBuffIndexs()[i] = Integer.valueOf(buffidstr[i]);
							}
						}
						// 与buff0释放冲突的buff
						if (subconf.confilictBuffs != null)
						{
							String[] buffidstr = subconf.confilictBuffs.split(";");
							subskill.setConfilictBuffIndexs(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getConfilictBuffIndexs()[i] = Integer.valueOf(buffidstr[i]);
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
						
						subskill.setSortParam(subconf.sortArg);
						
						int ceffect = getSkillCilentEffectId(subskill.getSubSkillId());
						if(ceffect > 0)
						{
							skillCfg.setPlayTime(skillCfg.getPlayTime()+calcSkillTime(ceffect));
						}
						subskill.setBuffUnits(new BuffUnit[4]);
						if (subconf.buff0_id != null)
						{
							subskill.getBuffUnits()[0] = new BuffUnit(Integer.valueOf(subconf.buff0_id));
							if (subconf.buff0_rate != null)
								subskill.getBuffUnits()[0].rateJavascript = new JavaScript(subconf.buff0_rate);
							else
								subskill.getBuffUnits()[0].rateJavascript = new JavaScript("1");
							if (subconf.buff0_round != null)
								subskill.getBuffUnits()[0].roundJavascript = new JavaScript(subconf.buff0_round);
							else
								subskill.getBuffUnits()[0].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[0].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff0_values);

							if (subconf.buff0_confilictStates != null)
								subskill.getBuffUnits()[0].confilictStateJavascript = new JavaScript(subconf.buff0_confilictStates);
							else
								subskill.getBuffUnits()[0].confilictStateJavascript = new JavaScript("1");					
							
							// buff0要求的前提buff
							if (subconf.getBuff0_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff0_requiredBuffs().split(";");
								subskill.getBuffUnits()[0].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[0].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff0释放冲突的buff
							if (subconf.getBuff0_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff0_confilictBuffs().split(";");
								subskill.getBuffUnits()[0].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[0].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff0_odds != null)
								subskill.getBuffUnits()[0].oddsJavascript = new JavaScript(subconf.buff0_odds);
							else
								subskill.getBuffUnits()[0].oddsJavascript = new JavaScript("1");		
							 
							 SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[0].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[0].flagtype = cfg.getBuffFlagType();
								
						}
						if (subconf.buff1_id != null)
						{
							subskill.getBuffUnits()[1] = new BuffUnit(Integer.valueOf(subconf.buff1_id));
							if (subconf.buff1_rate != null)
								subskill.getBuffUnits()[1].rateJavascript = new JavaScript(subconf.buff1_rate);
							else
								subskill.getBuffUnits()[1].rateJavascript = new JavaScript("1");

							if (subconf.buff1_round != null)
								subskill.getBuffUnits()[1].roundJavascript = new JavaScript(subconf.buff1_round);
							else
								subskill.getBuffUnits()[1].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[1].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff1_values);

							//	buff1状态过滤
							if (subconf.buff1_confilictStates != null)
								subskill.getBuffUnits()[1].confilictStateJavascript = new JavaScript(subconf.buff1_confilictStates);
							else
								subskill.getBuffUnits()[1].confilictStateJavascript = new JavaScript("1");					
							
							// buff1要求的前提buff
							if (subconf.getBuff1_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff1_requiredBuffs().split(";");
								subskill.getBuffUnits()[1].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[1].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff1释放冲突的buff
							if (subconf.getBuff1_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff1_confilictBuffs().split(";");
								subskill.getBuffUnits()[1].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[1].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff1_odds != null)
								subskill.getBuffUnits()[1].oddsJavascript = new JavaScript(subconf.buff1_odds);
							else
								subskill.getBuffUnits()[1].oddsJavascript = new JavaScript("1");	
							
							SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[1].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[1].flagtype = cfg.getBuffFlagType();
						}
						if (subconf.buff2_id != null)
						{
							subskill.getBuffUnits()[2] = new BuffUnit(Integer.valueOf(subconf.buff2_id));
							if (subconf.buff2_rate != null)
								subskill.getBuffUnits()[2].rateJavascript = new JavaScript(subconf.buff2_rate);
							else
								subskill.getBuffUnits()[2].rateJavascript = new JavaScript("1");

							if (subconf.buff2_round != null)
								subskill.getBuffUnits()[2].roundJavascript = new JavaScript(subconf.buff2_round);
							else
								subskill.getBuffUnits()[2].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[2].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff2_values);

							//	buff2状态过滤
							if (subconf.buff2_confilictStates != null)
								subskill.getBuffUnits()[2].confilictStateJavascript = new JavaScript(subconf.buff2_confilictStates);
							else
								subskill.getBuffUnits()[2].confilictStateJavascript = new JavaScript("1");					
							
							// buff2要求的前提buff
							if (subconf.getBuff2_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff2_requiredBuffs().split(";");
								subskill.getBuffUnits()[2].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[2].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff2释放冲突的buff
							if (subconf.getBuff2_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff2_confilictBuffs().split(";");
								subskill.getBuffUnits()[2].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[2].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff2_odds != null)
								subskill.getBuffUnits()[2].oddsJavascript = new JavaScript(subconf.buff2_odds);
							else
								subskill.getBuffUnits()[2].oddsJavascript = new JavaScript("1");	

							SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[2].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[2].flagtype = cfg.getBuffFlagType();
						}
						if (subconf.buff3_id != null)
						{
							subskill.getBuffUnits()[3] = new BuffUnit(Integer.valueOf(subconf.buff3_id));
							if (subconf.buff3_rate != null)
								subskill.getBuffUnits()[3].rateJavascript = new JavaScript(subconf.buff3_rate);
							else
								subskill.getBuffUnits()[3].rateJavascript = new JavaScript("1");

							if (subconf.buff3_round != null)
								subskill.getBuffUnits()[3].roundJavascript = new JavaScript(subconf.buff3_round);
							else
								subskill.getBuffUnits()[3].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[3].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff3_values);

							//	buff3状态过滤
							if (subconf.buff3_confilictStates != null)
								subskill.getBuffUnits()[3].confilictStateJavascript = new JavaScript(subconf.buff3_confilictStates);
							else
								subskill.getBuffUnits()[3].confilictStateJavascript = new JavaScript("1");					
							
							// buff3要求的前提buff
							if (subconf.getBuff3_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff3_requiredBuffs().split(";");
								subskill.getBuffUnits()[3].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[3].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff3释放冲突的buff
							if (subconf.getBuff3_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff3_confilictBuffs().split(";");
								subskill.getBuffUnits()[3].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[3].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff3_odds != null)
								subskill.getBuffUnits()[3].oddsJavascript = new JavaScript(subconf.buff3_odds);
							else
								subskill.getBuffUnits()[3].oddsJavascript = new JavaScript("1");	

							SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[3].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[3].flagtype = cfg.getBuffFlagType();
					
						}
						nCount++;	
					}					
				}
				skillCfg.parseUsedAttrTypes();
				battleSkillConfigs.put(skillId, skillCfg);
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error("ERROR: Init battle skill config fail. skillId = " + skillId);
			}
		}
	}
	
	/**
	 * 没有返回0，如果返回的15，则是被动技能
	 * @param skillId
	 * @return
	 */
	public int getSkillCilentEffectId(int skillId)
	{
		SSchoolSkillitem ceffect0 = ConfigManager.getInstance().getConf(SSchoolSkillitem.class).get(skillId);
		if(ceffect0 != null)
			return ceffect0.effectid;
		SPetSkillitem ceffect3 = ConfigManager.getInstance().getConf(SPetSkillitem.class).get(skillId);
		if(ceffect3 != null)
			return ceffect3.effectid;

		return 0;
	}
	
	public void initInbornConfigs()
	{
		Map<Integer, SInbornInfo> sSInbornInfos = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.skill.SInbornInfo.class);
		inbornConfigs.clear();
		for (SInbornInfo sSInbornInfo : sSInbornInfos.values())
		{
			try
			{
				InbornConfig inbornconf = new InbornConfig(sSInbornInfo);
				inbornConfigs.put(inbornconf.getInbornId(), inbornconf);
				
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error("ERROR: Init scene Inborn config fail. InbornId = " + sSInbornInfo.id);
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
				SceneSkillConfig skillCfg = new SceneSkillConfig(sSceneSkillConfig.id);
				skillCfg.setSkillName(sSceneSkillConfig.name);
				skillCfg.setType(sSceneSkillConfig.type);

				// 技能选择目标的类型
				skillCfg.setTargettype(sSceneSkillConfig.getTargetType());

				// 消耗HP
				if (sSceneSkillConfig.getHpConsume() == null)
					skillCfg.setHpConsumeJavaScript(new JavaScript("0"));
				else
					skillCfg.setHpConsumeJavaScript(new JavaScript(sSceneSkillConfig.getHpConsume()));
				// 消耗MP
				if (sSceneSkillConfig.getMpConsume() == null)
					skillCfg.setMpConsumeJavaScript(new JavaScript("0"));
				else
					skillCfg.setMpConsumeJavaScript(new JavaScript(sSceneSkillConfig.getMpConsume()));
				// 消耗SP
				if (sSceneSkillConfig.getSpConsume() == null)
					skillCfg.setSpConsumeJavaScript(new JavaScript("0"));
				else
					skillCfg.setSpConsumeJavaScript(new JavaScript(sSceneSkillConfig.getSpConsume()));

				if (sSceneSkillConfig.getSubskillIDs() != null)
				{
					String[] subskillids = sSceneSkillConfig.getSubskillIDs().split(",");
					skillCfg.setSubSkills(new SubSkillConfig[subskillids.length]);
					
					String[] subskillstartTimes = sSceneSkillConfig.subskillstarttimes.split(",");
					
					int nCount = 0;
					for (String subskillid : subskillids)
					{			
						fire.pb.skill.SSubSkillConfig subconf = fire.pb.main.ConfigManager.getInstance().getConf( fire.pb.skill.SSubSkillConfig.class ).get(Integer.valueOf(subskillid));
						if(subconf ==null)
							logger.error("技能中没有那子技能缺少数据， 子技能 = " + subskillid);
							
						skillCfg.getSubSkills()[nCount] = new SubSkillConfig(Integer.valueOf(subskillid));
						SubSkillConfig subskill=skillCfg.getSubSkills()[nCount];
						
						if (nCount < subskillstartTimes.length)
							subskill.setSkillBeginTime(Integer.valueOf(subskillstartTimes[nCount]));
						else
							subskill.setSkillBeginTime(-1);
						
						if(subconf.name !=null)
							subskill.setSubName(subconf.name);

						if (subconf.confilictBuffs != null)
							subskill.setConfilictStateJs(new JavaScript(subconf.confilictBuffs));
						else
							subskill.setConfilictStateJs(new JavaScript("1"));					
						
						subskill.setConfilictStatesnotifyId (subconf.confilictStatesnotify);
						// buff0要求的前提buff 
						if (subconf.requiredBuffs != null)
						{
							String[] buffidstr = subconf.requiredBuffs.split(";");
							subskill.setRequiredBuffIndexs(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getRequiredBuffIndexs()[i] = Integer.valueOf(buffidstr[i]);
							}
						}
						// 与buff0释放冲突的buff
						if (subconf.confilictBuffs != null)
						{
							String[] buffidstr = subconf.confilictBuffs.split(";");
							subskill.setConfilictBuffIndexs(new int[buffidstr.length]);
							for (int i = 0; i < buffidstr.length; i++)
							{
								subskill.getConfilictBuffIndexs()[0] = Integer.valueOf(buffidstr[i]);
							}
						}							
						
						if (subconf.targetType != null)
							subskill.setTargettype(Integer.valueOf(subconf.targetType));
						if (subconf.targetNum != null)
							subskill.setTargetCountJs(new JavaScript(subconf.targetNum));
						else
							subskill.setTargetCountJs(new JavaScript("1"));
						subskill.setSortParam(subconf.sortArg);
						
						if (subconf.targetRate != null)
							subskill.setTargetRate(new JavaScript(subconf.targetRate));
						else
							subskill.setTargetRate(new JavaScript("1"));

			
						subskill.setBuffUnits(new BuffUnit[4]);
						if (subconf.buff0_id != null)
						{
							subskill.getBuffUnits()[0] = new BuffUnit(Integer.valueOf(subconf.buff0_id));
							if (subconf.buff0_rate != null)
								subskill.getBuffUnits()[0].rateJavascript = new JavaScript(subconf.buff0_rate);
							else
								subskill.getBuffUnits()[0].rateJavascript = new JavaScript("1");
							if (subconf.buff0_round != null)
								subskill.getBuffUnits()[0].roundJavascript = new JavaScript(subconf.buff0_round);
							else
								subskill.getBuffUnits()[0].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[0].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff0_values);

							if (subconf.buff0_confilictStates != null)
								subskill.getBuffUnits()[0].confilictStateJavascript = new JavaScript(subconf.buff0_confilictStates);
							else
								subskill.getBuffUnits()[0].confilictStateJavascript = new JavaScript("1");					
							
							// buff0要求的前提buff
							if (subconf.getBuff0_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff0_requiredBuffs().split(";");
								subskill.getBuffUnits()[0].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[0].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff0释放冲突的buff
							if (subconf.getBuff0_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff0_confilictBuffs().split(";");
								subskill.getBuffUnits()[0].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[0].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff0_odds != null)
								subskill.getBuffUnits()[0].oddsJavascript = new JavaScript(subconf.buff0_odds);
							else
								subskill.getBuffUnits()[0].oddsJavascript = new JavaScript("1");					

							SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[0].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[0].flagtype = cfg.getBuffFlagType();
						}
						if (subconf.buff1_id != null)
						{
							subskill.getBuffUnits()[1] = new BuffUnit(Integer.valueOf(subconf.buff1_id));
							if (subconf.buff1_rate != null)
								subskill.getBuffUnits()[1].rateJavascript = new JavaScript(subconf.buff1_rate);
							else
								subskill.getBuffUnits()[1].rateJavascript = new JavaScript("1");

							if (subconf.buff1_round != null)
								subskill.getBuffUnits()[1].roundJavascript = new JavaScript(subconf.buff1_round);
							else
								subskill.getBuffUnits()[1].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[1].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff1_values);

							//	buff1状态过滤
							if (subconf.buff1_confilictStates != null)
								subskill.getBuffUnits()[1].confilictStateJavascript = new JavaScript(subconf.buff1_confilictStates);
							else
								subskill.getBuffUnits()[1].confilictStateJavascript = new JavaScript("1");					
							
							// buff1要求的前提buff
							if (subconf.getBuff1_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff1_requiredBuffs().split(";");
								subskill.getBuffUnits()[1].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[1].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff1释放冲突的buff
							if (subconf.getBuff1_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff1_confilictBuffs().split(";");
								subskill.getBuffUnits()[1].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[1].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff1_odds != null)
								subskill.getBuffUnits()[1].oddsJavascript = new JavaScript(subconf.buff1_odds);
							else
								subskill.getBuffUnits()[1].oddsJavascript = new JavaScript("1");	

							SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[1].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[1].flagtype = cfg.getBuffFlagType();
							
						}
						if (subconf.buff2_id != null)
						{
							subskill.getBuffUnits()[2] = new BuffUnit(Integer.valueOf(subconf.buff2_id));
							if (subconf.buff2_rate != null)
								subskill.getBuffUnits()[2].rateJavascript = new JavaScript(subconf.buff2_rate);
							else
								subskill.getBuffUnits()[2].rateJavascript = new JavaScript("1");

							if (subconf.buff2_round != null)
								subskill.getBuffUnits()[2].roundJavascript = new JavaScript(subconf.buff2_round);
							else
								subskill.getBuffUnits()[2].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[2].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff2_values);

							//	buff2状态过滤
							if (subconf.buff2_confilictStates != null)
								subskill.getBuffUnits()[2].confilictStateJavascript = new JavaScript(subconf.buff2_confilictStates);
							else
								subskill.getBuffUnits()[2].confilictStateJavascript = new JavaScript("1");					
							
							// buff2要求的前提buff
							if (subconf.getBuff2_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff2_requiredBuffs().split(";");
								subskill.getBuffUnits()[2].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[2].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff2释放冲突的buff
							if (subconf.getBuff2_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff2_confilictBuffs().split(";");
								subskill.getBuffUnits()[2].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[2].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff2_odds != null)
								subskill.getBuffUnits()[2].oddsJavascript = new JavaScript(subconf.buff2_odds);
							else
								subskill.getBuffUnits()[2].oddsJavascript = new JavaScript("1");	

							SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[2].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[2].flagtype = cfg.getBuffFlagType();
						}
						if (subconf.buff3_id != null)
						{
							subskill.getBuffUnits()[3] = new BuffUnit(Integer.valueOf(subconf.buff3_id));
							if (subconf.buff3_rate != null)
								subskill.getBuffUnits()[3].rateJavascript = new JavaScript(subconf.buff3_rate);
							else
								subskill.getBuffUnits()[3].rateJavascript = new JavaScript("1");

							if (subconf.buff3_round != null)
								subskill.getBuffUnits()[3].roundJavascript = new JavaScript(subconf.buff3_round);
							else
								subskill.getBuffUnits()[3].roundJavascript = new JavaScript("0");
							subskill.getBuffUnits()[3].effectJavascriptMap = fire.pb.util.Parser.parseFightJsEffects(subconf.buff3_values);

							//	buff3状态过滤
							if (subconf.buff3_confilictStates != null)
								subskill.getBuffUnits()[3].confilictStateJavascript = new JavaScript(subconf.buff3_confilictStates);
							else
								subskill.getBuffUnits()[3].confilictStateJavascript = new JavaScript("1");					
							
							// buff3要求的前提buff
							if (subconf.getBuff3_requiredBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff3_requiredBuffs().split(";");
								subskill.getBuffUnits()[3].requiredBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[3].requiredBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}
							// 与buff3释放冲突的buff
							if (subconf.getBuff3_confilictBuffs() != null)
							{
								String[] buffidstr = subconf.getBuff3_confilictBuffs().split(";");
								subskill.getBuffUnits()[3].confilictBuffIds = new int[buffidstr.length];
								for (int i = 0; i < buffidstr.length; i++)
								{
									subskill.getBuffUnits()[3].confilictBuffIds[i] = Integer.valueOf(buffidstr[i]);
								}
							}	

							if (subconf.buff3_odds != null)
								subskill.getBuffUnits()[3].oddsJavascript = new JavaScript(subconf.buff3_odds);
							else
								subskill.getBuffUnits()[3].oddsJavascript = new JavaScript("1");	

							SingleBuffConfig cfg = fire.pb.buff.Module.getInstance().getDefaultIBuffConfig(subskill.getBuffUnits()[3].buffIndex);
							if(cfg != null)
								subskill.getBuffUnits()[3].flagtype = cfg.getBuffFlagType();
						}
						nCount++;	
					}					
				}				
				skillCfg.parseUsedAttrTypes();
				sceneSkillConfigs.put(skillCfg.getSkillId(), skillCfg);
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error("ERROR: Init scene skill config fail. skillId = " + sSceneSkillConfig.id);
			}

		}
	}

	public FightSkillConfig getFightSkillConfig(int skillId)
	{
		return battleSkillConfigs.get(skillId);
	}

	public SceneSkillConfig getSceneSkillConfig(int skillId)
	{
		return sceneSkillConfigs.get(skillId);
	}
	

	public int getSubSkillCostTimes(int subSkillId)
	{
		if(subSkillCostTimes.containsKey(subSkillId))
			return subSkillCostTimes.get(subSkillId);
		return -1;
	}


	private int calcSkillTime(int ceffect)
	{
		return ScriptTimeCalc.getScriptTimeByTimeId(ScriptTimeCalc.DEFAULT_RESULT_TIME_ID);
	}
	
	//是否战斗技能
	public boolean isBattleSkill(int skillId)
	{
		return battleSkillConfigs.containsKey(skillId);
	}

	//是否宠物技能
	public static boolean isPetSkill(int skillId)
	{
		if (skillId >= 200000 && skillId < 300000)
			return true;
		else
			return false;
	}

	//是否职业技能
	public static boolean isRoleSchoolSkill(int skillId)
	{
		if (skillId >= 110000 && skillId < 200000)
			return true;
		else
			return false;
	}

	//职业技能id
	public List<Integer> getAllSkillIDsBySchool(int schoolId)
	{
		List<Integer> ids = schoolSkillIds.get(schoolId);
		if (ids != null)
			return ids;
		else
			return new ArrayList<Integer>();
	}

	public final static int[] inbornSeq = new int[72];
	public final static int[] inborns1level = new int[8];
	public final static int[] inborns2level = new int[8];
	public final static int[] inborns3level = new int[8];
	public final static int[] inborns4level = new int[8];
	public final static int[] inborns5level = new int[8];
	public final static int[] inborns6level = new int[8];
	public final static int[] inborns7level = new int[8];
	public final static int[] inborns8level = new int[8];
	public final static int[] inborns9level = new int[8];
	
	static
	{
		inbornSeq[0 ] = inborns1level[0] = 11;
		inbornSeq[1 ] = inborns1level[1] = 12;
		inbornSeq[2 ] = inborns1level[2] = 13;
		inbornSeq[3 ] = inborns1level[3] = 14;
		inbornSeq[4 ] = inborns1level[4] = 15;
		inbornSeq[5 ] = inborns1level[5] = 16;
		inbornSeq[6 ] = inborns1level[6] = 17;
		inbornSeq[7 ] = inborns1level[7] = 18;
		inbornSeq[8 ] = inborns2level[0] = 21;
		inbornSeq[9 ] = inborns2level[1] = 22;
		inbornSeq[10] = inborns2level[2] = 23;
		inbornSeq[11] = inborns2level[3] = 24;
		inbornSeq[12] = inborns2level[4] = 25;
		inbornSeq[13] = inborns2level[5] = 26;
		inbornSeq[14] = inborns2level[6] = 27;
		inbornSeq[15] = inborns2level[7] = 28;
		inbornSeq[16] = inborns3level[0] = 31;
		inbornSeq[17] = inborns3level[1] = 32;
		inbornSeq[18] = inborns3level[2] = 33;
		inbornSeq[19] = inborns3level[3] = 34;
		inbornSeq[20] = inborns3level[4] = 35;
		inbornSeq[21] = inborns3level[5] = 36;
		inbornSeq[22] = inborns3level[6] = 37;
		inbornSeq[23] = inborns3level[7] = 38;
		inbornSeq[24] = inborns4level[0] = 41;
		inbornSeq[25] = inborns4level[1] = 42;
		inbornSeq[26] = inborns4level[2] = 43;
		inbornSeq[27] = inborns4level[3] = 44;
		inbornSeq[28] = inborns4level[4] = 45;
		inbornSeq[29] = inborns4level[5] = 46;
		inbornSeq[30] = inborns4level[6] = 47;
		inbornSeq[31] = inborns4level[7] = 48;
		inbornSeq[32] = inborns5level[0] = 51;
		inbornSeq[33] = inborns5level[1] = 52;
		inbornSeq[34] = inborns5level[2] = 53;
		inbornSeq[35] = inborns5level[3] = 54;
		inbornSeq[36] = inborns5level[4] = 55;
		inbornSeq[37] = inborns5level[5] = 56;
		inbornSeq[38] = inborns5level[6] = 57;
		inbornSeq[39] = inborns5level[7] = 58;
		inbornSeq[40] = inborns6level[0] = 61;
		inbornSeq[41] = inborns6level[1] = 62;
		inbornSeq[42] = inborns6level[2] = 63;
		inbornSeq[43] = inborns6level[3] = 64;
		inbornSeq[44] = inborns6level[4] = 65;
		inbornSeq[45] = inborns6level[5] = 66;
		inbornSeq[46] = inborns6level[6] = 67;
		inbornSeq[47] = inborns6level[7] = 68;
		inbornSeq[48] = inborns7level[0] = 71;
		inbornSeq[49] = inborns7level[1] = 72;
		inbornSeq[50] = inborns7level[2] = 73;
		inbornSeq[51] = inborns7level[3] = 74;
		inbornSeq[52] = inborns7level[4] = 75;
		inbornSeq[53] = inborns7level[5] = 76;
		inbornSeq[54] = inborns7level[6] = 77;
		inbornSeq[55] = inborns7level[7] = 78;
		inbornSeq[56] = inborns8level[0] = 81;
		inbornSeq[57] = inborns8level[1] = 82;
		inbornSeq[58] = inborns8level[2] = 83;
		inbornSeq[59] = inborns8level[3] = 84;
		inbornSeq[60] = inborns8level[4] = 85;
		inbornSeq[61] = inborns8level[5] = 86;
		inbornSeq[62] = inborns8level[6] = 87;
		inbornSeq[63] = inborns8level[7] = 88;
		inbornSeq[64] = inborns9level[0] = 91;
		inbornSeq[65] = inborns9level[1] = 92;
		inbornSeq[66] = inborns9level[2] = 93;
		inbornSeq[67] = inborns9level[3] = 94;
		inbornSeq[68] = inborns9level[4] = 95;
		inbornSeq[69] = inborns9level[5] = 96;
		inbornSeq[70] = inborns9level[6] = 97;
		inbornSeq[71] = inborns9level[7] = 98;    
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		ReloadResult result = fire.pb.buff.Module.getInstance().reload();
		if(!result.isSuccess())
		{
			result.appendMsg(" skill module reload failed, ");
			return result;
		}
		try
		{
			Module m = new Module();
			m.init();
			ModuleManager.getInstance().putModuleByName(MODULE_NAME, m);
			return result;
		}
		catch(Exception e)
		{
			logger.error(e);
			result.setSuccess(false);
			result.appendMsg("skill module reload failed, ");
			return result;
		}
	}
	
}
