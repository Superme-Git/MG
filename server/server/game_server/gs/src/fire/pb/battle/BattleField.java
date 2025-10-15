package fire.pb.battle;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import fire.log.Logger;
import fire.pb.PropConf;
import fire.pb.PropRole;
import fire.pb.activity.winner.WinnerManager;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.ai.AIOperation;
import fire.pb.battle.ai.BattleAIManager;
import fire.pb.battle.ai.TargetFilter;
import fire.pb.battle.livedie.LiveDieMange;
import fire.pb.battle.operate.AiOperate;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.buff.continual.DecisionBuff;
import fire.pb.effect.Role;
import fire.pb.effect.RoleImpl;
import fire.pb.main.ConfigManager;
import fire.pb.move.SpriteComponents;
import fire.pb.npc.SNpcShape;
import fire.pb.option.Attack;
import fire.pb.option.BasicOperation;
import fire.pb.option.CatchPet;
import fire.pb.option.ChangeEnvironment;
import fire.pb.option.Defend;
import fire.pb.option.EndBattle;
import fire.pb.option.FetchPet;
import fire.pb.option.InstantRunaway;
import fire.pb.option.PAddProtector;
import fire.pb.option.Protect;
import fire.pb.option.Rest;
import fire.pb.option.Runaway;
import fire.pb.option.SummonBack;
import fire.pb.option.SummonMonster;
import fire.pb.option.SummonPet;
import fire.pb.option.UseItem;
import fire.pb.option.UseSkill;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.school.School;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Module;
import fire.pb.skill.Result;
import fire.pb.skill.SkillAgent;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.Misc;
import fire.pb.util.ParseObjIDInBattle;
import fire.pb.util.Parser.ID2Odds;
import mkdb.Procedure;
import xbean.BattleInfo;
import xbean.CameraInfo;

/**战场*/
public class BattleField
{
	public static final Logger logger = Logger.getLogger("BATTLE");
	public static final int MAX_FIGHT_STAR = 5;

	public static final int HOST_FIRST_ID = 1;
	public static final int GUEST_FIRST_ID = 15;
	public static final int A_SIDE_FIGHTER_NUM = 14;

	public static final int BATTLE_WIN = 1;
	public static final int BATTLE_LOSE = -1;
	public static final int BATTLE_DRAW = 0;
	public static final int BATTLE_NOT_END = 2;

	// 战斗胜利判定条件：
	public static final int BATTLE_KILLALL = 1; //清除敌方全部角色——战斗胜利
	public static final int BATTLE_ALL = 2; //必败战斗——进入战斗即胜利
	public static final int BATTLE_LEADER_SURVIVAL = 3; //清除敌方全部角色，并且队长存活——战斗胜利
	public static final int BATTLE_ALL_SURVIVAL = 4; //清除敌方全部角色，并且全部角色存活战斗胜利

	public static final int ACTION_AUTO_TIMEOUT_DELAY = 3;// 改回来直接设置，为了方便，将来有必要再拿出去
	public static final int ACTION_TIMEOUT_DELAY = 30;// 改回来直接设置，为了方便，将来有必要再拿出去
	public static final int FEEDBACK_TIMEOUT_DELAY = 50; // 这个播放时间delay，是在战斗脚本播放的最小时间基础上的加成值，也就是说
															// 最大播放时间 = 最小播放时间 + FEEDBACK_TIMEOUT_DELAY

	public static final int MAX_ROUND_PLAY_MILLI_TIME = 120000;// 120秒  最大播放时间，防止计算出错

	public static final int FIRST_WATCHER_BATTLE_INDEX = BattleField.A_SIDE_FIGHTER_NUM * 2 + 3;

	// 跟观战有关的常量
	//系统最大回合数
//	public static final int MAX_BATTLE_ROUND = 150;
	

	/**创建一场强制PK战斗*/
	public static void launchBattlePKorDuel(final long roleID, final long otherRoleID, int battleType, BattleEndHandler handler)
	{

		new PNewBattle(roleID, otherRoleID, battleType, handler).submit();
	}

	/**获取指定战斗索引单元的敌方*/
	public static java.util.List<Integer> getEnemies(final xbean.BattleInfo battle, final int index)
	{

		final java.util.List<Integer> retList = new java.util.LinkedList<Integer>();
		xbean.Fighter fighterb = battle.getFighters().get(index);
		for (Map.Entry<Integer, xbean.Fighter> f : battle.getFighters().entrySet())
		{
			if (f.getValue().getIshost() != fighterb.getIshost())
				retList.add(f.getKey());
		}
		return retList;
	}

	public static boolean isFighting(long roleId)
	{

		Long battleId = xtable.Roleid2battleid.select(roleId);
		return (battleId != null);
	}

	/**找到一方队长的FighterID，一方没有队伍或者暂离队伍时应该只有一个人，返回其id*/
	public static int getLeaderFighterId(final xbean.BattleInfo battle, final boolean ishost)
	{

		for (xbean.Fighter fighter : battle.getFighters().values())
		{
			if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				if (fighter.getIshost() != ishost)
					continue;
				long roleId = fighter.getUniqueid();
				Team team = TeamManager.selectTeamByRoleId(roleId);
				if (team == null)
					return fighter.getBattleindex();// 没有队伍，自己在战斗中
				long leaderId = team.getTeamInfo().getTeamleaderid();
				Integer fid = battle.getRoleids().get(leaderId);
				if (fid == null)
					return fighter.getBattleindex();// 队长不在本战斗中
				else
					return fid;
			}
		}
		return 0;
	}

	/**getLeaderRoleid:拿到队长的id,如果没有队伍,则返回个人的id*/
	public static long getLeaderRoleid(final xbean.BattleInfo battle, final boolean isHost)
	{
		int index = BattleField.getLeaderFighterId(battle, isHost);
		xbean.Fighter fighter = battle.getFighters().get(index);
		long leaderRoleid = 0;
		if (fighter != null)
			leaderRoleid = fighter.getUniqueid();
		return leaderRoleid;
	}

	/**随机一个友方*/
	public static int getRandFriend(final xbean.BattleInfo battle, final int index)
	{

		final java.util.List<Integer> friends = BattleField.getFriends(battle, index);
		friends.remove((Integer) index);// 移除自己
		if (friends.size() == 0)
			return 0;
		final int select = mkdb.Mkdb.random().nextInt(friends.size());
		return friends.get(select);
	}

	/**随机一个活着的友方*/
	public static int getRandLiveFriend(final xbean.BattleInfo battle, final int fighterId)
	{

		final java.util.List<Integer> friends = BattleField.getLiveFriends(battle, fighterId);
		friends.remove((Integer) fighterId);// 移除自己
		if (friends.size() == 0)
			return 0;
		final int select = mkdb.Mkdb.random().nextInt(friends.size());
		return friends.get(select);
	}

	/**是否是友方*/
	public static boolean isFriend(final xbean.BattleInfo battle, final int operator, final int aim)
	{

		xbean.Fighter opfighterb = battle.getFighters().get(operator);
		xbean.Fighter aimfighterb = battle.getFighters().get(aim);
		if (opfighterb == null || aimfighterb == null)
			return false;
		if (opfighterb.getIshost() == aimfighterb.getIshost())
			return true;
		else
			return false;
	}

	/**获取所有战斗单元的友方,包括自己和自己的宠物 -- 基本上不考虑宠物站位1-5  1--14  15-19 25-29*/
	public static int getFriendEmptyPos(final xbean.BattleInfo battle, final int index)
	{
		xbean.Fighter fighterb = battle.getFighters().get(index);
		int begin = BattleField.HOST_FIRST_ID + (fighterb.getIshost()?0:BattleField.A_SIDE_FIGHTER_NUM)+5;
		int end = begin+BattleField.A_SIDE_FIGHTER_NUM-5;
		
		for (int i = begin; i < end; i++)
		{
			if(BattleField.checkOutBattle(battle, i))
				return i;
		}
		return 0;
	}

	/**获取所有战斗单元的友方,包括自己和自己的宠物*/
	public static int getEnemyEmptyPos(final xbean.BattleInfo battle, final int index)
	{
		xbean.Fighter fighterb = battle.getFighters().get(index);
		int begin = BattleField.HOST_FIRST_ID + (fighterb.getIshost()?BattleField.A_SIDE_FIGHTER_NUM:0);
		int end = begin+BattleField.A_SIDE_FIGHTER_NUM;
		
		for (int i = begin; i < end; i++)
		{
			if(BattleField.checkOutBattle(battle, i))
				return i;
		}
		return 0;
	}
	
	/**获取所有战斗单元的友方,包括自己和自己的宠物*/
	public static java.util.List<Integer> getFriends(final xbean.BattleInfo battle, final int index)
	{

		final java.util.List<Integer> retList = new java.util.LinkedList<Integer>();
		xbean.Fighter fighterb = battle.getFighters().get(index);
		for (Map.Entry<Integer, xbean.Fighter> f : battle.getFighters().entrySet())
		{
			if (f.getValue().getIshost() == fighterb.getIshost())
				retList.add(f.getKey());
		}
		return retList;
	}

	/**获取战斗单元所有活着的友方,包括自己和自己的宠物*/
	public static java.util.List<Integer> getLiveFriends(final xbean.BattleInfo battle, final int fighterId)
	{

		final java.util.List<Integer> retList = new java.util.LinkedList<Integer>();
		xbean.Fighter fighterb = battle.getFighters().get(fighterId);
		for (Map.Entry<Integer, xbean.Fighter> f : battle.getFighters().entrySet())
		{
			if (f.getValue().getIshost() == fighterb.getIshost() && checkLiveInBattle(battle, f.getKey()))
				retList.add(f.getKey());
		}
		return retList;
	}
	
	/**获取战斗单元所有活着的友方,包括自己*/
	public static java.util.List<Integer> getLiveFriendNoPets(final xbean.BattleInfo battle, final int fighterId)
	{

		final java.util.List<Integer> retList = new java.util.LinkedList<Integer>();
		xbean.Fighter fighterb = battle.getFighters().get(fighterId);
		for (Map.Entry<Integer, xbean.Fighter> f : battle.getFighters().entrySet())
		{
			if (f.getValue().getIshost() == fighterb.getIshost()&&f.getValue().getFightertype() != xbean.Fighter.FIGHTER_PET && checkLiveInBattle(battle, f.getKey()))
				retList.add(f.getKey());
		}
		return retList;
	}

	/**获取战斗单元所有活着的敌方,包括宠物*/
	public static java.util.List<Integer> getLiveEnemys(final xbean.BattleInfo battle, final int fighterId)
	{

		final java.util.List<Integer> retList = new java.util.LinkedList<Integer>();
		xbean.Fighter fighterb = battle.getFighters().get(fighterId);
		for (Map.Entry<Integer, xbean.Fighter> f : battle.getFighters().entrySet())
		{
			if (f.getValue().getIshost() != fighterb.getIshost() && checkLiveInBattle(battle, f.getKey()))
				retList.add(f.getKey());
		}
		return retList;
	}
	
	/**获取战斗单元所有活着的敌方,不包括宠物*/
	public static java.util.List<Integer> getLiveEnemyNoPets(final xbean.BattleInfo battle, final int fighterId)
	{

		final java.util.List<Integer> retList = new java.util.LinkedList<Integer>();
		xbean.Fighter fighterb = battle.getFighters().get(fighterId);
		for (Map.Entry<Integer, xbean.Fighter> f : battle.getFighters().entrySet())
		{
			if (f.getValue().getIshost() != fighterb.getIshost()&&f.getValue().getFightertype() != xbean.Fighter.FIGHTER_PET && checkLiveInBattle(battle, f.getKey()))
				retList.add(f.getKey());
		}
		return retList;
	}
	
	public static int getRandTargetByType(final xbean.BattleInfo battle, final int fighterId, final int targetType, boolean examdead, boolean examhide, java.util.List<Integer> filterbuffids)
	{
		ParseObjIDInBattle objinbattle = new ParseObjIDInBattle(targetType);

		List<Integer> aims = new ArrayList<Integer>();
		aims.addAll(battle.getFighters().keySet());
		Misc.randomlizeList(aims);

		//做2次选择是先过滤下变羊的怪和图腾怪。没找到在找一次。
		for (int aim : aims)
		{
			Fighter fighter = battle.getFighterobjects().get(aim);
			
			boolean ok = true;
			if (filterbuffids != null) //过滤掉掉 目标带有这些BUF的 by changhao
			{
				for (Integer buffid : filterbuffids)
				{
					boolean exit = fighter.getBuffAgent().existBuff(buffid);
					if (exit)
					{
						ok = false;
						break;
					}					
				}
			}
			
			if (ok == false)
			{
				continue;
			}

			if(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SLEEP2)||
					fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
				continue;
			
			if (BattleField.checkOutBattle(battle, aim))
				continue;
			if(examdead&&fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH))
					continue;
			if(examhide&&(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ROLE_HIDDEN)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_FIRST)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_SECOND)))
				continue;
		
			int relation = BattleField.getAimRelation(battle, fighterId, aim);
			if (objinbattle.validTarget(relation))
			{
				if(aim==relation)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("getRandTargetByType() figterid为:" + fighterId + "和目标："+aim + "有以下"+ relation + "关系，满足触发条件" + targetType);}
				}
				return aim;
			}
		}
		
		for (int aim : aims)
		{
			Fighter fighter = battle.getFighterobjects().get(aim);
			if (BattleField.checkOutBattle(battle, aim))
				continue;
			if(examdead&&fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH))
					continue;
			if(examhide&&(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ROLE_HIDDEN)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_FIRST)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DETECT_HIDDEN_SECOND)))
				continue;
		
			int relation = BattleField.getAimRelation(battle, fighterId, aim);
			if (objinbattle.validTarget(relation))
			{
				if(aim==relation)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("getRandTargetByType() figterid为:" + fighterId + "和目标："+aim + "有以下"+ relation + "关系，满足触发条件" + targetType);}
				}
				return aim;
			}
		}

		return 0;
	}

	/**随机一个敌方*/
	public static int getRandEnemy(final xbean.BattleInfo battle, final int index)
	{

		final java.util.List<Integer> enemies = BattleField.getEnemies(battle, index);
		if (enemies.size() == 0)
			return 0;
		final int select = mkdb.Mkdb.random().nextInt(enemies.size());
		return enemies.get(select);
	}

	/**获取一个合击的协助者*/
	public static int getAttackAssistor(final xbean.BattleInfo battle, final int index, final int aim)
	{

		final java.util.List<Integer> friends = BattleField.getFriends(battle, index);
		friends.remove((Integer) index);
		for (int friend : friends)
		{
			if (!checkLiveInBattle(battle, friend))
				continue;// 去除不能行动的队友
			if ((battle.getRoundresult().get(friend) & ResultType.RESULT_REST) != 0)
				continue;// 去除已经行动过的队友
			xbean.Decision decision = battle.getDecisions().get(friend);
			if (decision == null)
				continue;// 会有fighter没有decision
			Fighter assistFighter = battle.getFighterobjects().get(friend);
			if (!assistFighter.canAction(OperationType.ACTION_ATTACK, 0))
				continue;// 不能普通物理攻击
			if (decision.getAim() == aim && decision.getOperatetype() == OperationType.ACTION_ATTACK)
				return friend;// 未行动过 && 同目标 && 普通攻击

		}
		return 0;
	}

	public static xbean.Decision actionToDecision(BasicOperation basicOperation)
	{

		final xbean.Decision d = xbean.Pod.newDecision();
		d.setAim(basicOperation.getAim());
		d.setOperateid(basicOperation.getOperateID());
		d.setOperatetype(basicOperation.getType());
		d.setOperator(basicOperation.getOperator());

		return d;
	}

	/**将角色发送的决策转化成相应的“操作”*/
	public static BasicOperation decisionToAction(final xbean.BattleInfo battle, final Fighter fighter, xbean.Decision decision)
	{

		if (null == decision)
			return null;

		// 检查混乱更换decision
		if (fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS)||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
		{
			int fighterid = 0;
			if(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
			{
				fighterid = BattleField.getRandLiveFriend(battle, fighter.getFighterId());
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击getRandLiveFriend()找到的目标是figterid为:" + fighterid + " 的战斗者");}
			}
			else
			{
				fighterid = BattleField.getRandTargetByType(battle, fighter.getFighterId(),4606,true,true, null);
				if(fighter.getFighterId() == fighterid)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击getRandTargetByType()找到的目标是自己！！！！！！！！！！！！！！！！！！！");}
					fighterid = BattleField.getRandTargetByType(battle, fighter.getFighterId(),4606,true,true, null);
				}
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击getRandTargetByType()找到的目标是figterid为:" + fighterid + " 的战斗者");}
			}
		
				
			if (fighterid == 0)
				return new Rest(battle, fighter.getFighterId());
			else
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击目标是figterid为:" + fighterid + " 的战斗者");}				
				return new UseSkill(battle, fighter.getFighterId(), fighterid, SkillConstant.BATTLE_SKILL_BASIC_CHAOS_ATTACK);
			}
		}		
		
		//处理自动扑捉宝宝逻辑
		dealBabyFight(battle,fighter,decision,true);
		

		// 检查强迫决定buff,更改decision
		DecisionBuff dbuff = fighter.getDecisonBuff();
		if (dbuff != null)
		{
			if(!(dbuff.getId() == BuffConstant.CONTINUAL_REST&&(decision.getOperatetype() == OperationType.ACTION_ESCAPE||decision.getOperatetype() == OperationType.ACTION_SUMMON_INSTANT||
					decision.getOperatetype() == OperationType.ACTION_SUMMON||decision.getOperatetype() == OperationType.ACTION_WITHDRAW)))
				decision = ((DecisionBuff) dbuff).getDecision();
		}
/*		else
		{
			dbuff = fighter.getHateDecisonBuff();
			if (dbuff != null)
				decision = ((DecisionBuff) dbuff).getDecision();
		}*/
	
		if(decision.getOperatetype() == OperationType.ACTION_SKILL )
		{
			int nskill = 0;
			if(decision.getOperateid() == SkillConstant.BATTLE_ROLE_HUNTER_1||decision.getOperateid() == SkillConstant.BATTLE_ROLE_HUNTER_2||decision.getOperateid() == SkillConstant.BATTLE_ROLE_HUNTER_3)
				nskill = SkillConstant.BATTLE_ROLE_BEHUNTER;
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_1||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_1||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_1)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_1;
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_2||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_2||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_2)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_2;
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_3||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_3||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_3)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_3;			
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_4||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_4||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_4)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_4;			
			if(nskill != 0 &&decision.getOperateid() != nskill &&!fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_BEHUNTER))
			{
				decision.setOperateid(nskill);				
				decision.setAim(fighter.getFighterId());				
			}
		}		
		
		switch (decision.getOperatetype())
		{
		case OperationType.ACTION_ATTACK:
			return new Attack(battle, decision.getOperator(), decision.getAim());

		case OperationType.ACTION_CATHCH:
			return new CatchPet(battle, decision.getOperator(), decision.getAim());

		case OperationType.ACTION_DEFEND:
			return new Defend(battle, decision.getOperator());

		case OperationType.ACTION_ESCAPE:
			return new Runaway(battle, decision.getOperator());

		case OperationType.ACTION_PROTECT:
			return new Protect(battle, decision.getOperator(), decision.getAim());

		case OperationType.ACTION_SUMMON:
			return new SummonPet(battle, decision.getOperator(), decision.getOperateid());

		case OperationType.ACTION_USEITEM:
			return new UseItem(battle, decision.getOperator(), decision.getAim(), decision.getOperateid());

			
		case OperationType.ACTION_SPECIAL_SKILL:
		case OperationType.ACTION_UNIQUE_SKILL:
		case OperationType.ACTION_SKILL:			
			return new UseSkill(battle, decision.getOperator(), decision.getAim(), decision.getOperateid());

		case OperationType.ACTION_WITHDRAW:
			return new FetchPet(battle, decision.getOperator(), decision.getAim());

		case OperationType.ACTION_REST:
			return new Rest(battle, decision.getOperator());

		default:
			break;
		}

		return null;
	}

	/**AIAction转化为Decision*/
	public static xbean.Decision aiActionToDecision(final xbean.BattleInfo battle, final Fighter fighter, AIOperation action)
	{
		// AI换动作
		if (null == action)
			return null;
		xbean.Decision decision = xbean.Pod.newDecision();
		decision.setOperator(fighter.getFighterId());
		decision.setAim(0);
		decision.setOperatetype(action.getOperationType());
		decision.setOperateid(action.getOperationId());
		
		// 带技能槽的ai要重新修正
		if(action.getOperationType() == OperationType.ACTION_SKILL&& action.getOperationId() < 1 
				&& action.getSkillSoltID().size() > 0 && fighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER) //伙伴 by changhao
		{
			int ncount = 0;
			int nSoltid = action.getSkillSoltID().get(0)-1;
			float rate = (float) Math.random();
			for(float nSoltodds:action.getSkillSoltOdds()) //技能槽释放概率 by changhao
			{
				if(rate < nSoltodds)
				{
					nSoltid = action.getSkillSoltID().get(ncount)-1;
					break;
				}
				rate -= nSoltodds;
				ncount++;
			}			
			if(fighter.getSkillAgent().getAllBattleSkills().size() > nSoltid&&fighter.getSkillAgent().getAllBattleSkills().get(nSoltid) > 0)
				decision.setOperateid(fighter.getSkillAgent().getAllBattleSkills().get(nSoltid));
			else
			{
				Module.logger.error("ERROR: ai动作fighter需要使用一个fighter没有的技能 skillId = " + action.getOperationId()+
						" fighterid = " +fighter.getFighterBean().getUniqueid()+" fightertype = " +fighter.getFighterBean().getFightertype()+
						" aiact.id = " +action.getIndex());
				throw new IllegalArgumentException("ERROR: ai动作fighter需要使用一个fighter没有的技能 skillId = " + action.getOperationId()+
						" fighterid = " +fighter.getFighterBean().getUniqueid()+" fightertype = " +fighter.getFighterBean().getFightertype()+
						" aiact.id = " +action.getIndex());
			}
		}
			
		if (action.getGoal() != -1)
		{
			// ai中有目标选择
			TargetFilter filter = BattleAIManager.getInstance().getTargetFilters().get(action.getGoal());
			if (filter == null)
				return null;
			if(action.getChangeAim())
			{// 如果是战场ai指定目标执行的行动。目标将进行切换。
				decision.setAim(-1);
			}
			else
			{
				List<Integer> aims = fighter.getAiagent().getTargets(action.getGoal());
				if (aims.size() != 0)
				{
					if (filter.sortAttrId != -1)// 如果有排序变量，则按第一个为目标
						decision.setAim(aims.get(0));
					else
						decision.setAim(Misc.getRandom(aims));
				}
			}
		}
		return decision;
	}

	/**处理自动扑捉宝宝逻辑*/
	public static void dealBabyFight(final xbean.BattleInfo battle,final Fighter fighter,xbean.Decision decision,boolean sendmsg){
		boolean havababy=false;
		int babyindex=-1;
		int round=0;
		for (Map.Entry<Integer, xbean.Monster> entry : battle.getMonsters().entrySet())
		{
			if(entry==null){
				continue;
			}
			if (entry.getValue().getMonstertype() == Monster.MONSTER_BABY){
				if(!checkOutBattle(battle,entry.getKey())){
					babyindex=entry.getKey();//设置宝宝索引
					havababy=true;
					battle.setBabyflag(1);
					
					xbean.Fighter f = battle.getFighters().get(babyindex);
					round=f.getRound();
					break;
				}
			}
		}
		
		//有宝宝存在并且活着
		if(havababy){
			//设置宝宝出现的第一回合
			if(fighter.isPartner()){
				if(round==1||round==0||round==2){//第一回合
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("宝宝存在并且活着。 施法者Id: ").append(fighter.getFighterId()).append(" ；改为执行防御"));}
					decision.setOperatetype(OperationType.ACTION_DEFEND);
				}else{
					if(battle.getBabybagfull()==0){
						//伙伴设置为防御
						if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("宝宝存在并且活着。 施法者Id: ").append(fighter.getFighterId()).append(" ；改为执行防御"));}
						decision.setOperatetype(OperationType.ACTION_DEFEND);
					}
				}
			}
			if(fighter.isPet()&&(decision.getIsauto()||round==0)){
				//伙伴设置为防御
				if(round==1||round==0||round==2){
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("宝宝存在并且活着。 施法者Id: ").append(fighter.getFighterId()).append(" ；改为执行防御"));}
					decision.setOperatetype(OperationType.ACTION_DEFEND);
				}else{
					if(battle.getBabybagfull()==0){
						if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug(new StringBuilder("宝宝存在并且活着。 施法者Id: ").append(fighter.getFighterId()).append(" ；改为执行防御"));}
						decision.setOperatetype(OperationType.ACTION_DEFEND);
					}
				}
				
			}
			if(fighter.isRole()&&babyindex!=-1&&(decision.getIsauto()||round==0))
			{
				//设置为扑捉
				if(round==0||(battle.getBattletype() == BattleType.BATTLE_LINE && fighter.getBounusNum() <=0))
				{
					if(round!=0&&sendmsg == true)
						fire.pb.talk.MessageMgr.sendMsgNotify(fighter.getFighterBean().getUniqueid(), 166131, null);
					decision.setOperatetype(OperationType.ACTION_DEFEND);
					decision.setAim(babyindex);
				}
				else if(round==1||round==2)
				{
					decision.setOperatetype(OperationType.ACTION_CATHCH);
					decision.setAim(babyindex);
				}
				else
				{
					if(battle.getBabybagfull()==0){
						decision.setOperatetype(OperationType.ACTION_CATHCH);
						decision.setAim(babyindex);
					}
				}
			}
		}
	}
	
	
	/**AIAction转化为BasicOperation*/
	public static BasicOperation aiActionToOperation(final xbean.BattleInfo battle, final Fighter fighter, AIOperation action)
	{

		xbean.Decision decision = aiActionToDecision(battle, fighter, action);

		// 检查混乱更换decision
		if ((fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS) ||fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
				&& (decision.getOperatetype() != OperationType.ACTION_ENVIRONMENTCHANGE && decision.getOperatetype() != OperationType.ACTION_BATTLE_END)
				&&!(fighter.getFightertype()>= xbean.Fighter.FIGHTER_PARTNER && (decision.getOperatetype() == OperationType.ACTION_ESCAPE||decision.getOperatetype() == OperationType.ACTION_ESCAPE_INSTANT)))
		{
			int fighterid = 0;
			if(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
			{
				fighterid = BattleField.getRandLiveFriend(battle, fighter.getFighterId());
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击getRandLiveFriend()找到的目标是figterid为:" + fighterid + " 的战斗者");}
			}
			else
			{
				fighterid = BattleField.getRandTargetByType(battle, fighter.getFighterId(),4606,true,true, null);
				if(fighter.getFighterId() == fighterid)
					fighterid = BattleField.getRandTargetByType(battle, fighter.getFighterId(),4606,true,true, null);
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击getRandTargetByType()找到的目标是figterid为:" + fighterid + " 的战斗者");}
			}	
			
			if (fighterid == 0)
				return new Rest(battle, fighter.getFighterId());
			else
			{
				if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击目标是figterid为:" + fighterid + " 的战斗者");}
				return new UseSkill(battle, fighter.getFighterId(), fighterid, SkillConstant.BATTLE_SKILL_BASIC_CHAOS_ATTACK);
			}
		}

		//处理自动扑捉宝宝逻辑
		dealBabyFight(battle,fighter,decision,true);	

		// 检查强迫决定buff,更改decision
		DecisionBuff dbuff = fighter.getDecisonBuff();
		if (dbuff != null)
		{
			if(!(dbuff.getId() == BuffConstant.CONTINUAL_REST&&(decision.getOperatetype() == OperationType.ACTION_ESCAPE||decision.getOperatetype() == OperationType.ACTION_SUMMON_INSTANT||
					decision.getOperatetype() == OperationType.ACTION_SUMMON||decision.getOperatetype() == OperationType.ACTION_WITHDRAW)))
				decision = ((DecisionBuff) dbuff).getDecision();
		}		
		
		if(decision.getOperatetype() == OperationType.ACTION_SKILL )
		{
			int nskill = 0;
			if(decision.getOperateid() == SkillConstant.BATTLE_ROLE_HUNTER_1||decision.getOperateid() == SkillConstant.BATTLE_ROLE_HUNTER_2||decision.getOperateid() == SkillConstant.BATTLE_ROLE_HUNTER_3)
				nskill = SkillConstant.BATTLE_ROLE_BEHUNTER;
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_1||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_1||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_1)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_1;
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_2||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_2||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_2)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_2;
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_3||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_3||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_3)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_3;			
			else if(decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_1_4||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_2_4||decision.getOperateid() == SkillConstant.BATTLE_HUOBAN_HUNTER_3_4)
				nskill = SkillConstant.BATTLE_HUOBAN_BEHUNTER_4;			
			if(nskill != 0 && !fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_BEHUNTER))
			{
				decision.setOperateid(nskill);				
				decision.setAim(fighter.getFighterId());				
			}
		}	
		
		switch (decision.getOperatetype())
		{
		case OperationType.ACTION_ATTACK:// 技能内选目标
			return new Attack(battle, decision.getOperator(), decision.getAim());

		case OperationType.ACTION_DEFEND:
			return new Defend(battle, decision.getOperator());

		case OperationType.ACTION_ESCAPE_INSTANT:
			return new InstantRunaway(battle, decision.getOperator(), action.getSummonMaps());

		case OperationType.ACTION_ESCAPE:
			return new Runaway(battle, decision.getOperator(), action.getEscOdds());

		case OperationType.ACTION_PROTECT:// 可能需要选目标
			if (decision.getAim() == 0)// 如果是0（没有目标）则随机一个友方
				decision.setAim(getRandLiveFriend(battle, fighter.getFighterId()));
			new PAddProtector(battle.getBattleid(), decision.getOperator(), decision.getAim());
			return new Protect(battle, decision.getOperator(), decision.getAim());

		case OperationType.ACTION_SUMMON:// AI怪的特殊召唤
			return new SummonMonster(battle, decision.getOperator(), action.getSummonMaps(), false);

		case OperationType.ACTION_SUMMON_INSTANT:// AI怪的瞬时召唤，如此召唤时AI怪没有起手
			return new SummonMonster(battle, decision.getOperator(), action.getSummonMaps(), true);

		case OperationType.ACTION_SKILL:// 技能内选目标
			int skilllevel = (int) (action.getCountFactor() * fighter.getEffectRole().getLevel() + action.getCountConst());

			return new UseSkill(battle, decision.getOperator(), decision.getAim(), decision.getOperateid(), skilllevel);

		case OperationType.ACTION_WITHDRAW:// AI怪召回其他怪物
			return new SummonBack(battle, decision.getOperator(), action);

		case OperationType.ACTION_REST:
			return new Rest(battle, decision.getOperator());
		case OperationType.ACTION_BATTLE_END:
			return new EndBattle(battle, decision.getOperator(), decision.getOperateid());
		case OperationType.ACTION_ENVIRONMENTCHANGE:
			return new ChangeEnvironment(battle, decision.getOperator(), decision.getOperateid());
		}
		return null;
	}

	/**检测战斗是否可以结束*/
	public static int checkBattleEnd(final xbean.BattleInfo battle)
	{
		
		int hostNum = 0;
		int guestNum = 0;
		int hostroledienum = 0;
		int hostrunwaynum = 0;
		int guestrunwaynum = 0;
		
		int hostroletotal = 0;
		int guestroletotal = 0;
		for (xbean.Fighter f : battle.getFighters().values())
		{
			if(battle.getFighterobjects().get(f.getBattleindex()).getBuffAgent().existBuff(BuffConstant.CONTINUAL_TOTEM_DISMISS))
				continue;
			if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				if (f.getIshost())
				{
					hostroletotal ++;
				}
				else
				{
					guestroletotal ++;
				}
			}
			
			int ret = checkBattleEnd(battle, f.getBattleindex());
			if (ret == 0)
			{
				if (f.getIshost())
					hostNum++;
				else
					guestNum++;
			}
			if(f.getFightertype() == xbean.Fighter.FIGHTER_ROLE||f.getFightertype() == xbean.Fighter.FIGHTER_PET)
			{
				ret = checkBattleEnd(battle, f.getBattleindex());
				if(ret != 0)
				{
					if ((ret & ResultType.RESULT_RUNAWAY) != 0)
					{
						if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
						{
							if (f.getIshost())
								hostrunwaynum ++;
							else
								guestrunwaynum ++;
						}
					}
					
					if(f.getIshost())
						hostroledienum++;
				}
			}
		}
		int victoryType = 1;
		
		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battle.getConfigid());
		if(battleConfig!=null&&battleConfig.victoryconditionType > 0){
			victoryType = battleConfig.victoryconditionType;
		}
		
		switch(victoryType)
		{ // 只有pve时候会有这种状态。
		case BATTLE_KILLALL://清除敌方全部角色——战斗胜利
			{
				if (0 == hostNum)
					return BATTLE_LOSE;
				if (0 == guestNum)
					return BATTLE_WIN;
				
				if (hostrunwaynum == battle.getRoleids().size())
				{
					return BATTLE_LOSE;
				}
			}
			break;			
		case BATTLE_ALL://必败战斗——进入战斗即胜利
			{
				if (0 == hostNum||0 == guestNum)
					return BATTLE_WIN;
			}
			break;			
		case BATTLE_LEADER_SURVIVAL://清除敌方全部角色，并且队长存活——战斗胜利
			{
				if (0 == hostNum)
					return BATTLE_LOSE;
				if (0 == guestNum)
				{ 
					int ret = checkBattleEnd(battle, 1);
					if(ret == 0)
						return BATTLE_WIN;
					else
						return BATTLE_LOSE;
				}
			}
			break;			
		case BATTLE_ALL_SURVIVAL://清除敌方全部角色，并且全部角色存活战斗胜利
			{
				if (0 == hostNum)
					return BATTLE_LOSE;
				if (0 == guestNum)
				{ 
					if(hostroledienum > 0)
						return BATTLE_LOSE;
					else
						return BATTLE_WIN;
				}
			}
			break;			
		}

		// 这段代码（下面4行）其实是冗余的。主要防止策划填表错误。
		if (0 == hostNum)
			return BATTLE_LOSE;
		if (0 == guestNum)
			return BATTLE_WIN;

		boolean ispvp = isPvP(battle);
		if(ispvp)
		{
			if(hostNum == 0  || hostrunwaynum == hostroletotal)
			{
				if(guestNum == 0 || guestrunwaynum == guestroletotal)//两边都无角色
					return ispvp ? BATTLE_DRAW : BATTLE_LOSE;
				else//主方无角色，客方有角色
					return BATTLE_LOSE;
			}
			else
			{
				if(guestNum == 0 || guestrunwaynum == guestroletotal)//主方有角色，客方无角色
					return ispvp ? BATTLE_WIN : BATTLE_NOT_END;
				else//主客方都有角色
					return BATTLE_NOT_END;
			}		
		}
		else
			return BATTLE_NOT_END;
	}

	public static boolean isPvP(xbean.BattleInfo battle)
	{
		return battle.getBattletype() % 10 == 1;
	}
	
	/**检测一个战斗单位的战斗是否可以结束*/
	public static int checkBattleEnd(final xbean.BattleInfo battle, final int fighterId)
	{
		if (!battle.getFighters().containsKey(fighterId))
			return ResultType.RESULT_RUNAWAY;

		final Integer val = battle.getRoundresult().get(fighterId);
		if (null == val)
			return 0;

		if ((val & ResultType.RESULT_RUNAWAY) != 0)
			return val;

		else if ((val & ResultType.RESULT_SUMMONBACK) != 0)
			return val;

		else if ((val & ResultType.RESULT_SEIZE) != 0)
			return val;

		else if ((val & ResultType.RESULT_DEATH) != 0)
			return val;

		else if ((val & ResultType.RESULT_KICKOUT) != 0)
			return val;

		else if ((val & ResultType.RESULT_GHOST) != 0)
			return val;
		return 0;

	}

	/**对战斗单元根据速度进行排序,加入乱敏规则*/
	public static void sort(final java.util.List<xbean.Fighter> fighterList, final xbean.BattleInfo battle)
	{
		Collections.sort(fighterList, new Comparator<xbean.Fighter>()
		{
			public int compare(xbean.Fighter i, xbean.Fighter o)
			{
				Fighter fi = battle.getFighterobjects().get(i.getBattleindex());
				Fighter fo = battle.getFighterobjects().get(o.getBattleindex());
				int fiSpeed = fi.getSeqSpeed();
				int foSpeed = fo.getSeqSpeed();
				if (fiSpeed > foSpeed)
					return -1;
				if (fiSpeed < foSpeed)
					return 1;
				return 0;
			}

		});
	}
	
	/**对战斗单元根据速度进行排序,加入乱敏规则*/
	public static void sortfighter(final java.util.List<Fighter> fighterList, final xbean.BattleInfo battle)
	{
		Collections.sort(fighterList, new Comparator<Fighter>()
		{
			public int compare(Fighter i, Fighter o)
			{
				int fiSpeed = i.getSeqSpeed();
				int foSpeed = o.getSeqSpeed();
				if (fiSpeed > foSpeed)
					return -1;
				if (fiSpeed < foSpeed)
					return 1;
				return 0;
			}
		});
	}
	
	/**对战斗单元根据速度进行排序,加入乱敏规则*/
	public static void sortAIOperation(java.util.List<AiOperate> clientActions)
	{
		Collections.sort(clientActions, new Comparator<AiOperate>()
		{
			public int compare(AiOperate i, AiOperate o)
			{
				Fighter fi = i.getFighter();
				Fighter fo = o.getFighter();
				int fiSpeed = fi.getSeqSpeed();
				int foSpeed = fo.getSeqSpeed();
				if (fiSpeed > foSpeed)
					return -1;
				if (fiSpeed < foSpeed)
					return 1;
				return 0;
			}

		});
	}

	
	/**计算一个玩家的临时速度,如果已经计算过了,则直接返回.临时速度数据存储在battleinfo的FighterObject中*/
	public static int calTempSpeed(Fighter fi)
	{

		// 如果临时速度已经算过了,则直接返回
		int speed = fi.getEffectRole().getSpeed();
		double finalSpeed = 0.0;
		double randomFloat = 0.0;
		if (speed * 0.1 < 100)
		{
			randomFloat = mkdb.Mkdb.random().nextFloat();
			finalSpeed = speed * 0.9 + speed * 0.1 * randomFloat;
		} else
		{
			randomFloat = mkdb.Mkdb.random().nextFloat();
			finalSpeed = speed * 0.9 + 100 * randomFloat;
		}
		fi.setTempSpeed((int) (finalSpeed - speed));
		if (logger.isDebugEnabled()) {logger.debug(new StringBuffer().append(fi.getFighterBean().getFightername()).append(" index:").append(fi.getFighterId()).append(" speed:").append(speed).append(" random:").append(randomFloat)
				.append(" finalSpeed:").append(finalSpeed).toString());}
		return fi.getTempSpeed();
	}

	/**检测本回合末战斗者是否还活着*/
	public static boolean checkLiveInBattle(final xbean.BattleInfo battle, final int index)
	{
		if (!battle.getFighters().containsKey(index))
			return false;

		final Integer val = battle.getRoundresult().get(index);
		if (null == val)
			return true;

		if ((val & ResultType.RESULT_RUNAWAY) != 0)
			return false;

		else if ((val & ResultType.RESULT_SUMMONBACK) != 0)
			return false;

		else if ((val & ResultType.RESULT_SEIZE) != 0)
			return false;

		else if ((val & ResultType.RESULT_DEATH) != 0)
			return false;

		else if ((val & ResultType.RESULT_KICKOUT) != 0)
			return false;

		else if ((val & ResultType.RESULT_GHOST) != 0)
			return false;

		return true;
	}

	public static boolean checkRunAway(final xbean.BattleInfo battle, final int index)
	{

		if (!battle.getFighters().containsKey(index))
			return true;

		final Integer val = battle.getRoundresult().get(index);

		if ((val & ResultType.RESULT_RUNAWAY) != 0)
			return true;

		return false;
	}

	/**获取指定对象的保护者ids*/
	public static java.util.List<Integer> getProtectors(xbean.BattleInfo battle, int fighterId)
	{

		final xbean.Protectors ps = battle.getProtectormap().get(fighterId);
		return (null == ps) ? new java.util.LinkedList<Integer>() : ps.getProtectorlist();
	}

	/**获取一个指定对象的保护者*/
	public static int getAProtector(xbean.BattleInfo battle, int fighterId, int hp)
	{
		final xbean.Protectors ps = battle.getProtectormap().get(fighterId);
		Integer protector = 0;
		float protectrate = PropConf.Battle.AUTO_PROTECT_RATE;		
		if (ps == null || ps.getProtectorlist().size() == 0)
		{//被动保护
			Fighter fighter = battle.getFighterobjects().get(fighterId);
			for (int friend : BattleField.getFriends(battle, fighterId))
			{
				Fighter protectorfighter = battle.getFighterobjects().get(friend);
				if (protectorfighter != null)
				{
					if (!protectorfighter.canAction(OperationType.ACTION_PROTECT, 0))
						continue;// 能不能保护
					if (protectorfighter.getFighterId() == fighterId)
						continue;// 不能自己保护自己
					int protectorhp = protectorfighter.getEffectRole().getHp();
					if ( protectorhp <= Math.abs(hp) || protectorhp < protectorfighter.getEffectRole().getMaxHp()*0.2f)
						continue;// 血少不能保护
					
					//每个组合被动保护概率=默认自动保护率+好友度加成自动保护率
					float extprotectrate = 0.0f;
					if(fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE && protectorfighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE )
					{
						int friendpoint = fire.pb.friends.FriendHelper.getFriendlyDegrees(
								fighter.getFighterBean().getUniqueid(), protectorfighter.getFighterBean().getUniqueid());
						if (friendpoint > 0)
							extprotectrate = 0.008f + 0.000004f*friendpoint;
					}
					if(protectrate + extprotectrate> Math.random())
					{
						protector = protectorfighter.getFighterId();
						break;
					}
				}
			}
		}
		else
		{//主动保护
			for (int index : ps.getProtectorlist())
			{
				if (!BattleField.checkOutBattle(battle, index))
				{
					if (index == fighterId)
						continue;// 不能自己保护自己
					// 验证战斗者处于可行动状态
					Fighter profighter = battle.getFighterobjects().get(index);
					if (profighter.canAction(OperationType.ACTION_PROTECT, 0))
					{
						protector = index;
						break;
					}
				}
			}
			ps.getProtectorlist().remove(protector);// 只能保护一次，就移除
		}
		return protector;
	}

	/**通过战斗index获取roleID*/
	public static long getRoleIDByIndex(final xbean.BattleInfo battle, final int index)
	{

		final xbean.Fighter f = battle.getFighters().get(index);
		if (null == f)
			return 0;

		if (f.getFightertype() != xbean.Fighter.FIGHTER_ROLE)
			return 0;

		return f.getUniqueid();
	}

	/**生成发给客户端的FighterInfo */
	public static FighterInfo getFighterInfo(final xbean.Fighter xfighter, xbean.BattleInfo battle)
	{

		final FighterInfo fi = new FighterInfo();
		Fighter fighter = battle.getFighterobjects().get(xfighter.getBattleindex());
		fi.fightername = xfighter.getFightername();
		fi.fightertype = xfighter.getFightertype();
		fi.dataid = xfighter.getUniqueid();
		fi.index = xfighter.getBattleindex();
		fi.hp = fighter.getEffectRole().getHp();
		fi.ep = xfighter.getInitattrs().get(AttrType.EFFECT_POINT).intValue();
		fi.maxhp = fighter.getEffectRole().getMaxHp();
		fi.uplimithp = fighter.getEffectRole().getUplimithp();
		fi.subtype = xfighter.getSubtype();
		fi.footlogoid=xfighter.getFootlogoid();
		fi.petkeys.addAll(xfighter.getFightedpets().keySet());
				
		for (Map.Entry<Integer, xbean.Buff> buff : fighter.getBuffAgent().getAllBuffBeans().entrySet())
		{
			ConstantlyBuffConfig conf = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buff.getKey());
			if (conf.isSnedWithScript())
				fi.buffs.put(buff.getKey(), buff.getValue().getRound());
		}
		// 对不同类型的战斗者特殊处理
		if (xfighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			fire.pb.map.Role.getPlayerComponents(xfighter.getUniqueid(), fi.components);
			fi.components.put((byte)SpriteComponents.SPRITE_HORSEDRESS, 0);			
			fi.shape = fire.pb.mission.UtilHelper.getRoleShapeID(xfighter.getUniqueid());
			if(fire.pb.mission.UtilHelper.getTransformid(xfighter.getUniqueid())!=0 && 
					fire.pb.mission.UtilHelper.getTransformid(xfighter.getUniqueid())!= fi.shape)
			{
				fi.components.put((byte)SpriteComponents.ROLE_COLOR1, 0);
				fi.components.put((byte)SpriteComponents.ROLE_COLOR2, 0);
			}
		} 
		else if (xfighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
		{
			xbean.Monster xmonster = battle.getMonsters().get(xfighter.getBattleindex());
			if (xmonster != null)
			{
				
				
				fi.shape = xmonster.getShape();
				fi.uplimithp = xmonster.getLevel();
				//这里处理一下npc怪的武器造型
				if(xfighter.getFightertype() == xbean.Fighter.FIGHTER_MONSTER_NPC||xfighter.getFightertype() == xbean.Fighter.FIGHTER_SYSTEM_PARTNER
						||xfighter.getFightertype() == xbean.Fighter.FIGHTER_PARTNER){
					java.util.TreeMap<Integer,SNpcShape> snpcshape=ConfigManager.getInstance().getConf(SNpcShape.class);
					if(snpcshape!=null){
						SNpcShape sNpcShape=snpcshape.get(fi.shape);
						if(sNpcShape!=null&&sNpcShape.getShowWeaponId()>0){
							fi.components.put((byte)SpriteComponents.SPRITE_WEAPON, sNpcShape.getShowWeaponId());
						}
					}
				}
				if (battle.getBattletype() == BattleType.BATTLE_SHOUXI)
				{// 首席弟子的怪造型要跟玩家一模一样
					int monsterid = xmonster.getMonsterid();
					School school = School.getSchoolByBattleNpc(monsterid);
					if (school != null)
					{
						xbean.ProfessionLeaderInfo sxi = xtable.Professionleader.select(school.getValue());
						if (sxi != null){
							Integer shape=xtable.Properties.selectShape(sxi.getRoleid());
							if(shape!=null && shape>0){
								fi.shape = shape;
							}
							for(Entry<Integer, Integer> entry:sxi.getShapecomponent().entrySet()){
								if(entry!=null){
									int type=entry.getKey();
									int value=entry.getValue();
									fi.components.put((byte) type, value);
								}
							}
							fi.components.put((byte)SpriteComponents.SPRITE_HORSEDRESS, 0);
						}
					}
				}
				//处理冠军试炼变形
				if(WinnerManager.isWinnerFight(battle.getConfigid())){
					if(xfighter.getBattleindex()==20){
						//并且位置为20
						School school = School.getSchoolByFighterConfigWinner(battle.getConfigid());
						if (school != null)
						{
							xbean.ProfessionLeaderInfo sxi = xtable.Professionleader.select(school.getValue());
							if (sxi != null){
								Integer shape=xtable.Properties.selectShape(sxi.getRoleid());
								if(shape!=null && shape>0){
									fi.shape = shape;
								}
								for(Entry<Integer, Integer> entry:sxi.getShapecomponent().entrySet()){
									if(entry!=null){
										int type=entry.getKey();
										int value=entry.getValue();
										fi.components.put((byte) type, value);
									}
								}
								fi.components.put((byte)SpriteComponents.SPRITE_HORSEDRESS, 0);
							}
						}
					}
				}
			}
		} else if (xfighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
		{
			Fighter hostfighter = battle.getFighterobjects().get(fighter.getFighterId() - 5);
			if (hostfighter == null)
				return fi;
			long roleid = hostfighter.getFighterBean().getUniqueid();
			PetColumn petcol = new PetColumn(roleid, PetColumnTypes.PET, true);
			PropRole prole = new PropRole(roleid, true);
			Pet pet = petcol.getPet(prole.getFightpetkey());
			if (pet == null)
				return fi;
			xbean.PetInfo petinfo=pet.getPetInfo();
			if(petinfo!=null){
				if(petinfo.getPetdye1()>0){
					fi.components.put((byte)SpriteComponents.ROLE_COLOR1, petinfo.getPetdye1());
					fi.components.put((byte)SpriteComponents.ROLE_COLOR2, petinfo.getPetdye2());
				}
			}
		}
		if(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_SHAPECHANGE))
		{
			xbean.Buff buff = fighter.getBuffAgent().getBuffBean(BuffConstant.CONTINUAL_SHAPECHANGE);				
			fi.shape = buff.getEffects().get(EffectType.SHAPE_ID).intValue();
		}
		return fi;
	}

	/**发给自己的属性和BUFF变化，*/
	public static void sendRoundChangeResult(xbean.BattleInfo battle)
	{
		for (Fighter fighter : battle.getFighterobjects().values())
		{
			fighter.getBuffAgent().psendSBuffChangeResult(fighter.getRoundBuffResult());
			fighter.getRoundBuffResult().clear();
		}
	}

	/**获得观战者的信息*/
	public static FighterInfo getWatcherFighterInfo(xbean.Fighter f, xbean.BattleInfo battle)
	{
		// Fighter fighter = battle.getFighterobjects().get(f.getBattleindex());
		final FighterInfo fi = new FighterInfo();
		fi.fightername = f.getFightername();
		fi.fightertype = f.getFightertype();
		fi.dataid = f.getUniqueid();
		fi.index = f.getBattleindex();

		fi.hp = f.getInihp();
		fi.ep = 0; 
		fi.maxhp = f.getInihp();
		fi.uplimithp = f.getInihp();
		fi.footlogoid=f.getFootlogoid();

		// 对不同类型的战斗者特殊处理
		if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			xbean.Properties prop = xtable.Properties.select(f.getUniqueid());
			fi.shape = prop.getShape();
			fire.pb.map.Role.getPlayerComponents(f.getUniqueid(), fi.components);
			fi.components.put((byte)SpriteComponents.SPRITE_HORSEDRESS, 0);
		} else if (f.getFightertype() == xbean.Fighter.FIGHTER_PET)
		{
			Fighter fighter = battle.getFighterobjects().get(f.getBattleindex());
			Fighter hostfighter = battle.getFighterobjects().get(fighter.getFighterId() - 5);
			if (hostfighter == null)
				return fi;
			long roleid = hostfighter.getFighterBean().getUniqueid();
			PetColumn petcol = new PetColumn(roleid, PetColumnTypes.PET, true);
			PropRole prole = new PropRole(roleid, true);
			Pet pet = petcol.getPet(prole.getFightpetkey());
			if (pet == null)
				return fi;
		}
		return fi;
	}

	/**返回当前还在战斗场中的FighterId（包括死亡和鬼魂中的） 注意，此方法用来判断收到的指令数*/
	public static List<Integer> getFighterIdsInBattle(BattleInfo battle)
	{

		List<Integer> liveRoles = new ArrayList<Integer>();
		for (int i : battle.getRoundresult().keySet())
		{
			if (!BattleField.checkOutBattle(battle, i))
			{
				xbean.Fighter f = battle.getFighters().get(i);
				liveRoles.add(f.getBattleindex());
			}
		}
		return liveRoles;
	}

	/**getHostFighters:得到所有host方队员的fighters,包括死亡的,但不包括逃跑的*/
	public static List<xbean.Fighter> getHostFighters(BattleInfo battle)
	{

		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		Collection<xbean.Fighter> allFighters = battle.getFighters().values();
		for (xbean.Fighter fighter : allFighters)
		{
			if (fighter.getIshost() && fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				fighters.add(fighter);
			}
		}
		return fighters;
	}

	/**getHostFighters:得到所有guest方队员的fighters,包括死亡的,但不包括逃跑的*/
	public static List<xbean.Fighter> getGuestFighters(BattleInfo battle)
	{

		List<xbean.Fighter> fighters = new ArrayList<xbean.Fighter>();
		Collection<xbean.Fighter> allFighters = battle.getFighters().values();
		for (xbean.Fighter fighter : allFighters)
		{
			if (!fighter.getIshost() && fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				fighters.add(fighter);
			}
		}
		return fighters;
	}

	/**返回当前还在战斗场中的玩家的FighterId（包括死亡的玩家）*/
	public static List<Integer> getRoleFigherIdsInBattle(BattleInfo battle)
	{

		List<Integer> liveRoles = new ArrayList<Integer>();
		for (int i : battle.getRoundresult().keySet())
		{
			if (!BattleField.checkOutBattle(battle, i))
			{
				xbean.Fighter f = battle.getFighters().get(i);
				if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
					liveRoles.add(f.getBattleindex());
			}
		}
		return liveRoles;
	}

	/**返回当前还在战斗场中的玩家的RoleId（包括死亡的玩家）*/
	public static List<Long> getRoleIdsInBattle(BattleInfo battle)
	{

		List<Long> liveRoles = new ArrayList<Long>();
		for (int i : battle.getRoundresult().keySet())
		{
			if (!BattleField.checkOutBattle(battle, i))
			{
				xbean.Fighter f = battle.getFighters().get(i);
				if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
					liveRoles.add(f.getUniqueid());
			}
		}
		return liveRoles;
	}

	/**检测战斗者是否已经出战斗,死亡/鬼魂不算出战斗*/
	public static boolean checkOutBattle(final xbean.BattleInfo battle, final int index)
	{

		if (!battle.getFighters().containsKey(index))
			return true;

		final Integer val = battle.getRoundresult().get(index);
		if (null == val)
			return false;

		if ((val & ResultType.RESULT_RUNAWAY) != 0)
			return true;

		else if ((val & ResultType.RESULT_SUMMONBACK) != 0)
			return true;

		else if ((val & ResultType.RESULT_SEIZE) != 0)
			return true;

		else if ((val & ResultType.RESULT_KICKOUT) != 0)
			return true;

		return false;
	}
	
	public static void removeDeathFighterHaloBuffs(final xbean.BattleInfo battle, DemoResult demoresult, int index)
	{			
		//List<DemoResult> extdemo =  new ArrayList<DemoResult>();
		Fighter fighter = battle.getFighterobjects().get(index);
		if (battle.getBattleresult() == BattleField.BATTLE_NOT_END)
		{
			SkillAgent skillagent = fighter.getSkillAgent();
			if(skillagent != null)
			{
				for(int skillId : skillagent.getAllBattleSkills())
				{
					FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
					if(sconf == null)
					{
						if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("怪物 "+fighter.getFighterBean().getFightername()+" FightSkillConfig为空，技能ID = " + skillId, new NullPointerException());}
						continue;
					}
					if(!sconf.isActiveSkill() && (sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO||sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2) 
							&& sconf.getSubSkills()[0]!=null && sconf.getSubSkills()[0].getBuffUnits()[0]!=null)
					{//光环技能，且有buff(一定是ConstantlyBuff)
						BuffUnit buffarg = sconf.getSubSkills()[0].getBuffUnits()[0];
						if(buffarg==null) continue;
						
						if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO)
						{
							for (int friend : BattleField.getFriends(battle, fighter.getFighterId()))
							{
								Fighter aimf = battle.getFighterobjects().get(friend);
								if (aimf != null)
								{
									Result result = aimf.getBuffAgent().removeFighterCBuff(buffarg.buffIndex, index);
									if (result.isSuccess())
									{
										fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, aimf);
									}
								}
							}
						}
						else if(sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_HALO2)
						{
							List<Integer> poss = fire.pb.battle.Module.getInstance().getEffectPoss(
									FormationConfig.GetPosbyFighteid(fighter.getFighterId()<15? battle.getHostform():battle.getGuestform(),fighter.getFighterId()));
							poss = FormationConfig.GetPosbyFighteids(fighter.getFighterId()<15? battle.getHostform():battle.getGuestform(),poss,fighter.getFighterId()<15);
							if(poss != null)
							{
								for (int friend : poss)
								{
									Fighter aimf = battle.getFighterobjects().get(friend);
									if (aimf != null)
									{
										Result result = aimf.getBuffAgent().removeFighterCBuff(buffarg.buffIndex, index);
										if (result.isSuccess())
										{
											fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, aimf);
										}
									}
								}
							}
						}
						
					}
				}
			}
		}
	}

	/**判断如果战斗单元血量变为 finalHp的时候，是否由20%以上降为20%以下,如果是，则发送宠物闲话*/
	public static boolean isHpDown20per(final xbean.BattleInfo battle, final int index, int finalHp)
	{

		final xbean.Fighter fighter = battle.getFighters().get(index);

		if (fighter == null || finalHp < 0)
			return false;
		boolean before = true;
		boolean after = true;
		if (fighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			fire.pb.battle.Fighter fo = battle.getFighterobjects().get(fighter.getBattleindex());
			fire.pb.effect.Role roleImpl = fo.getEffectRole();
			before = roleImpl.getHp() * 100 / roleImpl.getMaxHp() > 20;
			after = finalHp * 100 / roleImpl.getMaxHp() <= 20;
			if (before && after)
			{
				if (battle.getFighters().get(fighter.getBattleindex() + 5) == null)
					return false;
				fire.pb.pet.PetChat.randomPetChatAndSend(battle, fighter.getBattleindex() + 5, false);
			} else
				return false;
		}
		if (fighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
		{
			final xbean.Fighter masterRole = battle.getFighters().get(index - 5);
			if (masterRole == null)
				return false;
			fire.pb.PropRole propRole = new fire.pb.PropRole(masterRole.getUniqueid(), true);
			fire.pb.effect.Role petImpl = new fire.pb.effect.PetImpl(masterRole.getUniqueid(), propRole.getFightpetkey());
			before = petImpl.getHp() * 100 / petImpl.getMaxHp() > 20;
			after = finalHp * 100 / petImpl.getMaxHp() <= 20;
			if (before && after)
			{
				fire.pb.pet.PetChat.randomPetChatAndSend(battle, fighter.getBattleindex(), true);
			} else
				return false;
		}
		return false;
	}

	/**玩家回血回魔*/
	public static void fullRoleHpMp(long roleid)
	{

		Role role = new RoleImpl(roleid);

		Map<Integer, Float> result = role.fullHpAndRecoverWound();
		role.fullMp();
		int maxmp = role.getMaxMp();
		SRefreshRoleData sr = new SRefreshRoleData();
		sr.datas.putAll(result);
		sr.datas.put(AttrType.MP, (float) maxmp);
		Procedure.psendWhileCommit(roleid, sr);
	}

	/**超时检测的取消*/
	public static void cancelTimeOutCheck(final xbean.BattleInfo battle)
	{

		if (null != battle.getTimeout())
		{
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("取消反馈超时检测 ");};
			battle.getTimeout().cancel(true);
			battle.setTimeout(null);
		}
	}

	public static List<Monster> genMonsters(final List<Integer> monsterIDs)
	{
		final List<Monster> monsters = new ArrayList<Monster>();
		for (int monsterid : monsterIDs)
		{
			Monster monster = new Monster(monsterid);
			monsters.add(monster);
		}
		return monsters;
	}

	/**获取目标对于自己的类型，只应用于道具的目标类型 */
	public static int getItemAimRelation(xbean.BattleInfo battle, int operator, int aim)
	{		xbean.Fighter aimf = battle.getFighters().get(aim);
		xbean.Fighter opf = battle.getFighters().get(operator);
		if (aimf == null)
			return 0;
		if (opf == null)
			return 0;
		// 宠物使用道具时的特殊处理
		if (opf.getFightertype() == xbean.Fighter.FIGHTER_PET)
		{
			if (aimf.getFightertype() == xbean.Fighter.FIGHTER_PET)
			{
				if (opf.getIshost() == aimf.getIshost())
				{
					if (operator == aim)
						return BattleAimRelation.SELF_PET;
					else
						return BattleAimRelation.FRIEND_PET;
				}
				else
					return BattleAimRelation.ENERMY_PET;
			} else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				if (opf.getIshost() == aimf.getIshost())
				{
					if (operator - aim == 5)
						return BattleAimRelation.SELF;// 道具目标类型为自己的角色
					else
						return BattleAimRelation.FRIEND_ROLE;
				}
				else
					return BattleAimRelation.ENERMY_ROLE;
			}
		}
		if (operator == aim)
			return BattleAimRelation.SELF;// 自己
		if (aimf.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			if (opf.getIshost() == aimf.getIshost())
				return BattleAimRelation.FRIEND_ROLE;// 友方人物
			else
				// 敌方人物
				return BattleAimRelation.ENERMY_ROLE;
		} else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_PET)
		{
			if (opf.getIshost() == aimf.getIshost())
			{// 己方宠物
				if ((operator + 5) == aim)
					return BattleAimRelation.SELF_PET;
				else
					return BattleAimRelation.FRIEND_PET;
			} else
			{// 敌方宠物
				return BattleAimRelation.ENERMY_PET;
			}
		} else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_MONSTER_HIDE||aimf.getFightertype() == xbean.Fighter.FIGHTER_MONSTER_NPC)
		{
			if (opf.getIshost() == aimf.getIshost())
				return BattleAimRelation.FRIEND_MONSTER;// 友方怪物
			else
				return BattleAimRelation.ENERMY_MONSTER;// 敌方怪物
		} else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_PARTNER||aimf.getFightertype() == xbean.Fighter.FIGHTER_SYSTEM_PARTNER)
		{
			if (opf.getIshost() == aimf.getIshost())
				return BattleAimRelation.FRIEND_NPC;// 友方NPC
			else
				return BattleAimRelation.ENERMY_NPC;// 敌方NPC
		}
		else
			return 0;
	}

	/**获取目标对于自己的类型*/
	public static int getAimRelation(xbean.BattleInfo battle, int operator, int aim)
	{

		xbean.Fighter aimf = battle.getFighters().get(aim);
		xbean.Fighter opf = battle.getFighters().get(operator);
		if (aimf == null)
			return 0;
		if (opf == null)
			return 0;
		if (operator == aim)
			return BattleAimRelation.SELF;// 自己
		else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
		{
			if (opf.getIshost() == aimf.getIshost())
				return BattleAimRelation.FRIEND_ROLE;// 友方人物
			else
				// 敌方人物
				return BattleAimRelation.ENERMY_ROLE;
		} else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_PET)
		{
			if (opf.getIshost() == aimf.getIshost())
			{// 己方宠物
				if ((operator + 5) == aim)
					return BattleAimRelation.SELF_PET;
				else
					return BattleAimRelation.FRIEND_PET;
			} else
			{// 敌方宠物
				return BattleAimRelation.ENERMY_PET;
			}
		} else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_MONSTER_HIDE||aimf.getFightertype() == xbean.Fighter.FIGHTER_MONSTER_NPC)
		{
			if (opf.getIshost() == aimf.getIshost())
				return BattleAimRelation.FRIEND_MONSTER;// 友方怪物
			else
				return BattleAimRelation.ENERMY_MONSTER;// 敌方怪物
		} else if (aimf.getFightertype() == xbean.Fighter.FIGHTER_PARTNER||aimf.getFightertype() == xbean.Fighter.FIGHTER_SYSTEM_PARTNER)
		{
			if (opf.getIshost() == aimf.getIshost())
				return BattleAimRelation.FRIEND_NPC;// 友方NPC
			else
				return BattleAimRelation.ENERMY_NPC;// 敌方NPC
		}
		else
			return 0;
	}

	/**战斗者在战斗中阵营内的相对位置 战斗矩阵为5*4*/
	public static class Position
	{
		public Position(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public final int x;

		public final int y;
	}

	/**获取站在左边的战斗者*/
	public static final int getLeftFighterId(xbean.BattleInfo battle, int fighterId)
	{

		xbean.Fighter fighterbean = battle.getFighters().get(fighterId);
		if (fighterbean == null)
			return 0;
		if (fighterbean.getPositionx() == 1)
			return 0;
		if (fighterbean.getPositionx() == 2 && fighterbean.getPositiony() == 3)
			return 0;
		if (fighterbean.getPositionx() == 3 && fighterbean.getPositiony() == 4)
			return 0;
		for (Map.Entry<Integer, xbean.Fighter> entry : battle.getFighters().entrySet())
		{
			if (entry.getValue().getIshost() == fighterbean.getIshost() && entry.getValue().getPositiony() == fighterbean.getPositiony()
					&& (entry.getValue().getPositionx() + 1) == fighterbean.getPositionx())
			{
				return entry.getKey();
			}
		}
		return 0;
	}

	/**获取站在右边的战斗者*/
	public static final int getRightFighterId(xbean.BattleInfo battle, int fighterId)
	{

		xbean.Fighter fighterbean = battle.getFighters().get(fighterId);
		if (fighterbean == null)
			return 0;
		if (fighterbean.getPositionx() == 5)
			return 0;
		if (fighterbean.getPositionx() == 4 && fighterbean.getPositiony() == 3)
			return 0;
		if (fighterbean.getPositionx() == 3 && fighterbean.getPositiony() == 4)
			return 0;
		for (Map.Entry<Integer, xbean.Fighter> entry : battle.getFighters().entrySet())
		{
			if (entry.getValue().getIshost() == fighterbean.getIshost() && entry.getValue().getPositiony() == fighterbean.getPositiony()
					&& (entry.getValue().getPositionx() - 1) == fighterbean.getPositionx())
			{
				return entry.getKey();
			}
		}
		return 0;
	}

	/**获取站在前边的战斗者*/
	public static final int getFrontFighterId(xbean.BattleInfo battle, int fighterId)
	{
		xbean.Fighter fighterbean = battle.getFighters().get(fighterId);
		if (fighterbean == null)
			return 0;
		if (fighterbean.getPositiony() == 1)
			return 0;
		for (Map.Entry<Integer, xbean.Fighter> entry : battle.getFighters().entrySet())
		{
			if (entry.getValue().getIshost() == fighterbean.getIshost() && entry.getValue().getPositionx() == fighterbean.getPositionx()
					&& (entry.getValue().getPositiony() + 1) == fighterbean.getPositiony())
			{
				return entry.getKey();
			}
		}
		return 0;
	}

	/**获取站在后边的战斗者*/
	public static final int getBehindFighterId(xbean.BattleInfo battle, int fighterId)
	{

		xbean.Fighter fighterbean = battle.getFighters().get(fighterId);
		if (fighterbean == null)
			return 0;
		if (fighterbean.getPositiony() == 4)
			return 0;
		for (Map.Entry<Integer, xbean.Fighter> entry : battle.getFighters().entrySet())
		{
			if (entry.getValue().getIshost() == fighterbean.getIshost() && entry.getValue().getPositionx() == fighterbean.getPositionx()
					&& (entry.getValue().getPositiony() - 1) == fighterbean.getPositiony())
			{
				return entry.getKey();
			}
		}
		return 0;
	}

	/**获取站在同一横排的其他战斗者*/
	public static final List<Integer> getLineFighterId(xbean.BattleInfo battle, int fighterId)
	{

		List<Integer> ids = new ArrayList<Integer>();
		xbean.Fighter fighterbean = battle.getFighters().get(fighterId);
		if (fighterbean == null)
			return ids;
		if (fighterbean.getPositiony() == 4)
			return ids;
		for (Map.Entry<Integer, xbean.Fighter> entry : battle.getFighters().entrySet())
		{
			if (entry.getValue().getIshost() == fighterbean.getIshost() && entry.getValue().getPositiony() == fighterbean.getPositiony() && entry.getValue().getBattleindex() != fighterId)
				ids.add(entry.getValue().getBattleindex());
		}
		return ids;
	}

	/**获取站在同一竖排的其他战斗者*/
	public static final List<Integer> getRowFighterId(xbean.BattleInfo battle, int fighterId)
	{

		List<Integer> ids = new ArrayList<Integer>();
		xbean.Fighter fighterbean = battle.getFighters().get(fighterId);
		if (fighterbean == null)
			return ids;
		for (Map.Entry<Integer, xbean.Fighter> entry : battle.getFighters().entrySet())
		{
			if (entry.getValue().getIshost() == fighterbean.getIshost() && entry.getValue().getPositiony() == fighterbean.getPositiony())
				ids.add(entry.getValue().getBattleindex());
		}
		return ids;
	}

	/**获取队长或者单角色的等级*/
	public static final int getLeaderLevel(final xbean.BattleInfo battle, boolean ishost)
	{

		int fighterId = getLeaderFighterId(battle, ishost);
		Fighter fighter = battle.getFighterobjects().get(fighterId);
		if (fighter == null)
			return 1;
		return fighter.getEffectRole().getLevel();
	}

	/**获取一方队伍的平均等级*/
	public static final int getTeamAverageLevel(final xbean.BattleInfo battle, boolean ishost)
	{

		int levelsum = 0;
		int count = 0;
		for (xbean.Fighter fighterbean : battle.getFighters().values())
		{
			if (fighterbean.getIshost() == ishost && fighterbean.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				Fighter fighter = battle.getFighterobjects().get(fighterbean.getBattleindex());
				levelsum += fighter.getEffectRole().getLevel();
				count++;
			}
		}
		if (count != 0)
			return levelsum / count;
		else
			return 1;
	}

	/**获取队伍的等级,使用权重值计算*/
	public static final int getTeamLevelWithWeight(final xbean.BattleInfo battle, boolean ishost)
	{
		// 获取队员
		List<xbean.Fighter> fighters = null; 
		if (ishost)
			fighters = getHostFighters(battle);
		else
			fighters = getGuestFighters(battle);

		// 获取等级列表
		List<Integer> levels = new ArrayList<Integer>();
		for (xbean.Fighter fighterbean : fighters)
		{
			Fighter fighter = battle.getFighterobjects().get(fighterbean.getBattleindex());
			if (fighter != null)
			{
				levels.add(fighter.getEffectRole().getLevel());
			}
		}

		// 等级降序排列
		levels.sort(new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2)
			{
				return o2 - o1;
			}
		});

		// TeamLV=(team1*权重1 + team2*权重2 + team3*权重3 + team4*权重4 + team5*权重5)/权重总和
		// 权重公式 最高等级为 teamnum+1   其余的都是1
		int v1 = 0, v2 = 0;
		for (int i = 0; i < levels.size(); i++)
		{
			int lv = levels.get(i);
			int weight = 1;
			if (i == 0)
			{
				weight = levels.size() + 1;
			}
			v1 += lv * weight;
			v2 += weight;
		}

		if (v2 != 0)
		{
			return v1 / v2;
		}
		return 1;
	}

	/**获取一方队伍的最高等级*/
	public static final int getTeamMaxeLevel(final xbean.BattleInfo battle, boolean ishost)
	{

		int levelmax = 1;
		for (xbean.Fighter fighterbean : battle.getFighters().values())
		{
			if (fighterbean.getIshost() == ishost && fighterbean.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{
				Fighter fighter = battle.getFighterobjects().get(fighterbean.getBattleindex());
				if (fighter.getEffectRole().getLevel() > levelmax)
					levelmax = fighter.getEffectRole().getLevel();
			}
		}
		return levelmax;
	}

	/**获取空闲没有战斗者的战斗ID*/
	public static int getAvailableFighterId(final xbean.BattleInfo battle, final int fighterId, boolean ishost)
	{

		if (!battle.getFighters().containsKey(fighterId))
			return fighterId;// 该fighterId没有被占用

		int begin = (ishost) ? BattleField.HOST_FIRST_ID : BattleField.GUEST_FIRST_ID;
		int end = begin + BattleField.A_SIDE_FIGHTER_NUM - 1;
		for (int i = begin; i <= end; i++)
		{
			if (ishost && (i >= 6 && i <= 10))
				continue;// 如果是站在主方的npc，则不能站在宠物位置上
			if (isPvP(battle)&&!ishost && (i >= 20 && i <= 24))
				continue;// 如果是pvp战斗，站在客方的npc，则不能站在宠物位置上
			if (!battle.getFighters().containsKey(i))
				return i;
		}

		return 0;
	}

	/**通过战场配置返回怪物*/
	public static final List<Monster> getMonstersFromBattleConfig(final int battleConfId)
	{

		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battleConfId);
		if (battleConfig == null || battleConfig.monsterRands.size() == 0)
			throw new IllegalArgumentException("根据BattleConfigId初始化战斗失败，错误的BattleConfigId：" + battleConfId);
		List<Monster> monsters = new ArrayList<Monster>();

		java.util.Map<Integer, Integer> id2index = new java.util.HashMap<Integer, Integer>();
		int count = 0;
		// 此处必须按顺序添加，不然会导致根据队伍人数截取怪物时出错
		int last = BattleField.HOST_FIRST_ID + BattleField.A_SIDE_FIGHTER_NUM * 2 - 1;
		for (int fighterId = BattleField.HOST_FIRST_ID; fighterId <= last; fighterId++)
		{
			ID2Odds id2odds = battleConfig.monsterRands.get(fighterId);
			if (id2odds == null)
				continue;
			int randresult = Misc.getProbability(id2odds.odds);
			Integer monsterId = id2odds.ids[randresult];
			Monster monster = new Monster(monsterId);
			monster.setFighterId(fighterId);
			monster.setName(Monster.getMonsterName(monsterId));
			monsters.add(monster);
			
			id2index.put(fighterId, count);
			count ++;
		}
		
		if (battleConfig.randomPos != null) //随机站位 by changhao
		{
			java.util.ArrayList<Integer> array = new java.util.ArrayList<Integer>();
			array.addAll(battleConfig.randomPos);
			Collections.shuffle(array);
			
			int i = 0;
			for (Integer e : battleConfig.randomPos)
			{
				Integer srcindex = id2index.get(e);
				if (srcindex != null)
				{
					Integer j = array.get(i);
					if (j != null)
					{
						Monster srcmonster = monsters.get(srcindex);
						if (srcmonster != null)
						{
							Integer destindex = id2index.get(j);
							if (destindex != null)
							{
								Monster destmonster = monsters.get(destindex);
								
								int id = srcmonster.getFighterId();
								srcmonster.setFighterId(destmonster.getFighterId());
								destmonster.setFighterId(id);
								
								monsters.set(srcindex, destmonster);
								monsters.set(destindex, srcmonster);
								
								i ++;
							}
						}
					}
				}
			}			
		}
		
		return monsters;
	}
	
	/**通过战场配置返回怪物*/
	public static final List<Monster> getMonstersFromBattleConfig(final int battleConfId, int fightPos, String monsterName, int npcShape){
		List<Monster> monsters  = getMonstersFromBattleConfig(battleConfId);
		for(Monster monster : monsters){
			if(monster.getFighterId() == fightPos && monsterName != null){
				monster.setName(monsterName);
				monster.setShape(npcShape);
			}
		}
		return monsters;
	}
	
	/**通过战场配置返回怪物*/
	public static final List<Monster> getMonstersFromBattleConfig(final int battleConfId, int fightPos, String monsterName){
		List<Monster> monsters  = getMonstersFromBattleConfig(battleConfId);
		for(Monster monster : monsters){
			if(monster.getFighterId() == fightPos && monsterName != null){
				monster.setName(monsterName);
			}
		}
		return monsters;
	}
	
	/**得到战场配置中某个怪物的数据 by changhao*/
	public static final Monster getMonsterFromBattleConfig(final int battleConfId, int fightPos, String monsterName){
		List<Monster> monsters  = getMonstersFromBattleConfig(battleConfId);
		for(Monster monster : monsters){
			if(monster.getFighterId() == fightPos && monsterName != null)
			{
				monster.setName(monsterName);
				return monster;
			}
		}
		return null;
	}
	
	/**通过战场配置返回怪物*/
	public static final List<Monster> getMonstersFromBattleConfig(final int battleConfId, int fightPos, int shape, String monsterName){
		List<Monster> monsters  = getMonstersFromBattleConfig(battleConfId);
		for(Monster monster : monsters){
			if(monster.getFighterId() == fightPos && monsterName != null){
				monster.setName(monsterName);
				monster.setShape(shape);
			}
		}
		return monsters;
	}

	/**根据Id来判断是主客方*/
	public static final boolean isHost(int fighterId)
	{

		if (fighterId >= BattleField.HOST_FIRST_ID && fighterId <= (HOST_FIRST_ID + A_SIDE_FIGHTER_NUM - 1))
			return true;
		else
			return false;
	}

	public static java.util.Set<Long> getHostRoleIDs(final xbean.BattleInfo battle)
	{

		final java.util.Set<Long> ids = new java.util.HashSet<Long>();
		for (java.util.Map.Entry<Long, Integer> e : battle.getRoleids().entrySet())
		{
			xbean.Fighter fighter = battle.getFighters().get(e.getValue());
			if (fighter.getIshost())
			{
				ids.add((Long) e.getKey());
			}
		}
		return ids;
	}

	public static java.util.Set<Long> getGuestRoleIDs(final xbean.BattleInfo battle)
	{

		final java.util.Set<Long> ids = new java.util.HashSet<Long>();
		for (java.util.Map.Entry<Long, Integer> e : battle.getRoleids().entrySet())
		{
			xbean.Fighter fighter = battle.getFighters().get(e.getValue());
			if (!fighter.getIshost())
			{
				ids.add((Long) e.getKey());
			}
		}
		return ids;
	}

	/**获取主方角色中最高的物理攻击力*/
	public static int getHostRoleMaxAttack(xbean.BattleInfo battle)
	{
		if (battle == null)
			return 0;
		int maxattack = 0;
		for (Fighter fighter : battle.getFighterobjects().values())
		{
			if (fighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_ROLE && fighter.getFighterBean().getIshost())
			{
				int attack = Math.max(fighter.getBuffAgent().getERole().getAttack(), 
						fighter.getBuffAgent().getERole().getMagicAttack());
				if (attack > maxattack)
					maxattack = attack;
			}
		}
		return maxattack;
	}

	/**返还所有活着的友方NPC怪物 */
	public static List<Fighter> getAllAliveFriendMonsters(xbean.BattleInfo battle, int fighterId)
	{
		List<Fighter> friendmonsters = new ArrayList<Fighter>();
		Fighter opfighter = battle.getFighterobjects().get(fighterId);
		if (opfighter == null)
			return friendmonsters;
		boolean ishost = opfighter.getFighterBean().getIshost();
		for (Fighter fighter : battle.getFighterobjects().values())
		{
			if (fighter.getFighterBean().getIshost() == ishost && fighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
			{
				if (checkLiveInBattle(battle, fighterId))
					friendmonsters.add(fighter);
			}
		}
		return friendmonsters;
	}

	/**验证战斗者 验证1: 宠物在的时候，它的主人也在战斗中*/
	public static void validFighters(xbean.BattleInfo battle)
	{
		for (xbean.Fighter xfighter : battle.getFighters().values())
		{
			if (xfighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			{

			}
			if (xfighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
			{
				int masterid = xfighter.getBattleindex() - 5;
				if (!battle.getFighters().containsKey(masterid))
				{
					battle.getFighters().remove(xfighter.getBattleindex());
					battle.getFighterobjects().remove(xfighter.getBattleindex());
					battle.getRoundresult().remove(xfighter.getBattleindex());
				}
			}
		}
	}

	/**处理失败的怪物*/
	public static void processFaileMonsters(xbean.BattleInfo battle, boolean includeGhost)
	{
		try{
			for (xbean.Fighter xfighter : battle.getFighters().values())
			{
				if (xfighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
				{
					int fighterId = xfighter.getBattleindex();
					Integer result = battle.getRoundresult().get(fighterId);
					if (result == null)
						continue;

					int result2 = 0;
					
					if ((result & ResultType.RESULT_DEATH) != 0)
						result2 = ResultType.RESULT_DEATH;

					else if ((result & ResultType.RESULT_KICKOUT) != 0)
						result2 = ResultType.RESULT_KICKOUT;

					else if ((result & ResultType.RESULT_RUNAWAY) != 0)
						result2 = ResultType.RESULT_RUNAWAY;

					else if (includeGhost && (result & ResultType.RESULT_GHOST) != 0)
						result2 = ResultType.RESULT_GHOST;

					else if ((result & ResultType.RESULT_SEIZE) != 0)
						result2 = ResultType.RESULT_SEIZE;

					else if ((result & ResultType.RESULT_SUMMONBACK) != 0)
						result2 = ResultType.RESULT_SUMMONBACK;

					if (result2 == 0)
						continue;
					xbean.Monster xmonster = battle.getMonsters().get(fighterId);
					if (xmonster == null)
						continue;
					xbean.FailedMonster fmons = getFailedMonster(fighterId, result2, xmonster);
					if(!battle.getFailedmonsters().contains(fmons))
						battle.getFailedmonsters().add(getFailedMonster(fighterId, result2, xmonster));
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**生成失败怪物结构 Procedure内调用*/
	public static xbean.FailedMonster getFailedMonster(int fighterId, int result, xbean.Monster monster)
	{
		xbean.FailedMonster fmonster = xbean.Pod.newFailedMonster();
		fmonster.setFighterid(fighterId);
		fmonster.setIshost(BattleField.isHost(fighterId));
		fmonster.setMonsterid(monster.getMonsterid());
		fmonster.setFightnpctype(monster.getFightnpctype());
		fmonster.setMonstertype(monster.getMonstertype());
		fmonster.setLevel(monster.getLevel());
		fmonster.setShape(monster.getShape());
		fmonster.setResult(result);
		return fmonster;
	}

	public static NewResultItem convertResultItem2New(ResultItem item)
	{
		NewResultItem newitem = new NewResultItem();
		newitem.execute = item.execute;
		newitem.newfighter = item.newfighter;
		newitem.rolechangedattrs = item.rolechangedattrs;
		newitem.petchangedattrs = item.petchangedattrs;
		for (SubResultItem subresult : item.subresultlist)
		{
			NewSubResultItem newsubresult = new NewSubResultItem();
			newsubresult.subskillid = subresult.subskillid;
			newsubresult.subskillstarttime = subresult.subskillstarttime;
			for (DemoResult demo : subresult.resultlist)
			{
				NewDemoResult newdemo = new NewDemoResult();
				newdemo.resulttype = demo.resulttype;
				newdemo.targetid = demo.targetid;
				newdemo.flagtype = demo.flagtype;
				newdemo.demobuffs = demo.demobuffs;
				newdemo.demoattrs = demo.demoattrs;
				if (demo.hpchange != 0)
					newdemo.datas.put(NewDemoResult.HP_CHANGE, demo.hpchange);
				if (demo.mpchange != 0)
					newdemo.datas.put(NewDemoResult.MP_CHANGE, demo.mpchange);
				if (demo.spchange != 0)
					newdemo.datas.put(NewDemoResult.SP_CHANGE, demo.spchange);
				if (demo.epchange != 0)
					newdemo.datas.put(NewDemoResult.EP_CHANGE, demo.epchange);
				if (demo.shapechange != 0)
					newdemo.datas.put(NewDemoResult.SHAPE_CHANGE, demo.shapechange);				
				if (demo.uplimithpchange != 0)
					newdemo.datas.put(NewDemoResult.UL_HP_CHANGE, demo.uplimithpchange);
				if (demo.targetresult != 0)
					newdemo.datas.put(NewDemoResult.TARGET_RESULT, demo.targetresult);
				if (demo.returnhurt != 0)
					newdemo.datas.put(NewDemoResult.RETURN_HURT, demo.returnhurt);
				if (demo.attackback != 0)
					newdemo.datas.put(NewDemoResult.ATTACK_BACK, demo.attackback);
				if (demo.stealhp != 0)
					newdemo.datas.put(NewDemoResult.STEAL_HP, demo.stealhp);
				if (demo.attackerresult != 0)
					newdemo.datas.put(NewDemoResult.ATTACKER_RESULT, demo.attackerresult);
				if (demo.protecterid != 0)
					newdemo.datas.put(NewDemoResult.PROTECTER_ID, demo.protecterid);
				if (demo.protecterhpchange != 0)
					newdemo.datas.put(NewDemoResult.PROTECTER_HP_CHANGE, demo.protecterhpchange);
				if (demo.protecterresult != 0)
					newdemo.datas.put(NewDemoResult.PROTECTER_RESULT, demo.protecterresult);
				if (demo.assisterid != 0)
					newdemo.datas.put(NewDemoResult.ASSISTER_ID, demo.assisterid);
				if (demo.stealmp != 0)
					newdemo.datas.put(NewDemoResult.STEAL_MP, demo.stealmp);
				if (demo.attackerulhpchange != 0)
					newdemo.datas.put(NewDemoResult.RETURN_HURT_DEATH, demo.attackerulhpchange);
				if (demo.protecterulhpchange != 0)
					newdemo.datas.put(NewDemoResult.PROTECTER_MAXHP_CHANGE, demo.protecterulhpchange);
				if (demo.godblesshp != 0)
					newdemo.datas.put(NewDemoResult.HP_GODBLESS, demo.godblesshp);
				newsubresult.resultlist.add(newdemo);	
			}
			newitem.subresultlist.add(newsubresult);	
		}
		return newitem;
	}


	static void calcRoleDatas(xbean.BattleInfo battle, boolean ishost)
	{
		int rolenum = 0;
		int rolesumlevel = 0;
		int maxlv = -1;
		int minlv = -1;
		Set<Long> roleids = new HashSet<Long>();
		for (Map.Entry<Long, Integer> entry : battle.getRoleids().entrySet())
		{
			Fighter fighter = battle.getFighterobjects().get(entry.getValue());
			// 不应该为null，不判断，如果空指针直接战斗回滚
			if (fighter.getFighterBean().getIshost() == ishost)
			{
				int lv = fighter.getEffectRole().getLevel();
				roleids.add(entry.getKey());
				rolenum++;
				rolesumlevel += lv;
				if (minlv == -1 || lv < minlv)
					minlv = lv;
				if (maxlv == -1 || lv > maxlv)
					maxlv = lv;
			}
		}
		battle.getBattledatas().put(ishost ? xbean.BattleInfo.DATA_HOST_ROLE_NUM : xbean.BattleInfo.DATA_GUEST_ROLE_NUM, (float) rolenum);
		battle.getBattledatas().put(ishost ? xbean.BattleInfo.DATA_HOST_ROLE_AVERAGE_LEVEL : xbean.BattleInfo.DATA_GUEST_ROLE_AVERAGE_LEVEL, (float) rolesumlevel / rolenum);

		// 统计主方的最大最小等级
		if (maxlv > 0 && ishost)
			battle.getBattledatas().put(xbean.BattleInfo.DATA_HOST_ROLE_MAX_LEVEL, (float) maxlv);
		if (minlv > 0 && ishost)
			battle.getBattledatas().put(xbean.BattleInfo.DATA_HOST_ROLE_MIN_LEVEL, (float) minlv);
	}

	/**计算怪物的相关数据，只计算客方怪物的*/
	static void calcMonsterDatas(xbean.BattleInfo battle)
	{
		int monsternum = 0;
		int monsterchiefnum = 0;
		int monstersumlevel = 0;
		for (Map.Entry<Integer, xbean.Monster> entry : battle.getMonsters().entrySet())
		{
			if (isHost(entry.getKey()))
				continue;// 主方不统计
			monsternum++;
			if (entry.getValue().getMonstertype() == Monster.MONSTER_CHIEF)
				monsterchiefnum++;
			monstersumlevel += entry.getValue().getLevel();
		}
		battle.getBattledatas().put(xbean.BattleInfo.DATA_MONSTER_NUM, (float) monsternum);
		battle.getBattledatas().put(xbean.BattleInfo.DATA_MONSTER_CHIEF_NUM, (float) monsterchiefnum);
		battle.getBattledatas().put(xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL, (float) monstersumlevel / monsternum);
	}

	/**根据fighterid获得Fighter*/
	static Fighter getFighter(xbean.BattleInfo battle, int fighterId)
	{
		if (fighterId == 0)
			return battle.getFieldfighter();
		else
			return battle.getFighterobjects().get(fighterId);
	}
	
	static boolean isFormSuppressed(xbean.BattleInfo battle, boolean ishost){

		FormationConfig myFormconf = null;
		FormationConfig hisFormconf = null;
		if (ishost) {
			myFormconf = fire.pb.battle.Module.getInstance().getFormationById(battle.getHostform(), battle.getHostformlevel());
			hisFormconf = fire.pb.battle.Module.getInstance().getFormationById(battle.getGuestform(), battle.getGuestformlevel());
		} else {
			myFormconf = fire.pb.battle.Module.getInstance().getFormationById(battle.getGuestform(),battle.getGuestformlevel());
			hisFormconf = fire.pb.battle.Module.getInstance().getFormationById(battle.getHostform(), battle.getHostformlevel());
		}
		
		boolean ok1 = false;
		if (myFormconf.getmapSuppressform1().get(hisFormconf.getFormId()) != null)
		{
			ok1 = true;
		}
		
		boolean ok2 = false;
		if (myFormconf.getmapSuppressform2().get(hisFormconf.getFormId()) != null)
		{
			ok2 = true;
		}
		
		return ok1 || ok2;
	}
	
	
	public static boolean validateaim(final xbean.BattleInfo battle, final SubSkillConfig subSkill, int optid, int aimid)	
	{
		boolean bchecked = false; 
		Fighter f = battle.getFighterobjects().get(aimid);
		Fighter fighter = battle.getFighterobjects().get(optid);
		if(subSkill.getGoalRaceTypes()!=null&&subSkill.getGoalRaceTypes().length>0)
		{
			for(int racetype: subSkill.getGoalRaceTypes())
			{
				if(f.getFightertype() == racetype)
				{
					bchecked = true;
					break;
				}
			}
			if(bchecked == false)
				return false;
			bchecked = false; 
		}
		if(subSkill.getGoalMonsterids()!=null&&subSkill.getGoalMonsterids().length>0)
		{
			for(int monsterid: subSkill.getGoalMonsterids())
			{
				if(fighter.getFightertype() > xbean.Fighter.FIGHTER_PET && f.getFighterBean().getUniqueid() == monsterid)
				{
					bchecked = true;
					break;
				}
			}
			if(bchecked == false)
				return false;
			bchecked = false;
		}
		if(subSkill.getGoalRelationTypes()!=null&&subSkill.getGoalRelationTypes().length>0)
		{
			for(int relationtype: subSkill.getGoalRelationTypes())
			{
				if(relationtype == 4 )
				{
					if(fighter.isRole() && aimid == optid +5)
					{	 
						bchecked = true;
						break;
					}
					else if(fighter.isPet() && aimid == optid -5)
					{
						bchecked = true;
						break;
					}
				}
				else
				{
					int relation = BattleAimRelation.COUPLE * (int) (Math.pow(2, relationtype-1)); 
					if(Math.abs(aimid-optid) < 14)
					{
						int relation2 = BattleField.getAimRelation(battle, optid, aimid);
						ParseObjIDInBattle objinbattle = new ParseObjIDInBattle(relation);
						if (objinbattle.validTarget(relation2))
						{
							bchecked = true;
							break;
						}
					}
				}
			}
			if(bchecked == false)
				return false;
		}
		return true;
	}	
	
	/** 导出战斗录像处理放在此 */
	public static void ConvertBattlevideo(OctetsStream stream) throws MarshalException {
		java.util.List<fire.pb.battle.FighterInfo> figterinfoonbegin = new java.util.LinkedList<fire.pb.battle.FighterInfo>(); // 出战斗时的战斗者信息
		java.util.Map<Integer, xbean.RoundResultItems> roundresultitem = new java.util.HashMap<Integer, xbean.RoundResultItems>(); // 整场战斗的demo

		if (true) {
			// 战斗环境
			final SSendBattleStart pbattleStart = new SSendBattleStart();
			pbattleStart.unmarshal(stream);

			// 战斗开始人物
			int nfigterinfo = stream.unmarshal_byte();
			for (int i = 0; i < nfigterinfo; i++) {
				fire.pb.battle.FighterInfo figterinfo = new fire.pb.battle.FighterInfo();
				figterinfo.unmarshal(stream);
				figterinfoonbegin.add(figterinfo);
			}

			int nround = stream.unmarshal_byte();
			for (int i = 0; i < nround; i++) {
				xbean.RoundResultItems rounditem = xbean.Pod.newRoundResultItems();
				int resultitemcount = stream.unmarshal_byte();
				for (int j = 0; j < resultitemcount; j++) {
					fire.pb.battle.NewResultItem item = new fire.pb.battle.NewResultItem();
					item.unmarshal(stream);
					rounditem.getResultitems().add(item);
				}

				int aiactioncount = stream.unmarshal_byte();
				for (int j = 0; j < aiactioncount; j++) {
					fire.pb.battle.AIOperation act = new fire.pb.battle.AIOperation();
					act.unmarshal(stream);
					rounditem.getAiactions().add(act);
				}

				int hpcount = stream.unmarshal_int();
				for (int j = 0; j < hpcount; j++) {
					int id = stream.unmarshal_int();
					int hp = stream.unmarshal_int();
					rounditem.getFighterfinallyhps().put(id, hp);
				}

				int mpcount = stream.unmarshal_int();
				for (int j = 0; j < mpcount; j++) {
					int id = stream.unmarshal_int();
					int mp = stream.unmarshal_int();
					rounditem.getFighterfinallymps().put(id, mp);
				}

				roundresultitem.put(i + 1, rounditem);
			}
		}
	}

	public static String outBattlevideoProcess(xbean.BattleInfo battle,byte cameraType, long guestID,long hostID) 
	{
		long curentime = System.currentTimeMillis();
		if(cameraType == BattleCameraInfo.CAMERA_TYPE_MISSION)
		{
			//如果是已经有一个相同任务的录像已经在等待发送到录像服务器，就不用在记录录像了。
			//此次代码是避免短时间内同一场战斗反复更新
			CameraInfo cameraInfo = xtable.Missioncameras.get(guestID);   
			if(cameraInfo !=null && (curentime - cameraInfo.getEndtime()) < BattleCameraInfo.MISSION_MAX_CAMERA_SPASE_TIME)
				return null;
			if(fire.pb.battle.BattleCameraThread.getInstance().haveSameCamera(guestID))
				return null;
		}
		
		if(cameraType == BattleCameraInfo.CAMERA_TYPE_BINGFENG)
		{
			//为了按职业区分录像 做如下处理
			PropRole prole = new PropRole(hostID, true);
			guestID = guestID*100+prole.getSchool();			

			CameraInfo cameraInfo = xtable.Missioncameras.get(guestID);   
			if(cameraInfo !=null && ((int)curentime-battle.getBattlebegintime()) > cameraInfo.getEndtime())
				return null;
			
			if(fire.pb.battle.BattleCameraThread.getInstance().haveShortTimeCamera(guestID, (int) (curentime-battle.getBattlebegintime())))
				return null;//已经有更短时间的录像不存了
		}
		
		OctetsStream stream = outBattlevideoProcess(battle);
		if(stream!= null)
		{
			BattleCameraInfo battleCameraInfo = new BattleCameraInfo(stream, cameraType, guestID,hostID, curentime,(int)(curentime-battle.getBattlebegintime()));
			if(cameraType == BattleCameraInfo.CAMERA_TYPE_LIVEDIE){
				fire.pb.battle.livedie.LiveDieEndHandler.dealLiveDieEnd(battleCameraInfo.getCameraFileName(),hostID,guestID,battle);
			}
			fire.pb.battle.BattleCameraThread.getInstance().add(battleCameraInfo,true);
			return battleCameraInfo.getCameraFileName();
		}
		return null;
	}
	
	/***
	 * 需要过滤掉身上有某个buf的目标，避免释放同一个控制技能到一个目标身上 by changhao
	 * @param curskill
	 * @param buffids
	 * @return
	 */
	public static java.util.List<Integer> GetFilterAimsByBuffs(int curskill, java.util.List<Integer> buffids)
	{		
		java.util.List<Integer> filterbuffids = new java.util.ArrayList<Integer>();
		
		FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(curskill);
		if (sconf != null)
		{
			for (SubSkillConfig subskill : sconf.getSubSkills())
			{
				if(subskill==null||subskill.getBuffUnits()==null||subskill.getBuffUnits().length<1)
					continue;
				for (BuffUnit buffarg : subskill.getBuffUnits())
				{
					if(buffarg==null) continue;
					
					if (buffids.contains(buffarg.buffIndex))
					{
						filterbuffids.add(buffarg.buffIndex);
					}
				}
			}						
		}
		
		return filterbuffids;
	}
	
	
	/**
	 * 冰封王座特殊处理
	 * @param battle
	 * @param cameraType
	 * @param guestID
	 * @param hostID
	 * @return
	 */
	public static String outBattlevideoBFProcess(xbean.BattleInfo battle, long guestID, long hostID, long curentime, long usetime) {
		// 为了按职业区分录像 做如下处理
		PropRole prole = new PropRole(hostID, true);
		guestID = guestID * 100 + prole.getSchool();

		CameraInfo cameraInfo = xtable.Missioncameras.get(guestID);
		if (cameraInfo != null && usetime > cameraInfo.getEndtime()) {
			fire.pb.instancezone.Module.logger.info("usetime > endtime:" + hostID + ",battleid:" + guestID + ",usetime:" + usetime);
			return null;
		}

		if (fire.pb.battle.BattleCameraThread.getInstance().haveShortTimeCamera(guestID, (int) usetime)) {
			fire.pb.instancezone.Module.logger.info("haveShorttime:" + hostID + ",battleid:" + guestID + ",usetime:" + (int) usetime + ",realusetime:" + usetime);
			return null;// 已经有更短时间的录像不存了
		}

		OctetsStream stream = outBattlevideoProcess(battle);
		if (stream != null) {
			BattleCameraInfo battleCameraInfo = new BattleCameraInfo(stream, (byte)BattleCameraInfo.CAMERA_TYPE_BINGFENG, guestID, hostID, curentime, (int)usetime);
			fire.pb.battle.BattleCameraThread.getInstance().add(battleCameraInfo, true);
			fire.pb.instancezone.Module.logger.info("add ok:" + hostID + ",battleid:" + guestID + ",usetime:" + usetime);
			return battleCameraInfo.getCameraFileName();
		}
		fire.pb.instancezone.Module.logger.info("stream == null:" + hostID + ",battleid:" + guestID + ",usetime:" + usetime);
		return null;// 已经有更短时间的录像不存了
	}
	
	/** 导出战斗录像处理放在此 */
	public static OctetsStream outBattlevideoProcess(xbean.BattleInfo battle) {
		OctetsStream stream = new OctetsStream();
		// 战斗环境
		final SSendBattleStart pbattleStart = new SSendBattleStart();
		pbattleStart.battletype = battle.getBattletype();
		pbattleStart.battleconfigid = battle.getConfigid();
		pbattleStart.roundnum = (battle.getPhase() == BattleInfo.PHASE_WAITOPERATION) ? battle.getRound() - 1
				: battle.getRound();
		pbattleStart.enemyside = 0;
		pbattleStart.friendsformation = battle.getHostform();
		pbattleStart.enemyformation = battle.getGuestform();
		pbattleStart.friendsformationlevel = battle.getHostformlevel();
		pbattleStart.enemyformationlevel = battle.getHostformlevel();
		pbattleStart.background = (byte) battle.getBackground();
		pbattleStart.backmusic = (byte) battle.getBgm();
		pbattleStart.marshal(stream);
		// 战斗开始人物
		stream.marshal((byte) battle.getFigterinfoonbegin().size());
		for (FighterInfo f : battle.getFigterinfoonbegin())
			f.marshal(stream);

		stream.marshal((byte) battle.getRound());
		for (int i = 0; i < battle.getRound(); i++) {
			xbean.RoundResultItems roundrsultitem = battle.getRoundresultitem().get(i + 1);
			if (roundrsultitem != null) {
				byte resultitemcount = (byte) roundrsultitem.getResultitems().size();
				stream.marshal(resultitemcount);
				for (fire.pb.battle.NewResultItem newitem : roundrsultitem.getResultitems()) {
					newitem.marshal(stream);
				}

				byte aiactioncount = (byte) roundrsultitem.getAiactions().size();
				stream.marshal(aiactioncount);
				for (fire.pb.battle.AIOperation act : roundrsultitem.getAiactions()) {
					act.marshal(stream);
				}

				stream.marshal(roundrsultitem.getFighterfinallyhps().size());
				for (java.util.Map.Entry<Integer, Integer> _e_ : roundrsultitem.getFighterfinallyhps().entrySet()) {
					stream.marshal(_e_.getKey());
					stream.marshal(_e_.getValue());
				}

				stream.marshal(roundrsultitem.getFighterfinallymps().size());
				for (java.util.Map.Entry<Integer, Integer> _e_ : roundrsultitem.getFighterfinallymps().entrySet()) {
					stream.marshal(_e_.getKey());
					stream.marshal(_e_.getValue());
				}
			}
		}
/*		try {
			ConvertBattlevideo(stream);
			writeFile(stream.getBytes(), "./video.txt");
			readFile("./video.txt");
			return stream;
		} catch (MarshalException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
		return stream;
	}

	/** 保存文件 */
	public static void writeFile(byte[] fileBytes, final String savePath) {
		BufferedOutputStream bos = null;
		if (fileBytes.length == 0)
			return;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(savePath));
			bos.write(fileBytes, 0, fileBytes.length);
			bos.flush();
		} catch (Exception e) {
			BattleField.logger.info(new StringBuilder("保存文件出错"));
		} finally {
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					BattleField.logger.info(new StringBuilder("保存文件关闭流"));
				}
			}
		}

	}
	
	public static void readFile(final String savePath) {
		File streamFile = new File(savePath);
		try {
			FileInputStream fis=new FileInputStream(streamFile);
			byte[] fileBytes = new byte[(int) streamFile.length()];			
			fis.read(fileBytes);
			OctetsStream o = new OctetsStream();
			o.insert(0,fileBytes);
//			int nfigterinfo = o.unmarshal_short();
			ConvertBattlevideo(o);
		} catch (Exception e) {
			BattleField.logger.info(new StringBuilder("保存文件出错"));
		}
	}
}