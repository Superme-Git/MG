package fire.pb.gm;

import fire.pb.friends.SUpdateFriendState;

public class GM_updatefrd extends GMCommand {

	@Override
	boolean exec(String[] args) {

		final long friId = Long.parseLong(args[0]);
		final int ship = Integer.parseInt(args[1]);

		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				long roleid = getGmroleid();
				SUpdateFriendState send = new SUpdateFriendState();
				send.relation = (short) ship;
				send.roleid = friId;
				mkdb.Procedure.psendWhileCommit(roleid, send);
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
