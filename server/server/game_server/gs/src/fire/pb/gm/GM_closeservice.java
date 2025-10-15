package fire.pb.gm;

import fire.pb.util.FireProp;

public class GM_closeservice extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final Integer serviceid = Integer.parseInt(args[0]);
		if (serviceid != null) {
			fire.pb.npc.NpcServiceManager.getInstance()
					.closeService(serviceid);
			int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
					"gm.closeservice.succ");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
		}
		return true;
	}

	@Override
	String usage() {
		return "//closeservice serviceid";
	}

}
