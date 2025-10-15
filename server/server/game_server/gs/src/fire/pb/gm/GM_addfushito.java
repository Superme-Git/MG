package fire.pb.gm;

import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.util.MessageUtil;

public class GM_addfushito extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 3) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		long roleid = Long.parseLong(args[0]);
		Integer userid = xtable.Properties.selectUserid(roleid);
		if (userid == null || userid < 0) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145153, 0, null);
			return false;
		}
		final int fushiType = Integer.parseInt(args[1]);
		final int fushi = Integer.parseInt(args[2]);

		fire.pb.fushi.PAddFuShi pAddFuShi = new fire.pb.fushi.PAddFuShi(
				userid, roleid, fushi, fushiType - 1, YYLoggerTuJingEnum.GM);
		pAddFuShi.submit();
		List<String> paras = MessageUtil.getMsgParaList(String.valueOf(roleid),
				String.valueOf(fushi));
		fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145152, 0, paras);
		sendToGM("add fushi succ! roleid:" + roleid + "fushi number:"
				+ fushi);
		return true;
	}

	@Override
	String usage() {

		return "//addfushito roleid type num 第一个指定roleid,第二个符石类型(1，非绑定.2。绑定.3。系统赠送),第三个指定符石数量";
	}

}
