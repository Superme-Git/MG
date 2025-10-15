package fire.pb.gm;

import fire.pb.util.DateValidate;

public class GM_spvprole extends GMCommand {

	@Override
	boolean exec(String[] args) {

		long rid = Long.parseLong(args[0]);
		xbean.PvP1Role xpvprole = xtable.Pvp1roles.select(rid);
		if (xpvprole == null) {
			sendToGM("该玩家没有进入战斗   ");
			return true;
		}

		int firstWin = xpvprole.getFirstwinaward();
		int fiveBattle = xpvprole.getFivebattleaward();

		String time = DateValidate.formatTimeByType(xpvprole.getLastbattletime(), null);
		sendToGM("firstWin= " + firstWin + " fiveBattle=" + fiveBattle + " time=" + time);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
