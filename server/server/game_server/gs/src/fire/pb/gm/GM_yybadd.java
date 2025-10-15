package fire.pb.gm;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;

public class GM_yybadd extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("用法为：//yybadd num");
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
				
				if (!FushiManager.addFushiToUser(userid, roleid, fushiNum, FushiConst.REASON_SUB_GM_CHARGEBACK, YYLoggerTuJingEnum.tujing_Value_gm_addfushi)) {
					logger.error("PAddFuShi.addFushiToUser[" + userid + "]roleid[" + roleid + "]调用失败！");
					return false;
				}
				return true;
			}
			
		}.submit();
		
		return true;
	}

	@Override
	String usage() {
		return "//yybadd num";
	}

}
