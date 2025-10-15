package fire.pb.gm;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;

public class GM_subfushi extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long roleid = Long.parseLong(args[0]);
		Integer userid = xtable.Properties.selectUserid(roleid);
		if (userid == null || userid < 0) {
			super.sendToGM("can't find the roleid!");
			return false;
		}
		final String rolename = xtable.Properties.selectRolename(roleid);
		final int user = userid;
		final int fushi = Integer.parseInt(args[1]);
		new mkdb.Procedure() {
			public boolean process() {
				boolean res = FushiManager.subFushiFromUser(user, roleid,
						fushi, 0, 0, FushiConst.REASON_SUB_GM_CHARGEBACK,
						YYLoggerTuJingEnum.GM, false
						);
				if (res) {
					String msg = "1-sucess." + "roleid:" + roleid
							+ "-rolename:" + rolename + "-cash:" + fushi;
					sendToGM(msg);
					return true;
				} else {
					String msg = "2-fail!" + "roleid:" + roleid + "-rolename:"
							+ rolename;
					sendToGM(msg);
					return false;
				}
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//subfushi roleid num";
	}

}
