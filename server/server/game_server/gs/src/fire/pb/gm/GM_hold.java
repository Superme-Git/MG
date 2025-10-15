package fire.pb.gm;

import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;

public class GM_hold extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1)
			return false;

		final fire.pb.map.Role gmrole = fire.pb.map.RoleManager.getInstance()
				.getRoleByID(getGmroleid());
		if (null == gmrole)
			return false;

		final long holdroleid = Long.parseLong(args[0]);
		final fire.pb.map.Role holdrole = fire.pb.map.RoleManager
				.getInstance().getRoleByID(holdroleid);
		if (holdrole == null) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}

		Transfer.justGoto(holdroleid, gmrole.getScene(), gmrole.getPos()
				.toGridPos().getX(), gmrole.getPos().toGridPos().getY(),
				SRoleEnterScene.GM_HOLD);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
