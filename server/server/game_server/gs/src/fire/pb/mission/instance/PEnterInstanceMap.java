package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.List;

import fire.msp.move.GEnterDynamicScene;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.Transfer;
import fire.pb.mission.SLandTask;
import fire.pb.mission.SLandTaskConfig;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Lockeys;
import mkdb.Procedure;

public class PEnterInstanceMap extends Procedure {
	private final long teamId;
	private final int instanceZoneId;
	
	public PEnterInstanceMap(final long teamid, final int instanceZoneId) {
		this.teamId = teamid;
		this.instanceZoneId = instanceZoneId;
	}
	
	@Override
	protected boolean process() throws Exception {
		SLandTask taskCofig = InstanceManager.getInstance().getInstanceTask(instanceZoneId);
		if (taskCofig == null) {
			InstanceManager.logger.error("副本数据为空" + instanceZoneId);
			return false;
		}
		
		Team team = TeamManager.selectTeamByTeamID(teamId);
		if (team == null)
			return false;
		
		//队长id
		final long roleid = team.getTeamLeaderId();
		
		final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
		roleids.addAll(team.getAllMemberIds());
		
		this.lock(xtable.Locks.ROLELOCK, roleids);
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLEINSTANCETASK, roleids));
		
		if (!checkTask(team, taskCofig, roleids)) {
			InstanceManager.logger.error("队伍中有等级不足的情况," + instanceZoneId);
			return false;
		}
		
		xbean.InstanceInfoCol teamLeadreInfoCol = xtable.Roleinstancetask.select(roleid);
		if (teamLeadreInfoCol == null) {
			return false;
		}
		
		//要进入的副本的id
		final int instanceZoneId = teamLeadreInfoCol.getLastinstanceid();
		xbean.InstanceTaskInfo teamLeadreTaskInfo = teamLeadreInfoCol.getInstinfo().get(instanceZoneId);
		if (teamLeadreTaskInfo == null) {
			return false;
		}
		
		teamLeadreInfoCol.setLastinstanceid(instanceZoneId);

		long currentTime = System.currentTimeMillis();
		int defaultTimes = InstanceManager.getInstance().getDayMaxTimes(roleid);

		teamLeadreInfoCol.setAccepttime(currentTime);
		teamLeadreTaskInfo.setAccepttime(currentTime);
		teamLeadreTaskInfo.setTotalexp(0);// 每次要清0
		
		long instanceOnlyId = teamLeadreTaskInfo.getInstanceonlyid();
		SLandTaskConfig config = InstanceManager.getInstance().getTaskConfig(taskCofig.instancezoneid, 1, 0);
		if (config == null) {
			InstanceManager.logger.error("没有找到任务 taskType =" + instanceZoneId + " 1, 0");
			return false;
		}
		
		// 同步队长进度到队员身上
		for (long rid : roleids) {
			xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.get(rid);
			if (taskInfo == null) {
				taskInfo = xbean.Pod.newInstanceInfoCol();
				xtable.Roleinstancetask.insert(rid, taskInfo);
			}
			
			if (!DateValidate.inTheSameDay(taskInfo.getAccepttime(), currentTime)) {
				taskInfo.setCounts(0);
				taskInfo.setAccepttime(currentTime);
			}

			xbean.InstanceTaskInfo instanceInfo = taskInfo.getInstinfo().get(instanceZoneId);
			if (instanceInfo == null) {
				instanceInfo = xbean.Pod.newInstanceTaskInfo();
				taskInfo.getInstinfo().put(instanceZoneId, instanceInfo);
			}
			taskInfo.setLastinstanceid(instanceZoneId);

			if (!DateValidate.inTheSameDay(instanceInfo.getAccepttime(),
					teamLeadreTaskInfo.getAccepttime())) {
				instanceInfo.setCounts(0); // 不在同一天则清0
				taskInfo.setCounts(0);
				instanceInfo.setDefaulttimes(defaultTimes); // 设置一天的默认次数
				instanceInfo.setCurrentstep(1);
				instanceInfo.setCurrenttaskid(0);
				instanceInfo.setState(0);
				instanceInfo.getStepawardtimes().clear();
			}

			instanceInfo.setAccepttime(teamLeadreTaskInfo.getAccepttime());
			instanceInfo.setInstanceonlyid(instanceOnlyId);
			instanceInfo.setCurrentstep(teamLeadreTaskInfo.getCurrentstep());
			instanceInfo.setCurrenttaskid(config.getTaskid());

			// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
			instanceInfo.setInstanceflag(4);
//			Long instId = xtable.Role2instancetask.select(rid);
//			if (instId == null) {
//				xtable.Role2instancetask.insert(rid, instanceOnlyId);
//			}
//			else {
//				if (instId != instanceOnlyId) {
//					xtable.Role2instancetask.remove(rid);
//					xtable.Role2instancetask.insert(rid, instanceOnlyId);
//				}
//			}

			// 刷新推荐任务列表的次数
//			InstanceManager.refreshAcceptTask(rid, teamLeadreInfo.getCounts());
		}
		
		// 创建副本所有的动态地图
		List<Integer> mapList = InstanceManager.getInstance().getInstanceMapId(instanceZoneId);
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

		GsClient.pSendWhileCommit(new GEnterDynamicScene(roleid, config.getMapid(), teamLeadreTaskInfo.getInstanceonlyid(), "", xpos,
				ypos, (byte) 1, (int) InstanceManager.Time_Out,
				DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.INSTANCE,
				null));
		
		xtable.Instanceaskinfos.remove(teamId);
		
		//如果有自动匹配,则删除
		TeamManager.getInstance().delTeamMatch(roleid);
		
		return true;
	}
	
	private boolean checkTask(Team team, SLandTask taskCofig,
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
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(team.getTeamLeaderId(), 160102,
					paras);
			return false;
		}

		if (allRoleIDs.size() < taskCofig.getTeamnum()) {
			List<String> paras = new ArrayList<String>();
			paras.add(String.valueOf(taskCofig.getTeamnum()));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(team.getTeamLeaderId(), 160103,
					paras);
			return false;
		}

		return true;
	}

}
