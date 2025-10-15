package fire.pb.activity.winner;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import xbean.Fighter;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.ResultType;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.effect.Role;
import fire.pb.effect.RoleImpl;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;
import fire.pb.util.TaskDlgUtil;
import gnet.link.Onlines;


public class WinnerEndHandler extends BattleEndHandler {

	private final long teamid;

	private final int npcbaseid;

	public WinnerEndHandler(long teamid, int npcbaseid) {

		super();
		this.teamid = teamid;
		this.npcbaseid = npcbaseid;

	}
	
	@Override
	protected boolean handleCamera() {
		return true;
	}
	
	@Override
	protected void notifyBattleEnd() {

		for (long roleid : battle.getRoleids().keySet()) {
			fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.BattleEndEvent(roleid, battle));
		}
	}
	
	@Override
	protected boolean handleAward() {
		WinnerRecord winnerRecord = WinnerManager.getInstance().teams.get(teamid);
		if(winnerRecord==null){
			return false;
		}
		Collection<xbean.Fighter> fighters = getAllRoleFighters();
		long teamLeaderID = 0;
		if(battle.getFighters().get(BattleField.HOST_FIRST_ID)!=null){
			teamLeaderID = battle.getFighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
		}
		int teamSize = fighters.size();
		for (xbean.Fighter f : fighters) {
			// 不管队员有没有死,都会获得奖励
			long roleid = f.getUniqueid();
			Map<String, Object> paras = new HashMap<String, Object>();
			if(roleid == teamLeaderID){
				paras.put(RewardMgr.IS_LEADER,  1);
			}else{
				paras.put(RewardMgr.IS_LEADER,  0);
			}
			paras.put(RewardMgr.TEAM_NUM,  teamSize);//添加队伍人数信息
			int curnum=0;
			WinnerRoleRecord winnerRoleRecord = WinnerManager.winnerrolerecords.get(roleid);
			if(winnerRoleRecord!=null){
				curnum=winnerRoleRecord.getScore()+calCurrentStageScore();
			}else{
				curnum=calCurrentStageScore();
			}
			paras.put(RewardMgr.TIME, curnum);
			
			RewardMgr.getInstance().distributeAllAward(roleid, WinnerManager.getAwardTaskComplete(), paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guanjunshilianget,0, PAddExpProc.OTHER_QUEST, "冠军试炼");
			WinnerManager.logger.info("玩家roleid "+roleid+"冠军试炼战斗结束奖励相关\t队伍人数"+teamSize+"\t是否队长\t"+paras.get(RewardMgr.IS_LEADER)+"\t当前环数"+curnum+"\t队伍id"+teamid+"\t当前队伍成绩\t"+winnerRecord.getTeamScore());
			//记录玩家的活跃度
			RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
			if (actrole != null){
				actrole.setActiveNum2(RoleLiveness.WINNER, winnerRecord.getTeamScore()+calCurrentStageScore());
				actrole.handleActiveLivenessData(RoleLiveness.WINNER);
			}
		}
		long curtime=System.currentTimeMillis();
		//是否活动已经结束，如果结束不记录成绩
		if (!WinnerManager.getInstance().isInWinnerActiveTime(curtime)){
			return true;
		}
		//计算消耗时间
		long usetime=curtime-winnerRecord.getTaskstartTime();
		if(usetime>0){
			winnerRecord.setAllCostTime(winnerRecord.getAllCostTime()+usetime);
		}
		
		int credit = calCurrentStageScore();
		// 给所有队员加上积分
		for (xbean.Fighter fighter : fighters) {
			long roleid = fighter.getUniqueid();
			if (!TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID))
				continue;
			WinnerRoleRecord singleRecord = WinnerManager.winnerrolerecords.get(roleid);
			if (singleRecord == null) {
				singleRecord=new WinnerRoleRecord(roleid);
				WinnerManager.winnerrolerecords.put(roleid, singleRecord);
			}
			int initScore = singleRecord.getScore();
			singleRecord.setScore(initScore + credit);
			singleRecord.setTime(curtime);
		}
		// 给队伍加积分
		int initTotalScore = winnerRecord.getTeamScore();
		winnerRecord.setTeamScore(initTotalScore + credit);
		// 如果这是最后一关,则要多做一些处理
		if (winnerRecord.getStageSucc()>= WinnerManager.TASK_MAX_NUM-1) {
			long time = curtime - winnerRecord.getRoundStartTime();
			//设置当前完成的轮数
			winnerRecord.setRound(winnerRecord.getRound()+1);
			winnerRecord.getStageList().clear();
			winnerRecord.setStageSucc(0);
			
			long originalTime = winnerRecord.getRoundCostTime();
			if (originalTime == 0 || originalTime > time){
				winnerRecord.setRoundCostTime(time);
			}
			// 发消息
			for (xbean.Fighter fighter : fighters) {
				long roleid = fighter.getUniqueid();
				if (!TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID)){
					continue;
				}
				TaskDlgUtil.refreshTaskState(roleid, WinnerManager.WINNER_TASK_ID, SpecialQuestState.SUCCESS);// 把任务栏标记成已完成
				MessageMgr.psendMsgNotifyWhileCommit(roleid, 140830, npcbaseid, null);
				//这里如果活动没有结束，就给玩家添加一个任务，引导到到接活动任务的npc处
				if(WinnerManager.npc_keys!=null&&WinnerManager.npc_keys.size()>0){
					Npc npc = SceneNpcManager.selectNpcByKey(WinnerManager.npc_keys.get(0));
					if(npc!=null){
						WinnerManager.getInstance().createWinnerTask(WinnerManager.WINNER_TASK_ID_ACCEPT,roleid,npc,SpecialQuestState.UNDONE,0,1);//创建任务
					}
				}
			}

		} else {//不是最后一关
			try {
				int nextStage = winnerRecord.getStageSucc() + 1;
				if(winnerRecord.getStageList().size()<=nextStage){
					WinnerManager.logger.info("冠军试炼战斗结束异常nextStage"+nextStage+"\t当前关卡列表数量"+winnerRecord.getStageList().size()+"\t队伍id"+teamid);
					return true;
				}
				int nextNpcIndex = winnerRecord.getStageList().get(nextStage);
				winnerRecord.setStageSucc(nextStage);
				if(WinnerManager.npc_keys!=null&&WinnerManager.npc_keys.size()>0){
					long nextNpckey = WinnerManager.npc_keys.get(nextNpcIndex);
					Npc nextNpc = SceneNpcManager.selectNpcByKey(nextNpckey);
					String hufaName = nextNpc.getName();
					List<String> paras = MessageUtil.getMsgParaList(hufaName);
					// 发消息,让玩家到下一关
					for (xbean.Fighter fighter : fighters) {
						long roleid = fighter.getUniqueid();
						if (!TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID))
							continue;
						MessageMgr.psendMsgNotifyWhileCommit(roleid, 140682, npcbaseid, paras);
						WinnerManager.getInstance().refreshWinnerTask(WinnerManager.WINNER_TASK_ID, roleid, nextNpc, SpecialQuestState.UNDONE, nextStage, nextStage + 1);
					}
					winnerRecord.setTaskstartTime(curtime);//重新设置任务开始时间
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				WinnerManager.logger.error("冠军试炼战斗结束异常"+"\t队伍id"+teamid+"\t", e);
			}
		}
		return true;
	}
	


	@Override
	protected boolean handleDeath() {
		List<Fighter> deadFighters = super.getDeadRoles();
		for (Fighter fighter : deadFighters) {
			long roleid = fighter.getUniqueid();
			Role role = new RoleImpl(roleid);
			xbean.Properties prop = xtable.Properties.get(roleid);
			int maxmp = role.getMaxMp();
			int maxhp = role.getMaxHp();
			role.addHp(1);
			prop.setWound(0);
			role.fullMp();
			SRefreshRoleData sRefreshRoleData = new SRefreshRoleData();
			sRefreshRoleData.datas.put(AttrType.HP, (float) role.getHp());
			sRefreshRoleData.datas.put(AttrType.MP, (float) maxmp);
			sRefreshRoleData.datas.put(AttrType.UP_LIMITED_HP, (float) maxhp);
			Onlines.getInstance().send(roleid, sRefreshRoleData);
		}
		
		List<xbean.Fighter> roleFighters = getAllRoleFighters();
		for (xbean.Fighter fighter : roleFighters) {
			Map<Integer, Integer> petFighters = fighter.getFightedpets();
			for (Entry<Integer, Integer> petFighter : petFighters.entrySet()) {
				int petKey = petFighter.getKey();
				int petvalue = petFighter.getValue();
				long roleid = fighter.getUniqueid();
				if (((petvalue & ResultType.RESULT_DEATH) != 0 || (petvalue & ResultType.RESULT_KICKOUT) != 0 || (petvalue & ResultType.RESULT_GHOST) != 0)) {
					PetColumn petColumn = new PetColumn(roleid, PetColumnTypes.PET, false);
					Pet pet = petColumn.getPet(petKey);
					if (pet != null)
						fullPetHpMp(pet.getPetInfo(), roleid);
				}
			}
		}
		return true;
	}


	/**
	 * 每次完成任务获得的积分数，完成1次任务为1，好计算排行
	 * @return
	 */
	private int calCurrentStageScore() {
		return 1;
	}
}
