package fire.pb.mission;

import fire.log.YYLogger;
import fire.pb.mission.MissionStatus;
import fire.pb.team.PAbsentReturnTeam;

public class PAcceptMajorMission extends PMissionProc {
	private final long roleid;
	private final int taskid;
	private final boolean isCapital;
	private final boolean isGM;
	private final int round;
	private Integer initvalue;
	public PAcceptMajorMission(long roleid, int taskid) {
		this(roleid, taskid, true);
	}
	public PAcceptMajorMission(long roleid, int taskid, boolean isCapital) {
		this(roleid, taskid, isCapital, false);
	}
	public PAcceptMajorMission(long roleid, int taskid, boolean isCapital, boolean isGM) {
		this(roleid, taskid, isCapital,isGM,0);
	}
	public PAcceptMajorMission(long roleid, int taskid, boolean isCapital, boolean isGM,int round) {
		this(roleid, taskid, isCapital, isGM, round, null);
	}
	public PAcceptMajorMission(long roleid, int taskid, boolean isCapital, boolean isGM,int round, Integer initvalue) {
		this.roleid = roleid;
		this.taskid = taskid;
		this.isCapital = isCapital;
		this.isGM = isGM;
		this.round = round;
		this.initvalue = initvalue;
	}
	
	protected void lockData(final MissionConfig conf) {
	}
	
	@Override
	protected boolean missionExecute() {
		MissionConfig conf = MissionManager.getInstance().getMissionConfig(taskid);
		if (conf == null){
			Module.logger.error("mission conf is null.taskid:"+taskid);
			return false;
		}
		lockData(conf);
		if (!isGM && !checkCanAccept(roleid, taskid)) {
			return false;
		}

		final int lineid = UtilHelper.getMissionLineid(taskid);
		final RoleMission mission = RoleMission.getScenarioTaskInstance(roleid, lineid);
		if (mission == null) {
			return false;
		}
		
		if (!new PMissionAccept(roleid, taskid) {
				@Override
				protected boolean acceptExecute() {
					return mission.accept(missionid,round, initvalue);
				}
			}.call()) {
			Module.logger.error("角色[" + roleid + "]任务接取事物call失败:" + taskid);
			return false;
		}
		if (mission.getState() == MissionStatus.FINISHED) {
			if (mission.isAutoCommit()) {
				if (!new PCommitMajorMission(roleid, mission.getId(), isCapital).call()) {
					Module.logger.error("角色[" + roleid + "]自动完成任务call失败:" + mission.getId());
					return false;
				}
				return true;
			}
		}
		Module.logger.info("角色[" + roleid + "]接受任务成功:" + taskid);

		if(conf.exeIndo.cruiseID <= 0){
			SAcceptMission send = mission.toProtocol();
			mkdb.Transaction.tsendWhileCommit(roleid, send);
			transform(mission.conf);
			npcfollowme(mission.conf);
		} else {
			//cruisetask(mission.conf, mission);
			Module.logger.info("角色[" + roleid + "]巡游任务[" + taskid + "]巡游id[" + conf.exeIndo.cruiseID + "]转到队伍处理后在下发任务消息!");
			PCruiseTask4Team proc = new PCruiseTask4Team(roleid, taskid, mission);
			pexecuteWhileCommit(proc);
		}
		

		// 运营日志
		YYLogger.acceptTaskLog(roleid, taskid);
		return true;
	}
	
	private void npcfollowme(final MissionConfig conf) {
		npcfollowme(conf, conf.exeIndo.followID);
	}
	
	private void npcfollowme(final MissionConfig conf, final int npcID) {
		UtilHelper.npcfollowme(roleid, npcID, 60, conf.missionId);
	}
	
	private void transform(final MissionConfig conf) {
		if (conf == null)
			return;
		transform(conf, conf.exeIndo.transformID);
	}
	
	private void transform(final MissionConfig conf, final int shapeID) {
		UtilHelper.transform(roleid, shapeID, 60, conf.missionId);
	}

	private boolean checkCanAccept(long roleid, int taskid) {
		if (UtilHelper.isFirstStepInLine(UtilHelper.getMissionStep(taskid))) {
			return canAccept(roleid, taskid);
		} 
		return true;
	}
	
	public static boolean canAcceptTheTask(final long roleid, final int taskid) {
		final MissionConfig conf = MissionManager.getInstance().getMissionConfig(taskid);
		if (conf == null) {
			Module.logger.debug("人物["+roleid+"]任务id["+taskid+"]的配置不存在");
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null) {
			throw new NullPointerException("人物["+roleid+"]的属性没有找到");
		}
		if (!levelSuit(prop.getLevel(), conf)) {
			Module.logger.debug("人物["+roleid+"]任务id["+taskid+"]人物等级不符合要求" );
			return false;
		}
		return true;
	}
	
	public static boolean teamCheck(final long roleid, final MissionConfig conf) {
		if(conf.exeIndo.cruiseID <= 0)
			return true;
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (team != null && team.isTeamLeader(roleid)) {
			//有队伍且自己是队长，暂离所有成员
			for (long memid : team.getNormalMemberIds()) {
				if(memid != roleid) {
					//pexecute(new fire.pb.team.PAbsentReturnTeam(memid, 1));
					new PAbsentReturnTeam(memid, 1).call();
				}
			}
			Module.logger.debug(roleid + "有队伍且自己是队长,暂离所有成员!");
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160433, null);
		} else if (team != null && !team.isTeamLeader(roleid)) {
			//有队伍且自己是队员，暂离自己
			new PAbsentReturnTeam(roleid, 1).call();
			//team.setTeamMemberStateWithSP(roleid, TeamMemberState.eTeamAbsent);
			//pexecute(new fire.pb.team.PAbsentReturnTeam(roleid, 1));
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160433, null);
			return false;//(队员状态不应该接到这个任务)
		}
		return true;
	}

	public static boolean canAccept(final long roleid, final int taskid) {
		final int lineid = UtilHelper.getMissionLineid(taskid);	
		xbean.CommitedScenarioMission cst = xtable.Commitedscenariomission.select(roleid);
		if (cst!=null && cst.getCommitted().contains(lineid)) {
			return false;
		}

		final MissionConfig conf = MissionManager.getInstance().getMissionConfig(taskid);
		if (conf == null) {
			Module.logger.debug("人物["+roleid+"]任务id["+taskid+"]的配置不存在");
			return false;
		}
		
		boolean requireDone = false;
		if (!conf.limitInfo.requireMissionList.isEmpty()) {
			for (final int require : conf.limitInfo.requireMissionList) {
				final int requirelineid;
				requirelineid = UtilHelper.getMissionLineid(require);
				if (cst == null) {
					break;
				}
				if (cst.getCommitted().contains(requirelineid)) {
					requireDone = true;
					break;
				}
			}
		} else {
			requireDone = true;
		}
		if (!requireDone) {
			Module.logger.debug("人物["+roleid+"]任务id["+taskid+"]前置任务线未完成" );
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null) {
			throw new NullPointerException("人物["+roleid+"]的属性没有找到");
		}
		if (!levelSuit(prop.getLevel(), conf)) {
			Module.logger.debug("人物["+roleid+"]任务id["+taskid+"],人物等级不符合要求" );
			return false;
		}
		if (!schoolSuit(prop.getSchool(), conf)) {
			Module.logger.debug("人物["+roleid+"]任务id["+taskid+"]人物门派不符合要求" );
			return false;
		}
		return true;
	}
	
	private static boolean levelSuit(final int rolelevel, final MissionConfig conf) {
		final int minLevel = conf.limitInfo.minLevel;
		final int maxLevel = conf.limitInfo.maxLevel;
		if (minLevel != 0 && maxLevel != 0 && minLevel > maxLevel) {
			throw new IllegalArgumentException( 
					"接受任务" + conf.missionId + "人物等级限制配置有错,最低等级" 
					+ minLevel + ",最高等级" + maxLevel );
		}
		if (minLevel != 0 && rolelevel < minLevel) {
			return false;
		}
		if ( maxLevel != 0 && rolelevel > maxLevel ) {
			return false;
		}
		return true;
	}
	
	private static boolean schoolSuit(final int school, final MissionConfig conf) {
		//return MissionManager.getInstance().getTasklineCanAccept(school).contains(UtilHelper.getMissionLineid(conf.missionId));
		return true;
	}
}
