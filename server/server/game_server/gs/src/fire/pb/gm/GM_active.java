package fire.pb.gm;

import fire.pb.main.ConfigManager;

public class GM_active extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length != 1) {
			sendToGM(usage());
			return true;
		}
		Integer val = Integer.parseInt(args[0]);
		ConfigManager.getInstance().setNeedActive(val > 0 ? true : false);
		sendToGM("服务器已经设置为" + (val > 0 ? "" : "不") + "需要激活码登陆!");
		return true;
	}

	@Override
	String usage() {
		return "//active 1/0 1需要激活码 0不需要激活码";
	}

}
