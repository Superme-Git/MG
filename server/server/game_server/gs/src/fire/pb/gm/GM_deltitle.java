package fire.pb.gm;

import fire.pb.title.PRemoveTitleProc;

public class GM_deltitle extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：请输入角色的id");
			return false;
		}

		final long roleId = getGmroleid();
		final int titleId = Integer.valueOf(args[0]);
		new PRemoveTitleProc(roleId, titleId).submit();
		return true;
	}

	@Override
	String usage() {
		return "//deltitle [roleId] [titleid]";
	}

}
