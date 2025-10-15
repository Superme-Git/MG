package fire.pb.gm;

import fire.log.LogManager;

public class GM_showlog extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length == 1) {
			String param = args[0];
			if ("--help".equals(param)) {
				sendToGM("GM命令格式   : " + usage());
				return true;
			}
		}
		if (args.length < 2) {
			sendToGM("GM命令格式错误  : " + usage());
			return true;
		}

		Long roleid = Long.parseLong(args[0]);
		xbean.Properties role = xtable.Properties.select(roleid);
		if (null == role) {
			sendToGM("您输入的role错误，具体参见--help");
			return true;
		}
		String swich = args[1];
		boolean isOn = false;
		if ("on".equals(swich)) {
			isOn = true;
		} else if ("off".equals(swich)) {
			isOn = false;
		} else {
			sendToGM("GM命令格式错误  : " + usage());
			return true;
		}
		LogManager.isShowLog = isOn;
		LogManager.showLogGMRoleid = roleid;

		return true;
	}

	@Override
	String usage() {
		return "showlog gmroleid on||off";
	}

}
