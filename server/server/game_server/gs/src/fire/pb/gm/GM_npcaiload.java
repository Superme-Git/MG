package fire.pb.gm;

import fire.pb.scene.npcai.AIManager;

public class GM_npcaiload extends GMCommand {

	@Override
	boolean exec(String[] args) {
		AIManager.getInstance().loadConfig();
		return true;
	}

	@Override
	String usage() {
		return "";
	}

}
