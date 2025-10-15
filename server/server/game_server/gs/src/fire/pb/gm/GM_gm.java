package fire.pb.gm;

public class GM_gm extends GMCommand {

	@Override
	boolean exec(String[] args) {

		int userID = getGmUserid();

		if (xtable.Auuserinfo.selectBlisgm(userID) != 1) {
			sendToGM("你不是GM，请不要轻易改变GM开关……");
			return false;
		}

		if ("on".equalsIgnoreCase(args[0])) {
			GMInteface.setGMOn(true);
			sendToGM("开所有人GM权限的命令已生效");
		} else if ("off".equalsIgnoreCase(args[0])) {
			sendToGM("GM命令开关已关闭");
			GMInteface.setGMOn(false);
		}

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
