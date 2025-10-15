package fire.pb.gm;

import fire.pb.activity.reg.PAddSuppRegNumProc;

public class GM_addsuppsignnum extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int suppsignnum;
		suppsignnum = Integer.parseInt(args[0]);
		if (suppsignnum == 0 || suppsignnum > 1L << 60
				|| suppsignnum < (1L << 60) * -1) {
			return false;
		}
		new PAddSuppRegNumProc(getGmroleid(), suppsignnum, true).submit();

		return true;
	}

	@Override
	String usage() {
		return "addsuppsignnum suppsignnum";
	}

}
