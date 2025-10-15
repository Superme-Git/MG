package fire.pb.gm;

import fire.pb.fanpai.PReqCardProc;
import fire.pb.npc.SPingJi;

public class GM_faipai extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 1) {
			return false;
		}
		final int fanPiaiId = Integer.valueOf(args[0]);
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				boolean send = new PReqCardProc(getGmroleid(), fanPiaiId, 4)
						.call();
				if (send) {
					SPingJi pingji = new SPingJi();
					pingji.exp = 100;
					pingji.grade = (byte) 3;
					mkdb.Procedure.psendWhileCommit(getGmroleid(), pingji);
				}
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