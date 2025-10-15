package fire.pb.gm;

import fire.pb.scene.manager.RoleManager;

public class GM_see extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		fire.pb.scene.movable.Role i = RoleManager.getInstance().getRoleByID(
				getGmroleid());
		if (i == null)
			return false;

		final String action = args[0];
		if (action.equals("icansee")) {
			final int size = i.getIcansee().size();
			if (size > 2) {
				String mes = "i can see :" + size;
				sendToGM(mes);
				return true;
			}
			for (fire.pb.scene.movable.Role r : i.getIcansee().values()) {
				sendToGM(r.toString());
			}
		} else if (action.equals("whoseeme")) {
			final int size = i.getWhoseeme().size();
			if (size > 2) {
				String mes = "who see me :" + size;
				sendToGM(mes);
				return true;
			}
			for (fire.pb.scene.movable.Role r : i.getWhoseeme().values()) {
				sendToGM(r.toString());
			}
		}

		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
