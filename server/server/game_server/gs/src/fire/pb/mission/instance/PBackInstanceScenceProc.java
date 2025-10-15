package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.List;

import fire.msp.move.GEnterDynamicScene;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.Transfer;
import fire.pb.mission.SLandTask;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.SceneManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import fire.pb.util.MessageUtil;
import fire.pb.util.TaskDlgUtil;
import fire.pb.util.TaskDlgUtil.NpcModel;
import mkdb.Procedure;


public class PBackInstanceScenceProc extends Procedure {

	private final long roleId;
	private final int posx;
	private final int posy;

	public PBackInstanceScenceProc(long roleId) {
		this.roleId = roleId;
		posx = 0;
		posy = 0;
	}

	public PBackInstanceScenceProc(long roleId, int x, int y) {
		this.roleId = roleId;
		posx = x;
		posy = y;
	}

	@Override
	protected boolean process() throws Exception {
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if (team == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 145123, null);
			return false;
		}
		
		if (team.isReturnMember(roleId)) {
			return false;
		}
		
		List<Long> list = new ArrayList<Long>();
		list.addAll(team.getAllMemberIds());
		//队长副本信息
		xbean.InstanceInfoCol teamLeaderInfoCol = xtable.Roleinstancetask.get(team.getTeamLeaderId());
		if (teamLeaderInfoCol == null) {
			return false;
		}
		xbean.InstanceTaskInfo teamLeaderInfo = teamLeaderInfoCol.getInstinfo().get(
				teamLeaderInfoCol.getLastinstanceid());
		if (teamLeaderInfo == null)
			return false;
		
		long instanceTaskid = teamLeaderInfo.getInstanceonlyid();
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceTaskid);
		if (instInfo == null) {
			return false;
		}
		
		int taskType = instInfo.getInstanceid();
		int mapId = InstanceManager.getInstance().getMapIdByTaskType(
				taskType, instInfo.getCurrentstep());
		
		SLandTask taskCofig = InstanceManager.getInstance().getInstanceTask(taskType);
		if (taskCofig != null) {
			if (!checkLevel(list, taskCofig)) {
				return false;
			}
		}
		
		//同步队员任务信息
		int instanceZoneId = teamLeaderInfoCol.getLastinstanceid();
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.get(roleId);
		if (taskInfo == null) {
			taskInfo = xbean.Pod.newInstanceInfoCol();
			xtable.Roleinstancetask.insert(roleId, taskInfo);
		}

		xbean.InstanceTaskInfo instanceInfo = taskInfo.getInstinfo().get(instanceZoneId);
		if (instanceInfo == null) {
			instanceInfo = xbean.Pod.newInstanceTaskInfo();
			taskInfo.getInstinfo().put(instanceZoneId, instanceInfo);
		}
		taskInfo.setLastinstanceid(instanceZoneId);
		final long currentTime = System.currentTimeMillis();
		if (!DateValidate.inTheSameDay(taskInfo.getAccepttime(), currentTime)) {
			taskInfo.setCounts(0);
			taskInfo.setAccepttime(currentTime);
		}

		int defaultTimes = InstanceManager.getInstance().getDayMaxTimes(roleId);
		if (!DateValidate.inTheSameDay(instanceInfo.getAccepttime(),
				teamLeaderInfo.getAccepttime())) {
			instanceInfo.setCounts(0); // 不在同一天则清0
			instanceInfo.setDefaulttimes(defaultTimes); // 设置一天的默认次数
			instanceInfo.setCurrentstep(0);
			instanceInfo.setCurrenttaskid(0);
		}
		
		taskInfo.setAccepttime(teamLeaderInfoCol.getAccepttime());

		instanceInfo.setAccepttime(teamLeaderInfo.getAccepttime());
		instanceInfo.setInstanceonlyid(teamLeaderInfo.getInstanceonlyid());
		instanceInfo.setCurrentstep(teamLeaderInfo.getCurrentstep());
		instanceInfo.setCurrenttaskid(teamLeaderInfo.getCurrenttaskid());

		// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
		instanceInfo.setInstanceflag(teamLeaderInfo.getInstanceflag());
		
		int questid = instInfo.getCurrenttaskid();
		instInfo.getRoleids().add(roleId);
		xbean.InstanceNpcInfo info = null;
		if (instInfo.getNpcs().size() > 0) {
			info = instInfo.getNpcs().get(0);
		}

		if (info == null) {
			InstanceManager.logger.info("Instance info == null   mission.roleids=:"
					+ instInfo.getRoleids() + "taskid:" + questid);
			return false;
		}
		NpcModel npcModel = TaskDlgUtil.createNpcModel(info.getNpckey(),
				info.getNpcid(), info.getMapid(), info.getPosx(),
				info.getPosy());
		
		TaskDlgUtil.createAndRefreshTaskDlg(
				roleId,
				questid,
				npcModel,
				1,
				1,
				SpecialQuestState.DONE,
				instanceInfo.getAccepttime() + InstanceManager.Time_Out
						- System.currentTimeMillis());
		
		//检测场景是否还在,如果不在,不能进入
		DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(mapId, instanceTaskid);
		if (ds == null)
			return false;

		// 查看能否跳地图
		if (!Transfer.canChangeMap(roleId, SRoleEnterScene.QUEST, 0)) {
			return false;
		}

		GsClient.pSendWhileCommit(new GEnterDynamicScene(roleId, mapId,
				instanceTaskid, "", posx, posy, (byte) 1,
				(int) InstanceManager.Time_Out,
				DynamicSceneType.NORMAL_SCENE,
				SRoleEnterScene.INSTANCE, null));
		
//		refreshTaskState(roleId, questid, SpecialQuestState.UNDONE);
		return true;
	}

	private boolean checkLevel(List<Long> allRoleIDs, SLandTask taskCofig) {
		// 验证等级问题
		List<Long> levelException = new ArrayList<Long>();
		for (Long mem : allRoleIDs) {
			PropRole pRole = new PropRole(mem, true);
			int level = pRole.getLevel();
			if (level < taskCofig.getMinlevel()
					|| level > taskCofig.getMaxlevel()) {
				levelException.add(mem);
			}
		}
		if (!levelException.isEmpty()) {
			List<String> paras = MessageUtil.getMsgParaList(MessageUtil
					.getRoleNames(levelException));
			paras.add(taskCofig.getMinlevel() + "");
			for (long rid : allRoleIDs) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(rid, 143221,
						0, paras);
			}
			return false;
		}

		return true;
	}
	
	

}
