package fire.pb.gm;

import fire.pb.hook.PReqSetHookDataProc;

public class GM_addhookdata extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		if (!fire.pb.StateCommon.isOnline(getGmroleid()))
			return false;
		final int enumHookData = Integer.parseInt(args[0]);
		if (enumHookData < 0 || enumHookData > 7) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}

		long roleid = getGmroleid();
		int changeVal = 0;
		if (args.length > 1) {
			changeVal = Integer.valueOf(args[1]);
		}
		new PReqSetHookDataProc(roleid, (short) enumHookData, (long) changeVal)
				.submit();
		return true;
	}

	@Override
	String usage() {
		return "//addexp [enumHookData] [changeVal]";
	}

}