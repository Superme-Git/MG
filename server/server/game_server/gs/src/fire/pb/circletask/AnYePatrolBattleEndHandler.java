package fire.pb.circletask;

import java.util.List;
import java.util.Map;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.HelpCountManage;
import fire.pb.PAddClanPointProc;
import fire.pb.PAddExpProc;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.ResultType;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.clan.ClanUtils;
import fire.pb.main.ConfigManager;

public class AnYePatrolBattleEndHandler extends BasicEndHandler {
	private int taskpos;
	private final long enterTime;

	public AnYePatrolBattleEndHandler(int taskpos) {
		super();
		this.taskpos = taskpos;
		this.enterTime = System.currentTimeMillis();
	}

	@Override
	public boolean handleAward() {
		xbean.Fighter fightLeader = battle.getFighters().get(1);
		if (fightLeader == null)
			return false;
		//拿到完成任务 人的公会信息
		Long taskRoleFaction = xtable.Properties.selectClankey(fightLeader.getUniqueid());//任务所有人即队长的公会id
		RoleAnYeTask rayt = new RoleAnYeTask(fightLeader.getUniqueid());
		xbean.AnYeTask ayt = rayt.getTasks().get(taskpos);
		if (ayt == null)
			return false;
		
		long now = System.currentTimeMillis();
		if (!fire.pb.util.DateValidate.inTheSameWeek(enterTime, now)) {
			//FIXME:战斗中跨周了，不再完成这个任务！
			return false;
		}
		
		//援助奖励，帮队友
		StringBuilder sb = new StringBuilder();
		for (xbean.Fighter xfighter : this.battle.getFighters().values()) {
			long memberid = xfighter.getUniqueid();
			if(memberid == fightLeader.getUniqueid())
				continue;
			
			String rolename = xtable.Properties.selectRolename(memberid);
			sb.append(rolename).append(",");
			
			Integer level = xtable.Properties.selectLevel(memberid);
			if(level == null || level.intValue() < 50)
				continue;
			
			Long curMemFactionkey = xtable.Properties.selectClankey(memberid);
			if(taskRoleFaction!=null&&curMemFactionkey != null && curMemFactionkey > 0 && taskRoleFaction > 0L) {
				if(curMemFactionkey != taskRoleFaction.longValue()){
					//世界援助
					final fire.pb.PropRole prole = new fire.pb.PropRole(memberid, false);
					long addexp = HelpCountManage.getAddExpValueByHelpFight(memberid);
					long resultexp=HelpCountManage.getInstance().addExpValue(memberid, 1, "马戏团世界援助奖励");
					if(resultexp>0){
						new fire.pb.PAddExpProc(memberid, addexp, PAddExpProc.OTHER,"马戏团世界援助").call();
					}
					//添加声望奖励
					fire.pb.HelpCountManage.getInstance().addShengWangToRole(memberid,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 2);
				} else {
					//公会援助
					final fire.pb.PropRole prole = new fire.pb.PropRole(memberid, false);
					long addexp = HelpCountManage.getAddExpValueByHelpFight(memberid);
					long resultexp=HelpCountManage.getInstance().addExpValue(memberid, 1, "马戏团公会援助奖励");
					if(resultexp>0){
						new fire.pb.PAddExpProc(memberid, addexp, PAddExpProc.OTHER,"马戏团公会援助").call();
					}
					//添加声望奖励
					fire.pb.HelpCountManage.getInstance().addShengWangToRole(memberid,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 2);
					
					int addcount = 5;
					int resultvalue=HelpCountManage.getInstance().addFactionValue(memberid, addcount, "马戏团公会援助奖励");
					if(resultvalue>0){
						new PAddClanPointProc(memberid, resultvalue, YYLoggerTuJingEnum.tujing_Value_huodongjiangli, false, "马戏团公会援助").call();
					}
					HelpCountManage.getInstance().addFactionHelpNum(memberid, "马戏团公会援助");
				}
			} else {
				//世界援助
				final fire.pb.PropRole prole = new fire.pb.PropRole(memberid, false);
				long addexp = HelpCountManage.getAddExpValueByHelpFight(memberid);
				long resultexp=HelpCountManage.getInstance().addExpValue(memberid, 1, "马戏团公会援助奖励");
				if(resultexp>0){
					new fire.pb.PAddExpProc(memberid, addexp, PAddExpProc.OTHER,"马戏团公会援助").call();
				}
				//添加声望奖励
				fire.pb.HelpCountManage.getInstance().addShengWangToRole(memberid,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 2);
			}
		}
		
		//替换求助信息
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
			
			AnYeMaXiTuanConf conf = ConfigManager.getInstance().getConf(AnYeMaXiTuanConf.class).get(ayt.getId());
			if (conf != null) {
				fire.pb.talk.MessageMgr.sendHelpMsg(fightLeader.getUniqueid(), conf.getId(), conf.getTaskname(), sb.toString());
			}
		}
		return true;
	}

	@Override
	public boolean handleDeath() {
		super.handleDeath();
		super.handlePetFighters();
		
		long now = System.currentTimeMillis();
		if (!fire.pb.util.DateValidate.inTheSameWeek(enterTime, now)) {
			//FIXME:战斗中跨周了，不再完成这个任务！
			return true;
		}
		
		List<xbean.Fighter> fighters = super.getDeadRoles();
		for (xbean.Fighter f : fighters) {
			fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(
					f.getUniqueid(), false);
			fire.pb.attr.SRefreshRoleData data = new fire.pb.attr.SRefreshRoleData();
			Map<Integer, Float> result1 = erole.fullHpAndRecoverWound();
			Map<Integer, Float> result2 = erole.fullMp();
			data.datas.putAll(result1);
			data.datas.putAll(result2);
			mkdb.Procedure.psendWhileCommit(f.getUniqueid(), data);
		}

		xbean.Fighter fightLeader = battle.getFighters().get(1);
		if (fightLeader != null) {
			RoleAnYeTask rayt = new RoleAnYeTask(fightLeader.getUniqueid());
			if(rayt.getTasks().get(taskpos).getState() != SpecialQuestState.UNDONE) {
				return true;
			}

			final Integer val = battle.getRoundresult().get(1);
			if (val != null) {
				if ((val & ResultType.RESULT_DEATH) != 0
						|| (val & ResultType.RESULT_RUNAWAY) != 0) {
					return true;
				}
			}
			rayt.handleTaskOver(taskpos);
		}
		return true;
	}
}