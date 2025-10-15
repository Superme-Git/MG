package fire.pb.gm;

import fire.pb.scene.Scene;

public class GM_rolenum extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final int val = fire.pb.map.RoleManager.getInstance().getRoles()
				.size();
		Scene.LOG.info("====总在线人数:" + val + "====");
		sendToGM("总在线人数" + val);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
