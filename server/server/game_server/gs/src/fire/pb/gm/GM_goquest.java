package fire.pb.gm;

import java.util.Iterator;

import fire.pb.PropRole;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.PAcceptCircTask;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.PAcceptMajorMission;
import fire.pb.mission.SRefreshMissionState;
import fire.pb.mission.UtilHelper;
import fire.pb.npc.NpcServiceManager;
import fire.pb.npc.SNpcServiceMapping;
import fire.pb.npc.SchoolMaster;
import fire.pb.util.GeneralFormat;

public class GM_goquest extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length == 0) {
			this.sendToGM("参数错误." + usage());
			return false;
		}

		if (args[0].equals("buyitem")) {
			fire.pb.event.Poster.getPoster().dispatchEvent(
					new fire.pb.event.BuyItemEvent(getGmroleid(), 88888));
			return true;
		}

		if (args[0].equals("ct")) {
			if (args.length < 2)
				return false;
			final PropRole prole = new PropRole(getGmroleid(), true);
			SchoolMaster sm = CircleTaskManager.getInstance().getMasterMap()
					.get(prole.getSchool());
			final int npcid = sm.getMasterid();
			Long npckey = 0L;
			for (fire.pb.map.Npc npc : SceneNpcManager.getInstance().getNpcs()
					.values()) {
				if (npc.getNpcID() == npcid) {
					npckey = npc.getNpcKey();
					break;
				}
			}
			SNpcServiceMapping conf = NpcServiceManager.getInstance()
					.getServiceMappingByServiceID(Integer.parseInt(args[1]));
			if (conf == null)
				return false;
			int npcid2 = NpcServiceManager.getNpcIDByKey(npckey);
			if (args.length == 2) {
				new PAcceptCircTask(getGmroleid(), 100001, npcid2,
						conf.getParam1(), false).submit();
			} else if (args.length == 3) {
				new PAcceptCircTask(getGmroleid(), 100001, npcid2,
						conf.getParam1(), Integer.parseInt(args[2])).submit();
			}
			return true;
		}

		if (args[0].equals("randq")) {
			final PropRole prole = new PropRole(getGmroleid(), true);
			for (int i = 0; i < 20; ++i) {
				GeneralFormat format = CircleTaskManager.getInstance()
						.getCircTaskTypeInfo(prole.getLevel(), 0, 1010000);
				if (format != null) {
					CircleTaskManager.logger.info(format.getId() + ",");
				} else {
					CircleTaskManager.logger.info("null,");
				}
			}
			return true;
		}

		Integer taskid;
		try {
			taskid = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			taskid = taskname2id.get(args[0]);
			if (taskid == null) {
				sendToGM(usage());
				return false;
			}
		}
		Long roleid;
		if (args.length < 2) {
			roleid = getGmroleid();
		} else {
			try {
				roleid = Long.parseLong(args[1]);
			} catch (NumberFormatException e) {
				roleid = xtable.Rolename2key.select(args[1]);
			}
		}
		if (roleid == null) {
			sendToGM(usage());
			return false;
		}
		final int missionID = taskid;
		if (UtilHelper.isScenarioMission(missionID)) {
			acceptScenarioTask(roleid, missionID);
			return true;
		}
		return false;
	}

	@Override
	String usage() {
		return "goquest [taskid] [roleid/name]";
	}

	private void acceptScenarioTask(final long roleid, final int missionID) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final int taskline = fire.pb.mission.UtilHelper
						.getMissionLineid(missionID);
				xbean.CommitedScenarioMission cst = xtable.Commitedscenariomission
						.get(roleid);
				if (cst != null) {
					Iterator<Integer> iter = cst.getCommitted().iterator();
					while (iter.hasNext()) {
						if (iter.next() == taskline) {
							iter.remove();
						}
					}
				}
				if (fire.pb.mission.UtilHelper
						.isBranchScenarioMission(missionID)) {

					xbean.AcceptedScenarioMission ast = xtable.Acceptedscenariomission
							.get(roleid);
					if (ast == null) {
						ast = xbean.Pod.newAcceptedScenarioMission();
						xtable.Acceptedscenariomission.insert(roleid, ast);
					}
					xbean.Mission xtask = ast.getAccepted().get(taskline);
					if (xtask != null) {
						xtask.getPath().clear();
						xtask.setStatus(fire.pb.mission.MissionStatus.COMMITED);
						SRefreshMissionState send = new SRefreshMissionState();
						send.missionid = xtask.getId();
						send.missionstatus = fire.pb.mission.MissionStatus.COMMITED;
						mkdb.Transaction.tsendWhileCommit(roleid, send);
					} else {
						xtask = xbean.Pod.newMission();
						ast.getAccepted().put(taskline, xtask);
					}
				} else if (fire.pb.mission.UtilHelper
						.isMajorScenarioMission(missionID)) {
					xbean.Mission xtask = xtable.Majorscenariomission
							.get(roleid);
					if (xtask != null) {
						xtask.getPath().clear();
						xtask.setStatus(fire.pb.mission.MissionStatus.COMMITED);
						SRefreshMissionState send = new SRefreshMissionState();
						send.missionid = xtask.getId();
						send.missionstatus = fire.pb.mission.MissionStatus.COMMITED;
						mkdb.Transaction.tsendWhileCommit(roleid, send);
					}
				} else {
					return false;
				}
				xtable.Showingmission.remove(roleid);
				return new PAcceptMajorMission(roleid, missionID, true, true)
						.call();
			}
		}.submit();
	}
}
