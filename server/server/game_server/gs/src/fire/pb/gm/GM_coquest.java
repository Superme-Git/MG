package fire.pb.gm;

import fire.pb.mission.PCommitMajorMission;

public class GM_coquest extends GMCommand {

	@Override
	boolean exec(String[] args) {
		Long roleid = getGmroleid();
		if (roleid == null) {
			sendToGM(usage());
			return false;
		}
		commitScenarioTask(roleid);
		return true;
	}

	@Override
	String usage() {
		return "coquest";
	}

	private void commitScenarioTask(final long roleid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.Mission xtask = xtable.Majorscenariomission
						.get(roleid);
				if (xtask != null) {
					return new PCommitMajorMission(roleid, xtask.getId(), true, true).call();
				} else {
					return false;
				}
			}
		}.submit();
	}
}
