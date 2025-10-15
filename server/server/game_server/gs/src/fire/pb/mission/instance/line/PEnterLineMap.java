package fire.pb.mission.instance.line;

import java.util.ArrayList;
import java.util.List;

import fire.log.YYLogger;
import fire.msp.move.GEnterDynamicScene;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.item.EnterJingYingInstEvent;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.Transfer;
import fire.pb.mission.SLineTask;
import fire.pb.mission.SLineTaskConfig;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Lockeys;
import mkdb.Procedure;

public class PEnterLineMap extends Procedure {
	private final long teamId;
	private final Integer lineId;
	
	public PEnterLineMap(final long teamid, final Integer lineId) {
		this.teamId = teamid;
		this.lineId = lineId;
	}
	
	@Override
	protected boolean process() throws Exception {
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(lineId);
		if (taskCofig == null) {
			return false;
		}
		
		Team team = TeamManager.getTeamByTeamID(teamId);
		if (team == null)
			return false;
		
		final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
		roleids.addAll(team.getNormalMemberIds());
		this.lock(xtable.Locks.ROLELOCK, roleids);
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLEINSTANCETASK, roleids));
		//队长id
		final long roleid = team.getTeamLeaderId();
		
		if (!checkTask(team, taskCofig, roleids)) {
			LineInstManager.logger.error("队伍中有等级不足的情况," + lineId);
			return false;
		}
		
		long currentTime = System.currentTimeMillis();
		
		xbean.InstanceInfoCol teamLeadreInfoCol = xtable.Roleinstancetask.get(roleid);
		if (teamLeadreInfoCol == null) {
			return false;
		}
		
		//要进入的副本的id
		final int instanceZoneId = teamLeadreInfoCol.getLastinstanceid();
		xbean.InstanceTaskInfo teamLeadreTaskInfo = teamLeadreInfoCol.getInstinfo().get(instanceZoneId);
		if (teamLeadreTaskInfo == null) {
			return false;
		}
		
		//设置队长接副本任务的时间
		if (!DateValidate.inTheSameDay(teamLeadreInfoCol.getAccepttime(), currentTime)) {
			teamLeadreTaskInfo.getStepawardtimes().clear();
			teamLeadreTaskInfo.setCurrentstep(1);
			teamLeadreTaskInfo.setAccepttime(currentTime);
		}
		else {
			int step = LineInstManager.getStep(teamLeadreInfoCol, lineId);
			teamLeadreTaskInfo.setCurrentstep(step);
		}
		
		xbean.InstanceInfo instInfo = xbean.Pod.newInstanceInfo();
		long instanceOnlyId = xtable.Instancetask.insert(instInfo);
		//设置队长id
		instInfo.setOwnerid(roleid);
		
		if (teamLeadreTaskInfo.getCurrentstep() == 0) {
			teamLeadreTaskInfo.setCurrentstep(1);
		}
		
		//新建任务
		SLineTaskConfig config = LineInstManager.getInstance().getTaskConfig(taskCofig.id, teamLeadreTaskInfo.getCurrentstep(), 0);
		if (config == null) {
			LineInstManager.logger.error("没有找到任务 taskType =" + lineId + " step" + teamLeadreTaskInfo.getCurrentstep());
			return false;
		}
		
//		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(231);
//		int commonCnt = Integer.parseInt(common.getValue());

		for (long rid : roleids) {
			xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(rid);
			if (infoCol == null) {
				infoCol = xbean.Pod.newInstanceInfoCol();
				xtable.Roleinstancetask.insert(rid, infoCol);
			}

			xbean.InstanceTaskInfo taskInfo = infoCol.getInstinfo().get(lineId);
			if (taskInfo == null) {
				taskInfo = xbean.Pod.newInstanceTaskInfo();
				infoCol.getInstinfo().put(lineId, taskInfo);
			}
			
			if (taskCofig.afterid != 0) {
				xbean.InstanceTaskInfo afterTaskInfo = infoCol.getInstinfo().get(taskCofig.afterid);
				if (afterTaskInfo == null || afterTaskInfo.getState() < 2) {
					SLineTask afterTaskCofig = LineInstManager.getInstance().getInstanceTask(taskCofig.afterid);
					if (afterTaskCofig != null) {
						List<String> para = new ArrayList<String>(3);
						PropRole pRole = new PropRole(rid, true);
						para.add(pRole.getName());
						para.add(afterTaskCofig.taskname);
						para.add(taskCofig.taskname);
						for (Long mem : team.getNormalMemberIds()) {
							MessageMgr.psendMsgNotify(mem, 160439, para);
						}
					}
					return false;
				}
			}
			
			if (!DateValidate.inTheSameDay(infoCol.getAccepttime(), currentTime)) {
				infoCol.setCounts(0);
				infoCol.getInstcount().clear();
				infoCol.setAccepttime(currentTime);
			}
			
			if (!DateValidate.inTheSameDay(taskInfo.getAccepttime(), currentTime)) {
				taskInfo.setCounts(0); // 不在同一天则清0
				taskInfo.setCurrentstep(1);
				taskInfo.setCurrenttaskid(0);
				taskInfo.getStepawardtimes().clear();
				taskInfo.setAccepttime(currentTime);
			}
			
			infoCol.setLastinstanceid(lineId);
			taskInfo.setCurrentstep(teamLeadreTaskInfo.getCurrentstep());
			taskInfo.setCurrenttaskid(config.getTaskid());
			taskInfo.setInstanceonlyid(instanceOnlyId);

			// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
			taskInfo.setInstanceflag(4);
			taskInfo.setAwarding(1);

			// 运营日志
			YYLogger.acceptTaskLog(roleid, lineId);
			
			// 刷新推荐任务列表的次数
//			LineInstManager.refreshAcceptTask(rid, teamLeadreInfo.getCounts());
		}

		instInfo.getRoleids().addAll(roleids);
		instInfo.setInstanceid(lineId);
		instInfo.setCurrentstep(teamLeadreTaskInfo.getCurrentstep());
		instInfo.setCurrenttaskid(config.getTaskid());
		instInfo.setCurrentstate(SpecialQuestState.UNDONE);

		// 创建副本所有的动态地图
		List<Integer> mapList = LineInstManager.getInstance().getInstanceMapId(lineId);
		if (mapList == null)
			return false;

		for (Integer mapId : mapList) {
			// 创建动态地图
			DuplicateHelper.createDynamicMap(mapId, instanceOnlyId, "",
					true, -1, DynamicSceneType.NORMAL_SCENE, null);
		}
		

		
		// 进入副本
		int xpos = taskCofig.getXzuobiao();
		int ypos = taskCofig.getYzuobiao();
		if (!Transfer.canChangeMap(roleid, SRoleEnterScene.QUEST, 0))
			return false;
		
		for (long memid : roleids) {
			fire.pb.event.Poster.getPoster().dispatchEvent(new EnterJingYingInstEvent(memid));
		}

		GsClient.pSendWhileCommit(new GEnterDynamicScene(roleid, config.getMapid(), instanceOnlyId, "", xpos,
				ypos, (byte) 1, (int) LineInstManager.Time_Out,
				DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.ENTER_LINE_SCENCE,
				null));
		
//		xtable.Instanceaskinfos.remove(teamId);
		
		//如果有自动匹配,则删除
//		TeamManager.getInstance().delTeamMatch(roleid);
		return true;
	}
	
	private boolean checkTask(Team team, SLineTask taskCofig,
			List<Long> allRoleIDs) {
		// 验证等级问题
		List<String> levelException = new ArrayList<String>();
		for (Long mem : allRoleIDs) {
			PropRole pRole = new PropRole(mem, true);
			if (pRole.getLevel() < taskCofig.getMinlevel()) {
				levelException.add(pRole.getName());
			}
		}
		if (!levelException.isEmpty()) {
			StringBuffer strParam = new StringBuffer();
			int nIndex = 0;
			for (String name : levelException) {
				if (nIndex != (levelException.size() - 1)) {
					strParam.append(name).append(",");
				} else {
					strParam.append(name);
				}
			}
			
			List<String> paras = new ArrayList<String>();
			paras.add(strParam.toString());
			paras.add(String.valueOf(taskCofig.getMinlevel()));
			for (Long mem : allRoleIDs) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 160102, paras);
			}
			
			return false;
		}

		if (allRoleIDs.size() < taskCofig.getTeamnum()) {
			List<String> paras = new ArrayList<String>();
			paras.add(String.valueOf(taskCofig.getTeamnum()));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(team.getTeamLeaderId(), 160103,	paras);
			return false;
		}

		return true;
	}
}
