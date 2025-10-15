package fire.pb.gm;

import fire.pb.battle.pvp.PvPControl;
import fire.pb.battle.pvp.PvPControlManager;

public class GM_pvptest extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：");
			return false;
		}

		int id2 = Integer.parseInt(args[0]);

		if (id2 == 1) {
			for (PvPControl c : PvPControlManager.getAllControl()) {
				c.setTestMode(true);
			}
			sendToGM("PVP设置为测试模式");
		} else {
			for (PvPControl c : PvPControlManager.getAllControl()) {
				c.setTestMode(false);
			}
			sendToGM("PVP设置为普通模式");
		}
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
