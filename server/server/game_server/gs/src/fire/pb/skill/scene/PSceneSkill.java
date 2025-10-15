package fire.pb.skill.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;










import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.IBuff;
import fire.pb.buff.OperateType;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.single.Cure;
import fire.pb.buff.single.FixedInjure;
import fire.pb.effect.RoleImpl;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Module;
import fire.pb.skill.SkillRole;
import fire.pb.skill.SubSkillConfig;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.team.TeamMemberState;
import fire.pb.util.ParseObjIDOutBattle;
import fire.script.AbstractJSEngine;
import fire.script.IJavaScriptEngine;
import fire.script.JavaScript;
import fire.script.SceneJSEngine;

public class PSceneSkill extends mkdb.Procedure
{
	
	public static final int GOAL_SELF_ROLE =1;
	public static final int GOAL_SELF_PET =2;
	public static final int GOAL_TEAM_ROLE =3;
	
	final private int skillId;
	final private long opRoleId;
	final private long goalId;
	final private int goalType;
	final private SceneSkillConfig skillconfig;
	
	public PSceneSkill(final long opRoleId,final int skillId,  final long goalId, final int goalType)
	{
		this.opRoleId = opRoleId;
		this.skillId = skillId;
		this.goalId = goalId;
		this.goalType = goalType;
		this.skillconfig = fire.pb.skill.Module.getInstance().getSceneSkillConfig(skillId);
		if (skillconfig == null)
		{
			Module.logger.error("ERROR: Scene Skill is not exist. skillId = " + skillId);
			throw new IllegalArgumentException("ERROR: Scene Skill is not exist. skillId = " + skillId);
		}
	}
	
	@Override
	protected boolean process()
	{
		boolean selfonly = true;
		ParseObjIDOutBattle objs = new ParseObjIDOutBattle(skillconfig.getTargettype());
		Team team = null;
		//lock
		if(!objs.teammate)
		{
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[]{opRoleId}));
		}
		else
		{
			Long teamId = xtable.Roleid2teamid.select(opRoleId);
			if(teamId == null)
			{
				//no team only lock self
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[]{opRoleId}));
			}
			else
			{
				team = TeamManager.selectTeamByTeamID(teamId);
				int opteamstate = team.getTeamMemberState(opRoleId);
				if(opteamstate == TeamMemberState.eTeamAbsent)
				{
					//lock self
					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[]{opRoleId}));
				}
				else
				{
					List<Long> normalMemberIds = team.getNormalMemberIds();
					if(normalMemberIds.size() == 1)
					{
						//lock self
						lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[]{opRoleId}));
					}
					else
					{
						//lock team
						lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, new Object[]{teamId}));
						//锁队员
						lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, normalMemberIds));
						selfonly = false;
					}
				}
			}
		}
		
		// check state
		BuffAgent buffagent = new BuffRoleImpl(opRoleId);
		if(!buffagent.canAddBuff(OperateType.USE_SCENE_SKILL))
			return true;
		
		//check level
		SkillRole skillrole = new SkillRole(opRoleId);
		int skilllevel = skillrole.getLevel(skillId);
		if(skilllevel == 0)
			skilllevel =1;

		//set skill lv
		SceneJSEngine engine = new SceneJSEngine();
		engine.setSkillLevel(skilllevel);
		
		//chack goal
		switch(goalType)
		{
		case GOAL_SELF_PET:
			PetColumn pets = new PetColumn(opRoleId, PetColumnTypes.PET, false);
			xbean.PetInfo petinfo = pets.getPetInfo((int)goalId);
			if(petinfo == null)
				return true;
			break;
		case GOAL_TEAM_ROLE:
			if(team == null)
				return true;
			if(!team.getNormalMemberIds().contains(goalId))
				return true;
			break;
		}
		
		
		RoleImpl erole = new RoleImpl(opRoleId,false);
		engine.setOpERole(erole);
		int hpconsume = getHPconsume(engine);
		if(erole.getHp() < -hpconsume)
			return true;
		int mpconsume = getMPconsume(engine);
		if(erole.getMp() < -mpconsume)
			return true;
		int spconsume = getSPconsume(engine);
		if(erole.getSp() < -spconsume)
			return true;
		
		SubSkillConfig mainsubskill = skillconfig.getSubSkills()[0];
		int targetcount = mainsubskill.getTargetCountJs().eval(engine,null,null).intValue();
		engine.setSkillTargetNum(targetcount);
		List<BuffAgent> buffAgents = new ArrayList<BuffAgent>();

		if(goalType == GOAL_SELF_ROLE)
		{
			buffAgents.add(new fire.pb.buff.BuffRoleImpl(opRoleId));
		}
		else if(goalType == GOAL_SELF_PET)
		{
			buffAgents.add(new fire.pb.buff.BuffPetImpl(opRoleId,(int)goalId));
		}
		else if(goalType == GOAL_TEAM_ROLE)
		{
			buffAgents.add(new fire.pb.buff.BuffRoleImpl(goalId));
		}
		
		if(objs.role && goalType != GOAL_SELF_ROLE)
		{
			buffAgents.add(new fire.pb.buff.BuffRoleImpl(opRoleId));
		}
		if(objs.petself && goalType != GOAL_SELF_PET)
		{
			Integer petkey = xtable.Properties.selectFightpetkey(opRoleId);
			if(petkey != -1)
				buffAgents.add(new fire.pb.buff.BuffPetImpl(opRoleId,petkey));
		}
		if(!selfonly && team != null)
		{
			for(long teamMemberId : team.getNormalMemberIds())
			{
				if(teamMemberId != goalId)
					buffAgents.add(new fire.pb.buff.BuffRoleImpl(teamMemberId));
			}
		}
		
		for (SubSkillConfig subskill : skillconfig.getSubSkills())
		{
			float fTargetRate = subskill.getTargetRate().eval(engine,null,null).floatValue();
			if(fTargetRate <= Math.random())
				continue;
			for(BuffUnit buffarg :subskill.getBuffUnits())
			{
				if(buffarg == null)
					break;	
				IBuff buff = fire.pb.buff.Module.getInstance().createBuff(buffarg.buffIndex);
				int count = 0;
				for(BuffAgent agent :buffAgents)
				{
					engine.setAimERole(agent.getERole());
					Map<Integer,Float> effects = calcEffects(engine, buffarg.effectJavascriptMap);
					if(buff instanceof ConstantlyBuff)
					{
						ConstantlyBuff cbuff = (ConstantlyBuff)buff.copy();
						cbuff.setEffects(effects);
						agent.addCBuffWithSP(cbuff);
					}
					else if(buff instanceof Cure)
					{
						((Cure)(buff.copy())).useOutBattleWithSP(agent.getERole(), effects);
					}
					else if(buff instanceof FixedInjure)
					{
						if(effects.get(EffectType.HP_ABL)!=null)
							hpconsume += effects.get(EffectType.HP_ABL);
						if(effects.get(EffectType.HP_PCT)!=null)
							hpconsume += effects.get(EffectType.HP_PCT)*erole.getHp();
						if(effects.get(EffectType.MP_ABL)!=null)
							mpconsume += effects.get(EffectType.MP_ABL);
						if(effects.get(EffectType.MP_PCT)!=null)
							mpconsume += effects.get(EffectType.MP_PCT)*erole.getMp();
						if(effects.get(EffectType.SP_ABL)!=null)
							hpconsume += effects.get(EffectType.SP_ABL);
						if(effects.get(EffectType.SP_PCT)!=null)
							hpconsume += effects.get(EffectType.SP_PCT)*erole.getSp();
					}
					else
					{
						Module.logger.error("PSceneSkill ERROR: 战斗外使用技能，不支持的buff，Id=" + buff.getId());
					}
					
					count++;
					if(count >= targetcount)
						break;
				}
			}		
		}
		
		SRefreshRoleData roledata = new SRefreshRoleData();
		if(hpconsume != 0)
		{
			erole.addHp(hpconsume);
			roledata.datas.put(AttrType.HP, (float)erole.getHp());
		}
		if(mpconsume != 0)
		{
			erole.addMp(mpconsume);
			roledata.datas.put(AttrType.MP, (float)erole.getMp());
		}
		if(spconsume != 0)
		{
			erole.addSp(spconsume, 0);
			roledata.datas.put(AttrType.SP, (float)erole.getSp());
		}
		psendWhileCommit(opRoleId, roledata);
		fire.pb.battle.Module.getInstance().checkBattleBuff( opRoleId );
		return true;
	}
	
	
	private Map<Integer,Float> calcEffects(SceneJSEngine engine,Map<Integer,JavaScript> scripts)
	{
		Map<Integer,Float> effects = new HashMap<Integer, Float>();
		
		for(Map.Entry<Integer, JavaScript> script : scripts.entrySet())
		{
			effects.put(script.getKey(),script.getValue().eval(engine,null,null).floatValue());
		}
		
		return effects;
	}
	
	private int getHPconsume(AbstractJSEngine engine)
	{
		Double result = -skillconfig.getHpConsumeJavaScript().eval(engine,null,null);
		return result.intValue();
	}
	
	private int getMPconsume(AbstractJSEngine engine)
	{
		Double result = -skillconfig.getMpConsumeJavaScript().eval(engine,null,null);
		return result.intValue();
	}
	
	private int getSPconsume(AbstractJSEngine engine)
	{
		Double result = -skillconfig.getSpConsumeJavaScript().eval(engine,null,null);
		return result.intValue();
	}
}
