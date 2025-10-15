package fire.pb.gm;

import fire.pb.GsClient;

public class GM_shownpc extends GMCommand {

	@Override
	boolean exec(String[] args) {
		int npcid = Integer.parseInt(args[0]);
		final fire.msp.npc.GHideOrShowNpc send = new fire.msp.npc.GHideOrShowNpc(
				npcid, 0);
		GsClient.sendToScene(send);
		return true;
	}

	@Override
	String usage() {
		return "//shownpc npcid";
	}

}
