package fire.pb.gm;

import fire.pb.map.Role;
import fire.pb.map.RoleManager;

public class GM_getpos extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final Role r = RoleManager.getInstance().getRoleByID(getGmroleid());
		if (null == r)
			return false;
		final String info = "所在位置:\t" + r.getScene() + "\t"
				+ r.getPos().toGridPos();
		logger.debug(info);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
