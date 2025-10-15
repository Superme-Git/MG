package fire.pb.gm;

import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;

public class GM_gotoplayer extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;

		final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance()
				.getRoleByID(getGmroleid());
		if (null == role)
			return false;

		final long gotoroleid = Long.parseLong(args[0]);
		final fire.pb.map.Role gotorole = fire.pb.map.RoleManager
				.getInstance().getRoleByID(gotoroleid);
		if (gotorole == null) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}
		Transfer.justGoto(getGmroleid(), gotorole.getScene(), gotorole.getPos()
				.toGridPos().getX(), gotorole.getPos().toGridPos().getY(),
				SRoleEnterScene.QUEST);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
