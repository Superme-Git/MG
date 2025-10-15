package fire.pb.mission;

import java.util.ArrayList;
import java.util.List;

import fire.log.YYLogger;
import fire.pb.PSetSysConfigOne;
import fire.pb.SysConfigType;
import fire.pb.course.CourseManager;
import fire.pb.mission.MissionExeTypes;
import fire.pb.mission.MissionFinTypes;
import fire.pb.mission.MissionStatus;
import fire.pb.mission.util.EMissionCommit;
import fire.pb.mission.util.IMissionCommitParam;
import fire.pb.mission.util.NullCommitParam;
import fire.pb.team.TeamManager;
import fire.pb.triggers.TriggerRole;

public class PCommitMajorMission extends PMissionProc {
	final long roleid;
	final int taskid;
	private final boolean force;
	public PCommitMajorMission(long roleid, int taskid, boolean isCapital) {
		this(roleid, taskid, new NullCommitParam(), isCapital, false);
	}
	public PCommitMajorMission(long roleid, int taskid, boolean isCapital, boolean force) {
		this(roleid, taskid, new NullCommitParam(), isCapital, force);
	}
	public PCommitMajorMission(long roleid, int taskid, IMissionCommitParam param, boolean isCapital) {
		this(roleid, taskid, param, isCapital, false);
	}
	public PCommitMajorMission(long roleid, int taskid, IMissionCommitParam param, boolean isCapital,
			boolean force) {
		this.roleid = roleid;
		this.taskid = taskid;
		this.commitparam = param;
		this.isCapital = isCapital;
		this.force = force;
	}
	boolean isCapital;
	IMissionCommitParam commitparam;
	private void lockdata() {
		if (isCapital) {
			//MissionConfig conf = MissionManager.getInstance().getTaskConfig(taskid);
			//if (conf != null && conf.hasGoto()) {
				TeamManager.getTeamByRoleId(roleid);
			//}
		}
	}
	@Override
	protected boolean missionExecute() {
		/*
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			boolean ok = fire.pb.fushi.DayPayManager.getInstance().CheckDayPayWithLevel(roleid);
			if (ok == false)
			{
				Module.logger.info("PCommitMajorMission:角色[" + roleid + "]任务不能提交-nodaypay:" + taskid);
				return false;
			}
		}
		*/	
		
		lockdata();
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null) {
			throw new NullPointerException("角色[" + roleid + "]的属性没有找到");
		}
		
		final RoleMission mission = new MissionColumn(roleid, false).getMission(taskid);
		if (mission == null) {
			Module.logger.error("角色[" + roleid + "]任务并未完成:" + taskid);
			YYLogger.completeTaskLog(roleid, taskid, YYLogger.FAIL_FLAG, "任务并未完成");
			return false;
		}
		if (!force && mission.getState() != MissionStatus.FINISHED) {
			if (!mission.isTaskFinished()) {
				sendItemNotify(mission.conf);
				Module.logger.error("角色[" + roleid + "]任务并未完成:" + taskid);
				YYLogger.completeTaskLog(roleid, taskid, YYLogger.FAIL_FLAG, "任务并未完成");
				return false;
			} else {
				mission.data.setStatus(MissionStatus.FINISHED);
			}
		}
		Integer initvalue = null;
		
		if (!new PMissionCommit(roleid, mission.getId()) {
			@Override
			protected MissionReward commitExecute() {
				return mission.commit(commitparam, isCapital);
			}
		}.call()) {
			Module.logger.error("角色[" + roleid + "]任务完成事物call失败:" + taskid);
			YYLogger.completeTaskLog(roleid, taskid, YYLogger.FAIL_FLAG, "任务完成事物call失败");
			return false;
		}
		int next = mission.getNext(commitparam);
		final int nextMissionID = mission.getNextTask(next);
		if (UtilHelper.getMissionStep(nextMissionID) != UtilHelper.getLastStep()) {
			final MissionConfig nextconf = MissionManager.getInstance().getMissionConfig(nextMissionID);
			if (nextconf == null)
				return true;
			if (UtilHelper.getMissionFinType(nextconf.exeIndo.missionType) == MissionFinTypes.LEVEL &&
					UtilHelper.getMissionExeType(nextconf.exeIndo.missionType) == MissionExeTypes.NUL){
				//下一个任务是等级任务不判断等级，直接接取
				if (!new PAcceptMajorMission(roleid, nextMissionID, isCapital,false,mission.data.getRound(), initvalue).call()) {
					Module.logger.error("角色[" + roleid + "]任务 [" + taskid +"]完成,接受后续任务处理失败:" + nextMissionID);
					YYLogger.completeTaskLog(roleid, taskid, YYLogger.FAIL_FLAG, "任务完成接受后续任务处理失败");
					return false;
				}
				Module.logger.info("角色[" + roleid + "]任务 [" + taskid +"]完成,接受了后续任务:" + nextMissionID);
			} else {
				//下一个任务是其他任务判断等级，等级达到才从其他表里接
				if (PAcceptMajorMission.canAcceptTheTask(roleid, nextMissionID)) {
					if (!new PAcceptMajorMission(roleid, nextMissionID, isCapital,false,mission.data.getRound(), initvalue).call()) {
						Module.logger.error("角色[" + roleid + "]任务[" + taskid +"]完成,接受后续任务处理失败:" + nextMissionID);
						YYLogger.completeTaskLog(roleid, taskid, YYLogger.FAIL_FLAG, "任务完成接受后续任务处理失败");
						return false;
					}
					Module.logger.info("角色[" + roleid + "]任务[" + taskid +"]完成,接受了后续任务:" + nextMissionID);
					TriggerRole trole = new TriggerRole(roleid, false);
					trole.trigger4taskcom(taskid);
				} else {
					final int taskline = UtilHelper.getMissionLineid(mission.getId());
					if (UtilHelper.isBranchScenarioMission(mission.getId())) {
						new MissionColumn(roleid, false).removeMissionline(taskline);
						Module.logger.info("角色[" + roleid + "],等级不符合下个支线任务:" + nextMissionID + ",移除当前支线任务线");
					}
					Module.logger.error("角色[" + roleid + "],等级不符合,任务接取失败:" + nextMissionID);
				}
			}
		} else {
			if (!mission.onMissionLineCompleted()) {
				Module.logger.error("角色[" + roleid + "]任务[" + taskid +"]完成,任务线完成处理失败");
				YYLogger.completeTaskLog(roleid, taskid, YYLogger.FAIL_FLAG, "任务线完成处理失败");
				return false;
			}
		}
		//对这单条任务完成进行记录，以便等级触发任务接取与否的判断
		xbean.CommitedMissions ct = xtable.Commitedmission.get(roleid);
		if (ct == null) {
			ct = xbean.Pod.newCommitedMissions();
			xtable.Commitedmission.insert(roleid, ct);
		}
		if(!ct.getCommitted().contains(Integer.valueOf(taskid))){
			ct.getCommitted().add(Integer.valueOf(taskid));
		}
		
		YYLogger.completeTaskLog(roleid, taskid, YYLogger.SUCC_FLAG, "");
		
		UtilHelper.clearcruisetask(roleid, mission.getConf().exeIndo.cruiseID, mission.getConf().missionId);
		
		int funcTrigTask = 0;
		//技能新功能开启
		if(MissionManager.getInstance().isNewFuncOpen(4, taskid)){
			new PSetSysConfigOne(roleid, SysConfigType.skillopen, 1).call();
			funcTrigTask = MissionManager.getInstance().getNewFuncOpenTrigger(4);
			if(funcTrigTask > 0)
				new PAcceptMajorMission(roleid, funcTrigTask).call();
			Module.logger.info( "技能新功能开启 roleid:" + roleid + " taskid:" + taskid );
		}
		
		//帮会新功能开启
		if(MissionManager.getInstance().isNewFuncOpen(5, taskid)){
			new PSetSysConfigOne(roleid, SysConfigType.factionopen, 1).call();
			funcTrigTask = MissionManager.getInstance().getNewFuncOpenTrigger(5);
			if(funcTrigTask > 0)
				new PAcceptMajorMission(roleid, funcTrigTask).call();
			Module.logger.info( "帮会新功能开启 roleid:" + roleid + " taskid:" + taskid );
		}
		
		//宠物新功能开启
		if(MissionManager.getInstance().isNewFuncOpen(1, taskid)){
			new PSetSysConfigOne(roleid, SysConfigType.petopen, 1).call();
			funcTrigTask = MissionManager.getInstance().getNewFuncOpenTrigger(1);
			if(funcTrigTask > 0)
				new PAcceptMajorMission(roleid, funcTrigTask).call();
			Module.logger.info( "宠物新功能开启 roleid:" + roleid + " taskid:" + taskid );
		}
		
		//助战新功能开启
		if(MissionManager.getInstance().isNewFuncOpen(6, taskid)){
			new PSetSysConfigOne(roleid, SysConfigType.patopen, 1).call();
			new fire.pb.huoban.PInitHuobanInfo(roleid).call();
			funcTrigTask = MissionManager.getInstance().getNewFuncOpenTrigger(6);
			if(funcTrigTask > 0)
				new PAcceptMajorMission(roleid, funcTrigTask).call();
			Module.logger.info( "助战新功能开启 roleid:" + roleid + " taskid:" + taskid );
		}
		
		//挂机新功能开启
		if(MissionManager.getInstance().isNewFuncOpen(7, taskid)){
			new PSetSysConfigOne(roleid, SysConfigType.guajiopen, 1).call();
			funcTrigTask = MissionManager.getInstance().getNewFuncOpenTrigger(7);
			if(funcTrigTask > 0)
				new PAcceptMajorMission(roleid, funcTrigTask).call();
			Module.logger.info( "挂机新功能开启 roleid:" + roleid + " taskid:" + taskid );
		}
		
		//指引新功能开启
		if(MissionManager.getInstance().isNewFuncOpen(2, taskid)){
			new PSetSysConfigOne(roleid, SysConfigType.zhiyinopen, 1).call();
			funcTrigTask = MissionManager.getInstance().getNewFuncOpenTrigger(2);
			if(funcTrigTask > 0)
				new PAcceptMajorMission(roleid, funcTrigTask).call();
			Module.logger.info( "指引新功能开启 roleid:" + roleid + " taskid:" + taskid );
		}
		
		//活动新功能开启
		if(MissionManager.getInstance().isNewFuncOpen(14, taskid)){
			new PSetSysConfigOne(roleid, SysConfigType.huodongopen, 1).call();
			funcTrigTask = MissionManager.getInstance().getNewFuncOpenTrigger(14);
			if(funcTrigTask > 0)
				new PAcceptMajorMission(roleid, funcTrigTask).call();
			Module.logger.info( "活动新功能开启 roleid:" + roleid + " taskid:" + taskid );
		}
		
		CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.TASK_COURSE, taskid, 0);
		
		return true;
	}
	
	private void sendItemNotify(MissionConfig conf) {
		final int tasktype = UtilHelper.getMissionFinType(conf.exeIndo.missionType);
		switch (tasktype) {
		case MissionFinTypes.ITEM: {
			if (commitparam.getCommitType() == EMissionCommit.SIMPLEITEM) {
				fire.pb.item.ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(
						conf.exeIndo.targetID);
				if (attr != null) {
					List<String> parameters = new ArrayList<String>();
					parameters.add(xtable.Properties.selectRolename(roleid));
					parameters.add(attr.name);
					fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
					if (team != null) {
						for (long memid : team.getNormalMemberIds())
							fire.pb.talk.MessageMgr.psendMsgNotify(memid, 143911, parameters);
					}
				}
				return;
			}
			final int exetype = UtilHelper.getMissionExeType(conf.exeIndo.missionType);
			if (exetype == MissionExeTypes.USE) { 
				if (!isCapital && conf.aiInfo.aIID == 0) {
					fire.pb.item.ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(
							conf.exeIndo.useItemID);
					if (attr == null)
						return;
					if (attr.useup == 1) {
						List<String> parameters = new ArrayList<String>();
						parameters.add(xtable.Properties.selectRolename(roleid));
						parameters.add(attr.name);
						fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
						if (team != null) {
							for (long memid : team.getNormalMemberIds())
								fire.pb.talk.MessageMgr.psendMsgNotify(memid, 143911, parameters);
						}
						return;
					}
				}
				break;
			}
			}
		}
	}
}
