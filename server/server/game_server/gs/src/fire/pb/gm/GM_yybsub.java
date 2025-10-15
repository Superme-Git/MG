package fire.pb.gm;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;

public class GM_yybsub extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("用法为：//yybsub num");
			return false;
		}
		
		final int userid = getGmUserid();
		final long roleid = getGmroleid();
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				int fushiNum = 10;
				if (args.length == 1) {
					fushiNum = Integer.parseInt(args[0]);
				}
				
				FushiManager.subFushiFromUser(userid, roleid,
						fushiNum, 0, 0, FushiConst.REASON_SUB_GM_CHARGEBACK,
						YYLoggerTuJingEnum.GM, false);
				
				return true;
			}
			
		}.submit();
		
		return true;
	}

	@Override
	String usage() {
		return "//yybsub num";
	}

}
