package fire.pb.gm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.scene.movable.NPC;

public class GM_ftb extends GMCommand {

	@Override
	boolean exec(String[] args) {

		try {

			Collection<Npc> npcs = new ArrayList<Npc>();
			npcs.addAll(SceneNpcManager.getInstance().getNpcs().values());

			List<Npc> ftbNpcs = new ArrayList<Npc>();
			for (Npc npc : npcs) {
				if (npc.getNpcID() == 10878)
					ftbNpcs.add(npc);
			}
			fire.log.LogManager.logger.info("luoji npc num:" + ftbNpcs.size());
			sendToGM("luoji npc num:" + ftbNpcs.size());
			for (Npc npc : ftbNpcs) {
				fire.log.LogManager.logger.info("npckey" + npc.getNpcKey()
						+ "posx:" + npc.getPos().toGridPos().getX() + "posy:"
						+ npc.getPos().toGridPos().getY());
				sendToGM("npckey" + npc.getNpcKey() + "posx:"
						+ npc.getPos().toGridPos().getX() + "posy:"
						+ npc.getPos().toGridPos().getY());
			}
			Collection<NPC> npcs2 = new ArrayList<NPC>();
			npcs2.addAll(fire.pb.scene.manager.SceneNpcManager.getInstance().npcs
					.values());
			List<NPC> ftbNpcs2 = new ArrayList<NPC>();
			for (NPC npc : npcs2) {
				if (npc.getNpcID() == 10878)
					ftbNpcs2.add(npc);
			}
			fire.log.LogManager.logger.info("scene npc num:" + ftbNpcs2.size());
			sendToGM("scene npc num:" + ftbNpcs2.size());
			for (NPC npc : ftbNpcs2) {
				fire.log.LogManager.logger.info("npckey" + npc.getNpcKey()
						+ "posx:" + npc.getPos().toGridPos().getX() + "posy:"
						+ npc.getPos().toGridPos().getY());
				sendToGM("npckey" + npc.getNpcKey() + "posx:"
						+ npc.getPos().toGridPos().getX() + "posy:"
						+ npc.getPos().toGridPos().getY());
			}
		} catch (Exception e) {
		}
		return true;
	}

	@Override
	String usage() {

		return null;
	}

	public static void main(String[] args) {

	}

}
