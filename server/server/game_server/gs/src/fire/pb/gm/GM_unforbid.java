package fire.pb.gm;

import fire.pb.PUnForbidUser;
import fire.pb.util.FireProp;

import com.locojoy.base.Octets;

public class GM_unforbid extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 1) {
			sendToGM("参数错误 ,正确格式:" + usage());
			return false;
		}

		final int dstuserid = Integer.valueOf(args[0]);
		final int gmuserid = getGmUserid();

		int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
				"gm.unforbid.succ");
		fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
		new PUnForbidUser(dstuserid, gmuserid, getGmLocalsid(), new Octets(),
				false).submit();
		return true;
	}

	@Override
	String usage() {
		return "//unforbid  $Player$";
	}

}
