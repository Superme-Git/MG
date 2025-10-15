package fire.pb.gm;

import fire.pb.PropRole;
import fire.pb.RoleSex;

public class GM_getmysex extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleid = getGmroleid();

		PropRole role = new PropRole(roleid, true);
		if (role.getSex() == RoleSex.MALE) {
			sendToGM("偶是男滴！！！");
			return true;
		} else {
			sendToGM("偶是女滴！！！");
			return true;
		}
	}

	@Override
	String usage() {
		return null;
	}

}
