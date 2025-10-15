package fire.pb.gm;

import fire.pb.util.FireProp;

public class GM_removeuser extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long roleid = Long.valueOf(args[0]);
		if (0 >= roleid)
			return false;
		Integer userID = xtable.Roleid2userid.select(roleid);
		if (userID != null)
		{
			if (fire.pb.StateCommon.isOnline(roleid))
			{
				final xbean.Properties prop = xtable.Properties.select(roleid);
				if (prop == null) {
					int msgid = fire.pb.util.FireProp.getIntValue(
							fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
					fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
					return false;
				}
				gnet.link.Onlines.getInstance().kick(roleid,fire.pb.KickErrConst.ERR_GM_KICKOUT);

				int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
						"gm.kick.succ");
				fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
				//gnet.link.Onlines.getInstance().getConnectedUsers().offline(userID.intValue(), false);
			}
			else
				gnet.link.Onlines.getInstance().getConnectedUsers().remove(userID.intValue());
		}
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
