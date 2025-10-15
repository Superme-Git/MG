package fire.pb.gm;

import fire.pb.mission.MissionColumn;
import fire.pb.mission.MissionStatus;
import fire.pb.mission.PMissionProc;
import fire.pb.mission.RoleMission;
import fire.pb.mission.util.GMCommitParam;

public class GM_curquest extends GMCommand {

	private final static String DirectFinish = "f";

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			showTask(getGmroleid());
			return true;
		} else {
			if (args[0].equals(DirectFinish)) {
				commitCurquest(Long.valueOf(args[1]), Integer.valueOf(args[2]),
						Integer.valueOf(args[3]));
				return true;
			}
			final Long roleid;
			if (args[0].matches("\\d+")) {
				roleid = Long.parseLong(args[0]);
			} else {
				roleid = xtable.Rolename2key.select(args[0]);
			}
			if (roleid == null) {
				sendToGM(usage());
				return false;
			}
			if (args.length > 1) {
				if (args[1].equals(DirectFinish)) {
					commitCurquest(roleid);
					return true;
				}
			}
		}
		sendToGM(usage());
		return false;
	}

	private void commitCurquest(final long roleid, final int taskid,
			final int taskValue) {
		new PMissionProc() {
			@Override
			protected boolean missionExecute() {
				MissionColumn tsc = new MissionColumn(roleid, false);
				RoleMission task = tsc.getMission(taskid);
				task.setTaskvalue(taskValue);
				return true;
			}
		}.submit();
	}

	private void commitCurquest(final long roleid) {
		new PMissionProc() {
			@Override
			protected boolean missionExecute() {
				MissionColumn tsc = new MissionColumn(roleid, false);
				for (RoleMission task : tsc) {
					return task.commit(new GMCommitParam(), true) != null;
				}
				return false;
			}
		}.submit();
	}

	private void showTask(final long roleid) {
		StringBuilder sb = new StringBuilder();
		sb.append("<T t=\"人物id=").append(roleid)
				.append("所接受的任务为:\"></T><B></B>");
		for (fire.pb.mission.RoleMission task : new fire.pb.mission.MissionColumn(
				roleid, true)) {
			if (task.getState() != MissionStatus.UNACCEPT) {
				if (fire.pb.mission.UtilHelper.isBranchScenarioMission(task
						.getId())) {
					sb.append("<T t=\"支线任务:");
				} else {
					sb.append("<T t=\"主线任务:");
				}
				sb.append(task.toString()).append("\"></T><B></B>");
			}
		}

		sendToGM(sb.toString());
	}

	@Override
	String usage() {
		return "curquest [roleid | name] {" + DirectFinish + " questid}"
				+ "(暂时只能支持剧情任务)";
	}

}
