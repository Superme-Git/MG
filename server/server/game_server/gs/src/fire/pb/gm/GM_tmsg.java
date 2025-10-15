package fire.pb.gm;

import fire.pb.mission.SWaitRollTime;

public class GM_tmsg extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long roleid = getGmroleid();
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				SWaitRollTime waitTime = new SWaitRollTime(160438);
				mkdb.Procedure.psendWhileCommit(roleid, waitTime);
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
