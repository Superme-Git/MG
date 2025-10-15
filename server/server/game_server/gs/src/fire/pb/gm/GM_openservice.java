package fire.pb.gm;

import fire.pb.util.FireProp;

public class GM_openservice extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final Integer serviceid = Integer.parseInt(args[0]);
		if (serviceid != null) {
			fire.pb.npc.NpcServiceManager.getInstance().openService(serviceid);
			int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
					"gm.openservice.succ");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
		}
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
