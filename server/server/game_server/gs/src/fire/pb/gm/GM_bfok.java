package fire.pb.gm;

import fire.pb.instancezone.bingfeng.BingFengLandMgr;

public class GM_bfok extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final int landid;
		if (args.length >= 1) {
			landid = Integer.parseInt(args[0]);
		}
		else {
			landid = 0;
		}
		
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				xbean.RoleBFInfo roleBingFengInfo = BingFengLandMgr.getInstance().getRoleBingFengInfo(getGmroleid(), landid, false);
				roleBingFengInfo.setStage(25);
				
				sendToGM("角色 " + getGmroleid() + " 冰封王座副本: " + landid	+ "已经通关了");
				
				return true;
			}
		}.submit();
		return false;
	}

	@Override
	String usage() {
		return "//bfok 1 or 2 or 3";
	}

}
