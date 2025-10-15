package fire.pb.gm;

import fire.pb.timer.ActivityManager;

public class GM_bqey extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length != 1) {
			sendToGM(usage());
			return false;
		}
		if (args[0].equals("start")) {
			if (ActivityManager.getActivitymap().get(114) == null) {
				GM_startact cmd = new GM_startact();
				cmd.setGmLocalsid(getGmLocalsid());
				cmd.setGmroleid(getGmroleid());
				cmd.setGmUserid(getGmUserid());
				String[] paras = new String[] { "114" };
				return cmd.exec(paras);
			} else {
				sendToGM("不期而遇已经开启,要想使用//bqey start,请先用//bqey stop停止活动");
				return false;
			}
		} else if (args[0].equals("stop")) {
			GM_stopact cmd = new GM_stopact();
			cmd.setGmLocalsid(getGmLocalsid());
			cmd.setGmroleid(getGmroleid());
			cmd.setGmUserid(getGmUserid());
			String[] paras = new String[] { "114", "1" };
			return cmd.exec(paras);
		}
		return true;
	}

	@Override
	String usage() {

		return "//bqey start 开启;//bqey stop 关闭";
	}

}
