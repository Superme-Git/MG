package fire.pb.gm;

import fire.pb.PForbidUser;

public class GM_xianyi extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length == 0) {
			new PForbidUser(getGmUserid(), 0, 0, -5, null, true, 0).submit();
			sendToGM("遇外挂几率增加1%");
		} else if (args[0].equals("times")) {
			sendToGM("额外的反外挂几率:" + PForbidUser.getExtraProb(getGmUserid())
					+ "%");
		}
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
