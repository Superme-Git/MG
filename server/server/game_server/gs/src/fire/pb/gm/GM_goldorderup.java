package fire.pb.gm;

import fire.pb.blackmarket.srv.BlackMarketManager;

public class GM_goldorderup extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		
		final int gold = Integer.parseInt(args[0]);
		final int rmb = Integer.parseInt(args[1]);
		final long roleid = getGmroleid();
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return BlackMarketManager.getInstance().goldOrderUp(roleid, gold, rmb);
			}
		}.submit();
		
		return true;
	}

	@Override
	String usage() {
		return "//goldorderup 金额     RMB";
	}

}
