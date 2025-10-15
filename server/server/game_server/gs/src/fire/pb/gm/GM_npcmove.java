package fire.pb.gm;

import fire.msp.npc.GStartNpcAI;
import fire.pb.GsClient;

public class GM_npcmove extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length == 0)
			return false;

		final String action = args[0];
		if (action.equals("summon")) {
			fire.pb.scene.npcai.AIManager.getInstance().summonTest();
		} else if (action.equals("go")) {
			if (args.length < 3)
				return false;

			final long key = Long.parseLong(args[1]);
			final int aid = Integer.parseInt(args[2]);

			GStartNpcAI snd = new GStartNpcAI();
			snd.npckey = key;
			snd.aiid = aid;

			GsClient.sendToScene(snd);
		} else if (action.equals("load")) {
			fire.pb.scene.npcai.AIManager.getInstance().loadConfig();
		}

		return true;

	}

	@Override
	String usage() {

		return null;
	}

}
