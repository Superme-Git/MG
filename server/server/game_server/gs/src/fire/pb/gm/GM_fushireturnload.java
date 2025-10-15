package fire.pb.gm;

import fire.pb.fushi.FushiManager;

public class GM_fushireturnload extends GMCommand {

	@Override
	boolean exec(String[] args) {
		float ratio = Float.parseFloat(args[0]);

		FushiManager.getInstance().initReturnFuShi();
		FushiManager.returnRatio = ratio;
		FushiManager.returnZoneId = fire.pb.main.ConfigManager.getGsZoneId();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
