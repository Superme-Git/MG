package fire.pb.gm;

import fire.pb.fushi.payday.PSubscribeOrderResponse;

public class GM_dyue extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length == 0) {
			this.sendToGM("参数错误." + usage());
			return false;
		}
		long curRoleId = getGmroleid();
		long tarRoleId = curRoleId;
		int tarTime = 30;
		if (args.length == 1)
			tarTime = Integer.parseInt(args[0]);
		if (args.length == 2){
			tarRoleId = Long.parseLong(args[0]);
			tarTime = Integer.parseInt(args[1]);
		}
		final long subrole = tarRoleId;
//		PropRole prole = new PropRole(subrole, true);
//		final int subuser = prole.getUserid();
		final int subtime = tarTime;
		new PSubscribeOrderResponse(subrole, subtime).submit();
//		new mkdb.Procedure() {
//			@Override
//			protected boolean process() {
//				 xbean.Properties properties = xtable.Properties.get(subrole);
//				 int level = properties.getLevel();
//				 StateManager.logger.info("xdbt:" + level);
//				return true;
//			}
//		}.submit();
		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
