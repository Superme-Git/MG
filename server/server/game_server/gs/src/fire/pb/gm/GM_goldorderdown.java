package fire.pb.gm;

import fire.pb.blackmarket.srv.BlackMarketManager;

public class GM_goldorderdown extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		
		final int pid = Integer.parseInt(args[0]);
		final long roleid = getGmroleid();
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return BlackMarketManager.getInstance().goldOrderDown(roleid, pid);
			}
		}.submit();
		
		return true;
	}

	@Override
	String usage() {
		return "//goldorderdown 订单号";
	}

}
