package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.msp.move.GEnterDynamicScene;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.mission.SAskIntoInstance;
import fire.pb.mission.SLandTask;
import fire.pb.mission.SLandTaskConfig;
import fire.pb.mission.instance.InstanceManager;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;


public class PReqInstanceTask extends Procedure {

	private final long roleid;

	
	private final int instanceZoneId;

	boolean fromAsk = false;

	public PReqInstanceTask(long roleid, int taskId, int fightType) {
		this.roleid = roleid;
		this.instanceZoneId = taskId;
		fromAsk = false;
	}

	
	public PReqInstanceTask(long roleid, int taskType, int fightType,
			boolean fromAsk) {
		this.roleid = roleid;
		this.instanceZoneId = taskType;
		this.fromAsk = fromAsk;
	}

	@Override
	protected boolean process() throws Exception {
		SLandTask taskCofig = InstanceManager.getInstance().getInstanceTask(instanceZoneId);
		if (taskCofig == null) {
			InstanceManager.logger.error("副本数据为空" + instanceZoneId);
			return false;
		}

		if (!InstanceManager.getInstance().getInstanceSet(instanceZoneId)) {
			InstanceManager.logger.error("副本还没有开启 instanceState");
			return false;
		}

		// 判断等级
		PropRole pRole = new PropRole(roleid, true);
		if (pRole.getLevel() < taskCofig.getMinlevel()) {
			List<String> paras = new ArrayList<String>();
			paras.add(String.valueOf(taskCofig.getMinlevel()));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160100,
					paras);
			return false;
		}
		
		//判断是否在队伍中
		Team team = TeamManager.getTeamByRoleId(roleid);
		

		Role mapRole = RoleManager.getInstance().getRoleByID(roleid);
		if (null == mapRole) {
			InstanceManager.logger.error("角色不在场景内," + instanceZoneId);
			return false;
		}
		if (mapRole.getMapId() != InstanceManager.ZILUOLAN_MAP) {
			InstanceManager.logger.error("角色所在地图有问题," + instanceZoneId);
			return false;
		}

		
		List<Long> allRoleIDs = new LinkedList<Long>();
		if (taskCofig.getTeamnum() > 1) {
			// 需要组队
			if (team == null) {
				List<String> paras = new ArrayList<String>(1);
				paras.add(String.valueOf(taskCofig.getTeamnum()));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160101, paras);
				return false;
			}

			if (team.getTeamLeaderId() == roleid) {
				allRoleIDs.addAll(team.getNormalMemberIds());
			} else {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160191, null);
				return false;
			}

			if (!checkTask(team, taskCofig, allRoleIDs)) {
				InstanceManager.logger.error("队伍中有等级不足的情况," + instanceZoneId);
				return false;
			}
			
			xbean.InstanceAskInfo askInst = xtable.Instanceaskinfos.get(team.getTeamId());
			if (askInst != null) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160363, null);
				return false;
			}
		} 
		else {
			// 不需要组队
			if (team != null) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,
						144816, null);
				return false;
			}
			allRoleIDs.add(roleid);
		}

		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
		
		xbean.InstanceInfoCol teamLeadreInfoCol = null;
		xbean.InstanceTaskInfo teamLeadreTaskInfo = null;

		teamLeadreInfoCol = xtable.Roleinstancetask.get(roleid);
		if (teamLeadreInfoCol == null) {
			teamLeadreInfoCol = xbean.Pod.newInstanceInfoCol();
			xtable.Roleinstancetask.insert(roleid, teamLeadreInfoCol);
		}

		teamLeadreTaskInfo = teamLeadreInfoCol.getInstinfo().get(instanceZoneId);
		if (teamLeadreTaskInfo == null) {
			teamLeadreTaskInfo = xbean.Pod.newInstanceTaskInfo();
			teamLeadreInfoCol.getInstinfo().put(instanceZoneId, teamLeadreTaskInfo);
		}
		
		long instanceOnlyId = teamLeadreTaskInfo.getInstanceonlyid();
		teamLeadreInfoCol.setLastinstanceid(instanceZoneId);
		
		// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
		int instState = teamLeadreTaskInfo.getInstanceflag();
		if (instState != 2 && instState != 4) { //新建任务
			SLandTaskConfig config = InstanceManager.getInstance().getTaskConfig(taskCofig.instancezoneid, 1, 0);
			if (config == null) {
				InstanceManager.logger.error("没有找到任务 taskType =" + instanceZoneId + " 1, 0");
				return false;
			}
			xbean.InstanceInfo instInfo = xbean.Pod.newInstanceInfo();
			instanceOnlyId = xtable.Instancetask.insert(instInfo);
			teamLeadreTaskInfo.setInstanceonlyid(instanceOnlyId);
			teamLeadreTaskInfo.setCurrentstep(1);
			
			instInfo.getRoleids().addAll(allRoleIDs);
			instInfo.setCopyflag(2);
			instInfo.setInstanceid(instanceZoneId);
			instInfo.setCurrentstep(1);
			instInfo.setCurrenttaskid(config.getTaskid());
			instInfo.setCurrentstate(SpecialQuestState.UNDONE);
		}
		else { //同步队长以前的进度
			xbean.InstanceInfo oldInstInfo = xtable.Instancetask.select(instanceOnlyId);
			if (oldInstInfo == null) {
				xbean.InstanceInfo instInfo = xbean.Pod.newInstanceInfo();
				instanceOnlyId = xtable.Instancetask.insert(instInfo);
				teamLeadreTaskInfo.setInstanceonlyid(instanceOnlyId);
				
			} else if (!oldInstInfo.getRoleids().contains(roleid)) {
				xbean.InstanceInfo instInfo = xbean.Pod.newInstanceInfo();
				instanceOnlyId = xtable.Instancetask.insert(instInfo);
				teamLeadreTaskInfo.setInstanceonlyid(instanceOnlyId);
			}
			
			xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceOnlyId);
			instInfo.getRoleids().clear();
			
			//设置主副本信息
			instInfo.getRoleids().addAll(allRoleIDs);
			instInfo.setCopyflag(2);
			instInfo.setInstanceid(instanceZoneId);
			if (teamLeadreTaskInfo.getCurrentstep() == 0) {
				instInfo.setCurrentstep(1);
			}
			else {
				instInfo.setCurrentstep(teamLeadreTaskInfo.getCurrentstep());
			}
			
			instInfo.setCurrenttaskid(teamLeadreTaskInfo.getCurrenttaskid());
		}
		
		if (team != null) {
			xbean.InstanceAskInfo askInfo = xtable.Instanceaskinfos.get(team.getTeamId());
			if (askInfo == null) {
				askInfo = xbean.Pod.newInstanceAskInfo();
				xtable.Instanceaskinfos.insert(team.getTeamId(), askInfo);
			}
			
			xbean.InstanceInfo instTaskInfo = xtable.Instancetask.select(instanceOnlyId);
			if (instTaskInfo == null)
				return false;
			
			//TODO 此处需要加剩余奖励次数
			SAskIntoInstance askInto = null;
			if (instTaskInfo.getCurrentstep() > 1) {
				askInto = new SAskIntoInstance(160223, taskCofig.taskname, 0, instTaskInfo.getCurrentstep(), 1, 1, 5, null, 0, (short)0);
			}
			else {
				askInto = new SAskIntoInstance(160210, taskCofig.taskname, 0, instTaskInfo.getCurrentstep(), 1, 1, 5, null, 0, (short)0);
			}
			//向每个队员询问是否要进入副本
			for (Long rid : allRoleIDs) {
				if (rid != team.getTeamLeaderId()) {
					askInfo.getAnswerinfo().put(rid, 0);
					mkdb.Procedure.psendWhileCommit(rid, askInto);
				}
			}
			
			//启动定时器,10秒如果没有人应答,自动进入副本
//			PEnterInstanceMap enterInst = new PEnterInstanceMap(team.getTeamId(), instanceZoneId);
			ScheduledFuture<?> future = mkdb.Executor.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					new mkdb.Procedure() {
						@Override
						protected boolean process() throws Exception {
							//此处必须进行一次删除,防止有些队员没有点确认,自动进入时没有删除该记录
							xtable.Instanceaskinfos.remove(team.getTeamId());
							return true;
						}
						
					}.submit();
//					enterInst.submit();
				}
			}, 25, TimeUnit.SECONDS);
			
			InstanceManager.askInstFuture.put(team.getTeamId(), future);
		}
		else {
			// 进入副本
			int xpos = taskCofig.getXzuobiao();
			int ypos = taskCofig.getYzuobiao();
			if (!Transfer.canChangeMap(roleid, SRoleEnterScene.QUEST, 0))
				return false;

			xbean.InstanceInfo instInfo = xtable.Instancetask.select(instanceOnlyId);

			SLandTaskConfig config = InstanceManager.getInstance().getTaskConfig(
					taskCofig.instancezoneid, instInfo.getCurrentstep(), 0);
			if (config == null) {
				InstanceManager.logger.error("没有找到任务 taskType =" + instanceZoneId
						+ "   1, 0");
				return false;
			}

			GsClient.pSendWhileCommit(new GEnterDynamicScene(roleid, config.getMapid(), instanceOnlyId, "", xpos,
					ypos, (byte) 1, (int) InstanceManager.Time_Out,
					DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.INSTANCE,
					null));
		}
		
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
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160102,
					paras);
			return false;
		}

		if (allRoleIDs.size() < taskCofig.getTeamnum()) {
			List<String> paras = new ArrayList<String>();
			paras.add(String.valueOf(taskCofig.getTeamnum()));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160103,
					paras);
			return false;
		}

		return true;
	}
}
