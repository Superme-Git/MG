package fire.pb.gm;

import fire.pb.PAddExpProc;
import fire.pb.fushi.PAddVipExp;

public class GM_addvipexp extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		if (!fire.pb.StateCommon.isOnline(getGmroleid()))
			return false;

		final int exp = Integer.parseInt(args[0]);
		if (exp == 0) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}
		long roleid = getGmroleid();
		if (args.length > 1)
			roleid = Long.valueOf(args[1]);

		final PAddVipExp addexpProc = new PAddVipExp(roleid, exp, PAddVipExp.REASON_ADD_VIP_EXP_GM);
		addexpProc.submit();

		return true;
	}

	@Override
	String usage() {
		return "//addvipexp [addnumber] [roleid]";
	}

}