package fire.pb.gm;

import fire.pb.instancezone.bingfeng.BingFengRole;

public class GM_addbftime extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数错误：" + usage());
			return false;
		}

		int setcount = 2;
		if (args.length == 1)
			setcount = Integer.valueOf(args[0]);

		final long roleid = getGmroleid();
		final int cnt = setcount;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				BingFengRole roleBingFeng = new BingFengRole(roleid, false);

				int currentTime = roleBingFeng.getBingFengRoleBean().getTimes();
				roleBingFeng.getBingFengRoleBean().setTimes(cnt + currentTime);

				return true;
			}

		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//addbftime [times]";
	}
}
