package fire.pb.mission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fire.pb.event.LevelBreakEvent;
import fire.pb.mission.MissionStatus;
import fire.pb.npc.SVisitNpc;
import fire.pb.team.TeamManager;



public class MissionColumn implements IRoleMissionColumn<RoleMission>{
	
	private final class MissionIterator implements Iterator<RoleMission> {
		private final Iterator<Integer> iter = branchdata.getAccepted().keySet().iterator();
		private boolean majorMissionshowed = majorscenario.getId() == 0;
		@Override
		public boolean hasNext() {
			return iter.hasNext() || !majorMissionshowed;
		}

		@Override
		public RoleMission next() {
			if (iter.hasNext()) {
				xbean.Mission data = branchdata.getAccepted().get(iter.next());
				return new MissionBranchScenario(roleid, data);
			}
			majorMissionshowed = true;
			return majorscenario;
		}

		@Override
		public void remove() {
			iter.remove();
		}
	}
	
	private final long roleid;
	
	private final MissionMajorScenario majorscenario;
	
	private final xbean.AcceptedScenarioMission branchdata;
	
	public MissionColumn(final long roleid, final boolean readonly) {
		this.roleid = roleid;
		majorscenario = new MissionMajorScenario(roleid, readonly);
		if (readonly) {
			xbean.AcceptedScenarioMission xxx = xtable.Acceptedscenariomission.select(roleid);
			if (xxx == null) {
				branchdata = xbean.Pod.newAcceptedScenarioMissionData();
			} else {
				branchdata = xxx;
			}
		} else {
			xbean.AcceptedScenarioMission xxx = xtable.Acceptedscenariomission.get(roleid);
			if (xxx == null) {
				branchdata = xbean.Pod.newAcceptedScenarioMission();
				xtable.Acceptedscenariomission.insert(roleid, branchdata);
			} else {
				branchdata = xxx;
			}
		}
	}
	
	final void afterEnterWorld() {
		if (majorscenario.getState() == MissionStatus.COMMITED && 
				!majorscenario.getConf().rewardInfo.postMissionList.isEmpty()) {
			Integer nextmission = majorscenario.getConf().rewardInfo.postMissionList.get(0);
			if (nextmission != null) {
				new PAcceptMajorMission(roleid, nextmission, true).call();
			}
		}
		
		xtable.Showingmission.remove(roleid);
		if (majorscenario.getState() == MissionStatus.UNACCEPT) {
			final int school = xtable.Properties.selectSchool(roleid);
			java.util.Set<Integer> all = MissionManager.getInstance().getTasklineCanAccept(school);
			for (Integer missionline : all) {
				final int taskid = UtilHelper.getMissionId(missionline, UtilHelper.getFirstStep());
				if (UtilHelper.isMajorScenarioMission(taskid)) {
					if (new PAcceptMajorMission(roleid, taskid, true).call()) {
						break;
					}
				}
			}
		} else {
			this.refreshPostMissionLine(0);
		}
	}
	
	protected void getCanacceptQuest(final java.util.List<Integer> quests) {
		for (xbean.Mission mission : branchdata.getAccepted().values()) {
			if (mission.getStatus() == MissionStatus.UNACCEPT) {
				if(!UtilHelper.isTuPoMission(mission.getId())) {
					quests.add(mission.getId());
				}
			}
		}
	}
	
	@Override
	public long getRoleId() {
		return roleid;
	}
	
	
	public RoleMission getMission(final int missionid) {
		if (UtilHelper.isBranchScenarioMission(missionid)) {
			final int lineid = UtilHelper.getMissionLineid(missionid);
			xbean.Mission mission = branchdata.getAccepted().get(lineid);
			if (mission == null || mission.getId() != missionid)
				return null;
			
			return new MissionBranchScenario(roleid, mission);
		}
		if (UtilHelper.isMajorScenarioMission(missionid)) {
			if (missionid == majorscenario.getId())
				return majorscenario;
			else
				return null;
		}
		return null;
	}
	
	public boolean hasTuPoMission() {
		for (RoleMission task : this) {
			if (task.getConf() != null) {
				if(task.getId() / 10000 == 51) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public RoleMission getMissionByLineid(final int tasklineid) {
		final int id = UtilHelper.getMissionId(tasklineid, UtilHelper.getFirstStep());
		if (UtilHelper.isBranchScenarioMission(id)) {
			xbean.Mission xtask = branchdata.getAccepted().get(tasklineid);
			if (xtask == null)
				return null;
			return new MissionBranchScenario(roleid, xtask);
		}
		if (UtilHelper.isMajorScenarioMission(id))
			return majorscenario;
		return null;
	}
	
	public ArrayList<Integer> getMissionsByNpcid(final int npcid, SVisitNpc svisitNpc) {
		ArrayList<Integer> missions = new ArrayList<Integer>();
		for (RoleMission mission : this) {
			int missionid = mission.getId();
			if (mission.getConf() != null && mission.getConf().exeIndo.npcID == npcid) {
				if (mission.getState() != MissionStatus.UNACCEPT){
					/*
					xbean.CommitedMissions ct = xtable.Commitedmission.select(this.roleid);
					if(null == ct)
						continue;
					else {
						if(ct.getCommitted().contains(Integer.valueOf(missionid)))
							continue;
						else 
							missions.add(missionid);
					}
					*/
					missions.add(missionid);
					if(mission.getConf().exeIndo.unionSeekHelp > 0)
						svisitNpc.services.add(mission.getConf().exeIndo.unionSeekHelp);
					if(mission.getConf().exeIndo.worldSeekHelp > 0)
						svisitNpc.services.add(mission.getConf().exeIndo.worldSeekHelp);
					if(mission.getConf().aiInfo.aIID > 0 && mission.getConf().battleInfo.battleVideo > 0)
						svisitNpc.services.add(mission.getConf().battleInfo.battleVideo);
					continue;
				} 
				if (UtilHelper.isFirstStepInLine(UtilHelper.getMissionStep(missionid))){
					/*
					xbean.CommitedMissions ct = xtable.Commitedmission.select(this.roleid);
					if(null == ct)
						continue;
					else {
						if(ct.getCommitted().contains(Integer.valueOf(missionid)))
							continue;
						else 
							missions.add(missionid);
					}
					*/
					missions.add(missionid);
					if(mission.getConf().exeIndo.unionSeekHelp > 0)
						svisitNpc.services.add(mission.getConf().exeIndo.unionSeekHelp);
					if(mission.getConf().exeIndo.worldSeekHelp > 0)
						svisitNpc.services.add(mission.getConf().exeIndo.worldSeekHelp);
					if(mission.getConf().aiInfo.aIID > 0 && mission.getConf().battleInfo.battleVideo > 0)
						svisitNpc.services.add(mission.getConf().battleInfo.battleVideo);
				}
			}
		}
		return missions;
	}
	
	public int getMissionState(final int taskid) {
		final int lineid = UtilHelper.getMissionLineid(taskid);
		xbean.CommitedScenarioMission cst = xtable.Commitedscenariomission.select(roleid);
		if (cst != null && cst.getCommitted().contains(lineid)) {
			return MissionStatus.COMMITED;
		}
		if (UtilHelper.isBranchScenarioMission(taskid)) {
			xbean.Mission xtask = branchdata.getAccepted().get( lineid );
			if (xtask == null) {
				return MissionStatus.UNACCEPT;
			} 
			if (xtask.getId() == taskid)
				return xtask.getStatus();
			if(!xtask.getPath().contains(taskid))
				return MissionStatus.UNACCEPT;
			else {
				return MissionStatus.COMMITED;
			}
		} else if (UtilHelper.isMajorScenarioMission(taskid)) {
			if (taskid == majorscenario.getId())
				return majorscenario.getState();
			else {
				if (majorscenario.data.getPath().contains(taskid)) {
					return MissionStatus.COMMITED;
				} else {
					return MissionStatus.UNACCEPT;
				}
			}
		}
		return MissionStatus.UNACCEPT;
	}
	
	@Override
	public Iterator<RoleMission> iterator() {
		return new MissionIterator();
	}
	
	void onEvent(final fire.pb.event.Event e) {
		if (e instanceof fire.pb.event.LevelupEvent) {
			onLevelChange();
		} else if (e instanceof fire.pb.event.LevelBreakEvent) {
			LevelBreakEvent ev = (LevelBreakEvent)e;
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					SReqMissionCanAccept send = new SReqMissionCanAccept();
					Module.getInstance().getCanAcceptMission4TuPo(roleid, send.missions, ev.getRolelevel());
					mkdb.Procedure.psend(roleid, send);
					return true;
				}
			}.call();
		} else if (e instanceof fire.pb.event.LevelContinueEvent) {
			new fire.pb.PLevelBreakUpProc(roleid).call();
		}
		
		majorscenario.onEvent(e);
		List<Integer> branchtasks = new ArrayList<Integer>();
		for (xbean.Mission xtask : branchdata.getAccepted().values()) {
			if(UtilHelper.isTuPoMission(xtask.getId()))
				continue;
			branchtasks.add(xtask.getId());
		}
		for (int taskid : branchtasks) {
			RoleMission mission = getMission(taskid);
			if (mission != null)
				mission.onEvent(e);
		}
	}

	private final void onLevelChange() {
		refreshPostMissionLine(0);
	}
	
	final boolean refreshPostMissionLine() {
		return refreshPostMissionLine(0);
	}
	
	final boolean refreshPostMissionLine(final int completedtaskline) {
		java.util.Set<Integer> posts = MissionManager.getInstance().getPostmissions(completedtaskline);
		boolean ret = true;
		if (posts != null) {
			for (Integer mission : posts) {
				if (UtilHelper.isBranchScenarioMission(mission)) {
					if(UtilHelper.isTuPoMission(mission))
						continue;
					final int tasklineid = UtilHelper.getMissionLineid(mission);

					xbean.Mission xtask = branchdata.getAccepted().get(tasklineid);
					if (xtask != null && xtask.getStatus() != MissionStatus.UNACCEPT) {
						continue;
					}
					if (PAcceptMajorMission.canAccept(roleid, mission)) {
						MissionConfig conf = MissionManager.getInstance().getMissionConfig( mission );
						if (conf == null)
							continue;
						if (conf.exeIndo.npcID > 0) {
							if(!UtilHelper.isTuPoMission(mission)){
								xtask = xbean.Pod.newMission();
								xtask.setId(mission);
								xtask.setStatus(MissionStatus.UNACCEPT);
								branchdata.getAccepted().put(tasklineid , xtask);
							}
						} else {
							mkdb.Procedure.pexecuteWhileCommit(
								new mkdb.Procedure() {
									@Override
									protected boolean process() {
										TeamManager.getTeamByRoleId(roleid);
										new PAcceptMajorMission(roleid, mission, true).call();
										return true;
									}
								} );
						}
					} else {
						branchdata.getAccepted().remove(tasklineid);	
					}

				} else if (UtilHelper.isMajorScenarioMission(mission) && PAcceptMajorMission.canAccept(roleid, mission)) {
					ret = new PAcceptMajorMission(roleid, mission, true).call();
				}

			}
		}

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				SReqMissionCanAccept send = new SReqMissionCanAccept();
				Module.getInstance().getCanAcceptMission(roleid, send.missions);
				mkdb.Procedure.psend(roleid, send);
				return true;
			}
		}.call();
		
		return ret;
	}
	public boolean removeMissionline(final int taskline) {
		branchdata.getAccepted().remove(taskline);
		return true;
	}
}

