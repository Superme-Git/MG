package fire.pb.gm;

import fire.pb.PAddExpProc;

public class GM_addexp extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		if (!fire.pb.StateCommon.isOnline(getGmroleid()))
			return false;

		final long exp = Long.parseLong(args[0]);
		if (exp == 0) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}
		long roleid = getGmroleid();
		if (args.length > 1)
			roleid = Long.valueOf(args[1]);
		final fire.pb.PAddExpProc addexpProc = new fire.pb.PAddExpProc(roleid, exp, PAddExpProc.GM, "GM_addexp添加");
		addexpProc.submit();
		return true;
	}

	@Override
	String usage() {
		return "//addexp [addnumber] [roleid]";
	}

}