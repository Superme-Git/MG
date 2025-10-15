package fire.pb.gm;

import java.util.HashMap;

import fire.pb.fushi.PAddCoinTest;
import fire.pb.main.ConfigManager;

public class GM_addcash extends GMCommand {

	@Override
	boolean exec(final String[] args) {

		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				Integer rmb = Integer.parseInt(args[0]);
				long roleid = getGmroleid();
				int userid = xtable.Properties.selectUserid(roleid);
				long sn = xtable.Chargeorder.nextKey();
				new PAddCoinTest(userid, ConfigManager.getGsZoneId(), sn,
						rmb * 10, new HashMap<String, String>(), rmb,
						"gmaddcash", String.valueOf(sn), rmb * 10, "gm").call();
				return true;
			}

		}.submit();
		return true;
	}

	@Override
	String usage() {

		return "addcash rmb";
	}

}
