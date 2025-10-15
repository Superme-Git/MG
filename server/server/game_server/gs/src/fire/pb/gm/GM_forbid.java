package fire.pb.gm;

import fire.pb.KickErrConst;
import fire.pb.PForbidUser;
import fire.pb.util.FireProp;

public class GM_forbid extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 3) {
			sendToGM("参数错误 ,正确格式:" + usage());
			return false;
		}
		final int dstuserid = Integer.valueOf(args[0]);
		final int gmuserid = getGmUserid();
		final int forbid_time = Integer.parseInt(args[1]);
		final String reason = args[2];

		int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
				"gm.forbid.succ");
		fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
		try {
			new PForbidUser(dstuserid, gmuserid, getGmLocalsid(), forbid_time,
					reason, false, KickErrConst.ERR_GM_KICKOUT).submit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	String usage() {
		return "单服封禁//forbid  $Player$ $Time$ $reason$";
	}

}
