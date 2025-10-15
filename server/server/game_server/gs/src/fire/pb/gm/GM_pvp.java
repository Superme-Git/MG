package fire.pb.gm;

import fire.pb.util.DateValidate;

public class GM_pvp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long rid = Long.valueOf(args[0]);
		xbean.PvP1Role xpvprole = xtable.Pvp1roles.select(rid);
		if (xpvprole != null) {
			String time = DateValidate.formatTimeByType(
					xpvprole.getLastbattletime(), null);
			int battleNum = xpvprole.getBattlenum();
			int continueWinNum = xpvprole.getContinuewinnum();

			sendToGM(time + "   " + battleNum + "  " + continueWinNum);
		} else {
			sendToGM("没有战斗信息");
		}
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
