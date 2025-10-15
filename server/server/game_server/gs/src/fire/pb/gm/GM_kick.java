package fire.pb.gm;

import fire.pb.util.FireProp;

public class GM_kick extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long dstroleid = Long.parseLong(args[0]);
		final xbean.Properties prop = xtable.Properties.select(dstroleid);
		if (prop == null) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}
		gnet.link.Onlines.getInstance().kick(dstroleid,
				fire.pb.KickErrConst.ERR_GM_KICKOUT);

		int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
				"gm.kick.succ");
		fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
		return true;
	}

	@Override
	String usage() {
		return "//kick  $Player$";
	}

}
