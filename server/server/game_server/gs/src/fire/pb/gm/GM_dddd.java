package fire.pb.gm;

import fire.pb.mission.UtilHelper;

public class GM_dddd extends GMCommand {

	@Override
	boolean exec(String[] args) {

		final long roleid = Long.parseLong(args[0]);
		final int taskId = Integer.parseInt(args[1]);
		final int value = Integer.parseInt(args[2]);

		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.AcceptedScenarioMission branchdata = xtable.Acceptedscenariomission
						.get(roleid);
				if (branchdata == null) {
					sendToGM("没有任务状态");
					return false;
				}

				final int lineid = UtilHelper.getMissionLineid(taskId);
				xbean.Mission ret = branchdata.getAccepted().get(lineid);
				if (ret == null) {
					sendToGM("没有任务状态");
					return false;
				}

				ret.setStatus(value);
				sendToGM("任务状态设置ok");
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
