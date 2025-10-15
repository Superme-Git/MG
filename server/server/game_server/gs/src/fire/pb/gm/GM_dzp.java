package fire.pb.gm;

import fire.pb.npc.NpcServices;
import fire.pb.npc.PReqFortuneWheel;

public class GM_dzp extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		Integer id = Integer.parseInt(args[0]);
		new PReqFortuneWheel(getGmroleid(), 0, 0, 1000, 1000, 1000, id, 0,
				null, true, NpcServices.FORTUNE_WHEEL).submit();
		return true;
	}

	@Override
	String usage() {

		return "//dzp id";
	}

}
